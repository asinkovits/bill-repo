package com.sinkovits.rent.billrepo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;

@RestController
@RequestMapping(BillController.MAPPING)
public class BillController {

    public static final String MAPPING = "/bill";
    private static final String ID_MAPPING = "/{id}";
    private static final String FILE_MAPPING = "/file";

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping
    public Collection<Bill> get() {
        return billService.getAllBills();
    }

    @GetMapping(ID_MAPPING)
    public Bill get(@PathVariable String id) {
        return billService.getBill(id);
    }

    @GetMapping(ID_MAPPING + FILE_MAPPING)
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String id) {

        BillFile file = billService.getBillFile(id);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        //        "attachment; filename=\"" + file.getFileName() + "\"")
                        "inline; filename=\"" + file.getFileName() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, file.getType())
                .body(file.getData());
    }


    @PostMapping
    public Bill create(
            @RequestParam String name,
            @RequestParam String value) {
        Bill bill = new Bill(name, value);
        billService.saveBill(bill);
        return bill;
    }

    @PostMapping(FILE_MAPPING)
    public Bill file(@RequestParam MultipartFile file) throws IOException {
        return billService.processBillFile(file);
    }

    @PutMapping(ID_MAPPING)
    public Bill update(
            @PathVariable String id,
            @RequestParam String name,
            @RequestParam String value) {
        Bill bill = new Bill(id, name, value);
        billService.updateBill(bill);
        return bill;
    }

    @PostMapping(ID_MAPPING + FILE_MAPPING)
    public Bill updateFile(
            @PathVariable String id,
            @RequestParam MultipartFile file) throws IOException {
        return billService.updateBillFile(id, file);
    }
}
