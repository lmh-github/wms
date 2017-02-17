package com.gionee.wms.common;

import org.apache.commons.io.IOUtils;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Created by Pengbin on 2017/3/11.
 */
public class PdfUtil {

    public static byte[] pdf2Img(String url) throws Exception {
        return pdf2Img(url, 3.0f);
    }

    /**
     * @param url   PDF Path or URL
     * @param scale scale
     * @return JEPG byts
     * @throws Exception exception
     */
    public static byte[] pdf2Img(String url, float scale) throws Exception {
        Document document = new Document();
        if (url.startsWith("http")) {
            document.setUrl(new URL(url));
        } else {
            document.setFile(url);
        }
        BufferedImage image = (BufferedImage) document.getPageImage(0, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, 0f, Math.abs(scale));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024 * 300);
        ImageIO.write(image, "png", outputStream);
        image.flush();
        document.dispose();
        return outputStream.toByteArray();
    }

    public static void pdf2Img(InputStream inputStream, OutputStream outputStream, float scale) throws Exception {
        try {
            Document document = new Document();
            document.setInputStream(inputStream, System.currentTimeMillis() + "");
            BufferedImage image = (BufferedImage) document.getPageImage(0, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, 0f, Math.abs(scale));
            ImageIO.write(image, "png", outputStream);
            image.flush();
            document.dispose();
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(inputStream);

        }
    }
}
