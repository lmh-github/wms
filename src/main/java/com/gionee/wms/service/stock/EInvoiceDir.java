package com.gionee.wms.service.stock;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Pengbin on 2017/3/23.
 */
public enum EInvoiceDir {
    IMG() {
        @Override
        String getSavePath(String fileName) {
            String path = EINVOICE_BASE_DIR + IMG_DIRECTORY_FORMAT.format(new Date());
            createDir(path);
            return path + fileName;
        }

        @Override
        String getResourceUrl(String fileName) {
            return IMG_DIRECTORY_FORMAT.format(new Date()) + fileName;
        }
    }, PDF() {
        @Override
        String getSavePath(String fileName) {
            String path = EINVOICE_BASE_DIR + PDF_DIRECTORY_FORMAT.format(new Date());
            createDir(path);
            return path + fileName;
        }

        @Override
        String getResourceUrl(String fileName) {
            return PDF_DIRECTORY_FORMAT.format(new Date()) + fileName;
        }
    };

    /** 发票PDF和IMG存储根目录 */
    public static final String EINVOICE_BASE_DIR = System.getProperty("WEBCONTENT.BASE.PASH");
    /** 图片目录生成 */
    private static final SimpleDateFormat IMG_DIRECTORY_FORMAT = new SimpleDateFormat("'invoice/img/'yyyy-MM-dd/");
    /** PDF目录生成 */
    private static final SimpleDateFormat PDF_DIRECTORY_FORMAT = new SimpleDateFormat("'invoice/pdf/'yyyy-MM-dd/");

    abstract String getSavePath(String fileName);

    abstract String getResourceUrl(String fileName);

    protected void createDir(String path) {
        try {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
