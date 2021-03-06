/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/22828782/all-links-of-existing-pdf-change-the-action-property-to-inherit-zoom-itext-lib
 */
package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfNumber;

import java.io.File;

public class ChangeZoomXYZDestination {
    public static final String DEST = "./target/sandbox/annotations/change_zoom_xyz_destination.pdf";

    public static final String SRC = "./src/test/resources/pdfs/xyz_destination.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ChangeZoomXYZDestination().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));

        PdfDictionary pageDict = pdfDoc.getPage(11).getPdfObject();
        PdfArray annots = pageDict.getAsArray(PdfName.Annots);

        // Loop over the annotations
        for (int i = 0; i < annots.size(); i++) {
            PdfDictionary annotation = annots.getAsDictionary(i);
            if (PdfName.Link.equals(annotation.getAsName(PdfName.Subtype))) {
                PdfArray d = annotation.getAsArray(PdfName.Dest);
                if (d != null && d.size() == 5 && PdfName.XYZ.equals(d.getAsName(1))) {

                    // Change the zoom factor of the current link to 0
                    d.set(4, new PdfNumber(0));
                }
            }
        }

        pdfDoc.close();
    }
}
