package com.sinkovits.rent.billrepo.parser;

import com.sinkovits.rent.billrepo.Bill;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Component
public class PdfFileParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(PdfFileParser.class);

    private final BasicBillParser billParser;

    @Autowired
    public PdfFileParser(BasicBillParser billParser) {
        this.billParser = billParser;
    }

    public Bill parse(InputStream inputStream) throws IOException {
        String text = parsePdf(inputStream);
        return billParser.parse(text);
    }

    private String parsePdf(InputStream inputStream) throws IOException {
        PDFTextStripper pdfStripper = null;
        PDDocument pdDoc = null;
        COSDocument cosDoc = null;
        try {
            PDFParser parser = new PDFParser(new RandomAccessBufferedFileInputStream(inputStream));
            parser.parse();
            cosDoc = parser.getDocument();
            pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);
            pdfStripper.setStartPage(1);
            pdfStripper.setEndPage(1);
            String parsedText = pdfStripper.getText(pdDoc);
            return parsedText;
        } finally {
            close(cosDoc);
        }

    }

    private void close(COSDocument cosDoc) {
        if (cosDoc != null)
            try {
                cosDoc.close();
            } catch (IOException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
    }
}
