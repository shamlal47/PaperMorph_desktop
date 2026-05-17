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
                // Load the image
                PDImageXObject pdImage = PDImageXObject.createFromFile(imageFile.getAbsolutePath(), document);

                // Create a page with the same dimensions as the image
                PDRectangle rectangle = new PDRectangle(pdImage.getWidth(), pdImage.getHeight());
                PDPage page = new PDPage(rectangle);
                document.addPage(page);

                // Draw the image onto the page
                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    contentStream.drawImage(pdImage, 0, 0, pdImage.getWidth(), pdImage.getHeight());
                }
            }
            // Save the document
            document.save(outputPdfFile);
        }
    }
}
