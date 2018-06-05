package com.sinkovits.rent.billrepo.parser;

import com.sinkovits.rent.billrepo.Bill;
import com.sinkovits.rent.billrepo.BillFile;
import com.sinkovits.rent.billrepo.FileManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FSFileManager implements FileManager {

    private Path fileStoragePath;

    public FSFileManager(@Value("${fileStoragePath}") String path) {
        this.fileStoragePath = Paths.get(path);
    }

    @Override
    public void persist(Bill bill, MultipartFile file) throws IOException {
        Path dirPath = fileStoragePath.resolve(bill.getId());
        File dir = dirPath.toFile();

        if (dir.exists()) {
            dir.delete();
        }

        dir.mkdir();

        Path filePath = dirPath.resolve(file.getOriginalFilename());

        file.transferTo(filePath.toFile());
    }

    @Override
    public BillFile retrieve(String id) {

        File dir = fileStoragePath.resolve(id).toFile();
        if (!dir.exists()) {
            return null;
        }

        File[] files = dir.listFiles();
        if(files.length == 0){
            return null;
        }

        File file = files[0];

        return new BillFile(file.getName(), new FileSystemResource(file.getAbsoluteFile()));
    }
}
