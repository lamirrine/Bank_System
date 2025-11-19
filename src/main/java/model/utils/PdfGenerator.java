package model.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import model.entities.Transaction;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PdfGenerator {

    public static String generateStatement(List<Transaction> transactions,
                                           String accountInfo,
                                           Date startDate,
                                           Date endDate,
                                           int customerId) throws Exception {

        // Criar diretório se não existir
        File downloadsDir = new File(System.getProperty("user.home"), "Downloads");
        if (!downloadsDir.exists()) {
            downloadsDir.mkdirs();
        }

        // Nome do arquivo
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = dateFormat.format(new Date());
        String fileName = "Extrato_" + customerId + "_" + timestamp + ".pdf";
        String filePath = new File(downloadsDir, fileName).getAbsolutePath();

        // Criar PDF com iText 5
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));

        document.open();

        try {
            // Cabeçalho
            addHeader(document, accountInfo, startDate, endDate);

            // Tabela de transações
            addTransactionsTable(document, transactions);

            // Rodapé
            addFooter(document);

        } finally {
            document.close();
        }

        return filePath;
    }

    private static void addHeader(Document document, String accountInfo, Date startDate, Date endDate)
            throws DocumentException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        // Título
        Paragraph title = new Paragraph("EXTRATO BANCÁRIO");
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

        // Informações da conta e período
        document.add(new Paragraph(" ")); // Espaço

        Paragraph accountParagraph = new Paragraph("Conta: " + accountInfo);
        document.add(accountParagraph);

        Paragraph periodParagraph = new Paragraph(
                "Período: " + dateFormat.format(startDate) + " a " + dateFormat.format(endDate));
        document.add(periodParagraph);

        Paragraph dateParagraph = new Paragraph(
                "Data de emissão: " + dateFormat.format(new Date()));
        document.add(dateParagraph);

        document.add(new Paragraph(" ")); // Espaço
    }

    private static void addTransactionsTable(Document document, List<Transaction> transactions)
            throws DocumentException {
        // Criar tabela com 5 colunas
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);

        // Cabeçalho da tabela
        table.addCell(createCell("Data", true));
        table.addCell(createCell("Descrição", true));
        table.addCell(createCell("Tipo", true));
        table.addCell(createCell("Valor", true));
        table.addCell(createCell("Saldo", true));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        // Adicionar transações
        for (Transaction transaction : transactions) {
            table.addCell(createCell(dateFormat.format(transaction.getTimestamp()), false));
            table.addCell(createCell(transaction.getDescription() != null ? transaction.getDescription() : "", false));
            table.addCell(createCell(transaction.getType().toString(), false));

            String amountText = String.format("MZN %,.2f", Math.abs(transaction.getAmount()));
            if (transaction.getAmount() >= 0) {
                table.addCell(createCell("+" + amountText, false));
            } else {
                table.addCell(createCell("-" + amountText, false));
            }

            table.addCell(createCell(String.format("MZN %,.2f", transaction.getResultingBalance()), false));
        }

        document.add(table);
    }

    private static PdfPCell createCell(String text, boolean isHeader) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        if (isHeader) {
            cell.setBackgroundColor(new com.itextpdf.text.BaseColor(240, 240, 240));
        }
        return cell;
    }

    private static void addFooter(Document document) throws DocumentException {
        document.add(new Paragraph(" ")); // Espaço

        Paragraph footer = new Paragraph(
                "Este extrato foi gerado eletronicamente e não necessita de assinatura.");
        footer.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(footer);
    }
}