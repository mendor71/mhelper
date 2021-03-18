package ru.pack.csps.service;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

@Component
public class FileCopyService {

    public void writeUserImage(byte[] base64image, File target, String fileFormat, OutputStream os) throws IOException {
        byte[] decoded = Base64.decodeBase64(base64image);
        ByteArrayInputStream bis = new ByteArrayInputStream(decoded);

        BufferedImage res = ImageIO.read(bis);
        ImageIO.write(res, fileFormat, target);

        try (InputStream is = new FileInputStream(target)) {
            int read;
            byte[] bytes = new byte[1];

            while ((read = is.read(bytes)) != -1) {
                os.write(bytes, 0, read);
            }
        } finally {
            try { os.flush(); os.close(); } catch (IOException ignored) {}
            target.delete();
        }
    }
}
