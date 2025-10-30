package model.services;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import model.entities.Account;
import model.entities.Transaction;

import java.io.FileOutputStream;
import java.util.List;

public class PDFService {

    public static String exportStatement(Account account, String ownerName, List<Transaction> transactions, String outPath) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(outPath));
        document.open();
        document.add(new Paragraph("Extrato Bancário"));
        document.add(new Paragraph("Cliente: " + ownerName));
        document.add(new Paragraph("Conta: " + account.getAccountNumber() + " (" + account.getType() + ")"));
        document.add(new Paragraph("Saldo atual: " + account.getBalance()));
        document.add(new Paragraph(" "));
        for (Transaction t : transactions) {
            document.add(new Paragraph(t.getCreatedAt() + " - " + t.getType() + " - " + t.getDescription() + " - " + t.getAmount() + " - saldo:" + t.getBalanceAfter()));
        }
        document.close();
        return outPath;
    }
}
