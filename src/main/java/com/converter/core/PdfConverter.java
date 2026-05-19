package com.converter.core;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.io.File;
import java.util.List;

public class PdfConverter {

    public static void convertImagesToPdf(List<File> imageFiles, File outputPdfFile) throws Exception {
        try (PDDocument document = new PDDocument()) {
            for (File imageFile : imageFiles) {
                PDImageXObject pdImage = PDImageXObject.createFromFile(imageFile.getAbsolutePath(), document);

                PDRectangle rectangle = new PDRectangle(pdImage.getWidth(), pdImage.getHeight());
                PDPage page = new PDPage(rectangle);
                document.addPage(page);

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    contentStream.drawImage(pdImage, 0, 0, pdImage.getWidth(), pdImage.getHeight());
                }
            }
            document.save(outputPdfFile);
        }
    }
}
