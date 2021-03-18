package ru.pack.csps.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pack.csps.exceptions.PropertyFindException;

import java.io.*;
import java.nio.charset.Charset;

@Service
public class PdfService {
    private SettingsService settingsService;
    //@Value("classpath:/fonts/Tahoma.ttf")
    //private URL font;

    public String writePdf(String htmlSource, String userName, String dtName) throws IOException, DocumentException, PropertyFindException {
        String targetFilePath = (String) settingsService.getProperty("tmp_dir_path") + "/" + userName + "_" + dtName + ".pdf";

        OutputStream os = new FileOutputStream(new File(targetFilePath));
        Document document = new Document();

        PdfWriter writer = PdfWriter.getInstance(document, os);
        document.open();

        XMLWorkerFontProvider fontImp = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
        fontImp.register((String) settingsService.getProperty("doc_pdf_font"));
        //fontImp.register(font.getFile());

        XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                new ByteArrayInputStream(htmlSource.getBytes("UTF-8")), null, Charset.forName("UTF-8"), fontImp);

        try {
            document.close();
            os.flush();
            os.close();
        } catch (IOException ignored) {}
        return targetFilePath;
    }

    public void writePdf(String htmlSource, OutputStream os) throws DocumentException, PropertyFindException, IOException {
        Document document = new Document();

        PdfWriter writer = PdfWriter.getInstance(document, os);
        document.open();

        XMLWorkerFontProvider fontImp = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
        fontImp.register((String) settingsService.getProperty("doc_pdf_font"));

        XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                new ByteArrayInputStream(htmlSource.getBytes("UTF-8")), null, Charset.forName("UTF-8"), fontImp);

        try {
            document.close();
            os.flush();
            os.close();
        } catch (IOException ignored) {}
    }

    @Autowired
    public void setSettingsService(SettingsService settingsService) {
        this.settingsService = settingsService;
    }
}
