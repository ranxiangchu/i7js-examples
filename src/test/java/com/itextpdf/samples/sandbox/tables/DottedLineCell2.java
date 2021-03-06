/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/34555756/one-cell-with-different-border-types
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;

import java.io.File;

public class DottedLineCell2 {
    public static final String DEST = "./target/sandbox/tables/dotted_line_cell2.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new DottedLineCell2().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDoc);

        Table table = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth();
        table.setMarginBottom(30);

        Cell cell = new Cell().add(new Paragraph("left border"));
        cell.setBorder(Border.NO_BORDER);
        cell.setNextRenderer(new DottedLineCellRenderer(cell, new boolean[] {false, true, false, false}));
        table.addCell(cell);

        cell = new Cell().add(new Paragraph("right border"));
        cell.setBorder(Border.NO_BORDER);
        cell.setNextRenderer(new DottedLineCellRenderer(cell, new boolean[] {false, false, false, true}));
        table.addCell(cell);

        cell = new Cell().add(new Paragraph("top border"));
        cell.setBorder(Border.NO_BORDER);
        cell.setNextRenderer(new DottedLineCellRenderer(cell, new boolean[] {true, false, false, false}));
        table.addCell(cell);

        cell = new Cell().add(new Paragraph("bottom border"));
        cell.setBorder(Border.NO_BORDER);
        cell.setNextRenderer(new DottedLineCellRenderer(cell, new boolean[] {false, false, true, false}));
        table.addCell(cell);

        document.add(table);

        table = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth();
        table.setMarginBottom(30);

        cell = new Cell().add(new Paragraph("left and top border"));
        cell.setBorder(Border.NO_BORDER);
        cell.setNextRenderer(new DottedLineCellRenderer(cell, new boolean[] {true, true, false, false}));
        table.addCell(cell);

        cell = new Cell().add(new Paragraph("right and bottom border"));
        cell.setBorder(Border.NO_BORDER);
        cell.setNextRenderer(new DottedLineCellRenderer(cell, new boolean[] {false, false, true, true}));
        table.addCell(cell);

        cell = new Cell().add(new Paragraph("no border"));
        cell.setBorder(Border.NO_BORDER);
        table.addCell(cell);

        cell = new Cell().add(new Paragraph("full border"));
        cell.setBorder(Border.NO_BORDER);
        cell.setNextRenderer(new DottedLineCellRenderer(cell, new boolean[] {true, true, true, true}));
        table.addCell(cell);

        document.add(table);

        document.close();
    }

    private static class DottedLineCellRenderer extends CellRenderer {
        boolean[] borders;

        public DottedLineCellRenderer(Cell modelElement, boolean[] borders) {
            super(modelElement);
            this.borders = new boolean[borders.length];

            for (int i = 0; i < this.borders.length; i++) {
                this.borders[i] = borders[i];
            }
        }

        // If renderer overflows on the next area, iText uses getNextRender() method to create a renderer for the overflow part.
        // If getNextRenderer isn't overriden, the default method will be used and thus a default rather than custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new DottedLineCellRenderer((Cell) modelElement, borders);
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);
            PdfCanvas canvas = drawContext.getCanvas();
            Rectangle position = getOccupiedAreaBBox();
            canvas.saveState();
            canvas.setLineDash(0, 4, 2);

            if (borders[0]) {
                canvas.moveTo(position.getRight(), position.getTop());
                canvas.lineTo(position.getLeft(), position.getTop());
            }

            if (borders[2]) {
                canvas.moveTo(position.getRight(), position.getBottom());
                canvas.lineTo(position.getLeft(), position.getBottom());
            }

            if (borders[3]) {
                canvas.moveTo(position.getRight(), position.getTop());
                canvas.lineTo(position.getRight(), position.getBottom());
            }

            if (borders[1]) {
                canvas.moveTo(position.getLeft(), position.getTop());
                canvas.lineTo(position.getLeft(), position.getBottom());
            }

            canvas.stroke();
            canvas.restoreState();
        }
    }
}
