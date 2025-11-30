package com.hotel.management.payment;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.Date;
import java.util.UUID;

@Component
public class ReceiptPDFGenerator {

    // Configure as needed
    private final String receiptsDir = "./receipts";

    public static class ReceiptResult {
        public final byte[] bytes;
        public final String path;
        public ReceiptResult(byte[] bytes, String path) {
            this.bytes = bytes;
            this.path = path;
        }
    }

    public ReceiptResult generateReceiptAndSave(String transactionId,
                                  Double amount,
                                  Integer bookingId,
                                  Integer facilityBookingId,
                                  String customerName) throws Exception {

        
        File dir = new File(receiptsDir);
        if (!dir.exists()) dir.mkdirs();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter.getInstance(doc, out);

        doc.open();
        doc.add(new Paragraph("HOTEL PAYMENT RECEIPT", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
        doc.add(new Paragraph(" "));
        doc.add(new Paragraph("Customer: " + customerName));
        doc.add(new Paragraph("Transaction ID: " + transactionId));
        doc.add(new Paragraph("Amount: Rs " + amount));
        doc.add(new Paragraph("Booking ID: " + (bookingId != null ? bookingId : "-")));
        doc.add(new Paragraph("Facility Booking ID: " + (facilityBookingId != null ? facilityBookingId : "-")));
        doc.add(new Paragraph("Date: " + new Date()));
        doc.close();

        byte[] pdfBytes = out.toByteArray();

        
        String filename = "receipt_" + transactionId + ".pdf";
        File file = new File(dir, filename);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(pdfBytes);
        }

        String path = file.getAbsolutePath();
        return new ReceiptResult(pdfBytes, path);
    }
}
