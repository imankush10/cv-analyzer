package com.cvanalyzer.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Service
public class TextExtractionService {

    public String extractTextFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        String fileName = file.getName().toLowerCase();

        if (fileName.endsWith(".pdf")) {
            return extractTextFromPDF(filePath);
        } else if (fileName.endsWith(".docx")) {
            return extractTextFromDOCX(filePath);
        } else if (fileName.endsWith(".doc")) {
            return extractTextFromDOC(filePath);
        } else {
            throw new IllegalArgumentException("Unsupported file format: " + fileName);
        }
    }

    private String extractTextFromPDF(String filePath) throws IOException {
        try (PDDocument document = PDDocument.load(new File(filePath))) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private String extractTextFromDOCX(String filePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath);
             XWPFDocument document = new XWPFDocument(fis)) {

            StringBuilder text = new StringBuilder();
            List<XWPFParagraph> paragraphs = document.getParagraphs();

            for (XWPFParagraph paragraph : paragraphs) {
                text.append(paragraph.getText()).append("\n");
            }

            return text.toString();
        }
    }

    private String extractTextFromDOC(String filePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath);
             HWPFDocument document = new HWPFDocument(fis)) {

            WordExtractor extractor = new WordExtractor(document);
            return extractor.getText();
        }
    }
}
