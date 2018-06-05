package com.sinkovits.rent.billrepo;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

public class BillFile {
    private final String fileName;
    private final Resource data;

    public BillFile(String fileName, Resource data) {
        this.fileName = fileName;
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public Resource getData() {
        return data;
    }

    public String getType() {
        return MediaType.APPLICATION_PDF_VALUE;
    }
}
