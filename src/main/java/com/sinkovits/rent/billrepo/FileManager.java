package com.sinkovits.rent.billrepo;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileManager {

    void persist(Bill bill, MultipartFile file) throws IOException;

    BillFile retrieve(String id);

}
