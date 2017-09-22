package com.gionee.wms.common;

import org.apache.commons.io.FileUtils;
import org.jdom2.internal.SystemProperty;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: huyunfan
 * Date: 2017/9/22
 * Time: 13:50
 */
public class FileUtil {

    private static final String FORMAT_DATE = "yyyy/MM/dd";

    /**
     * 单个文件对象上传并返回
     *
     * @param multipartFile
     * @return
     */
    public static FileInfo upload(MultipartFile multipartFile) {
        FileInfo fileInfo = null;

        if (multipartFile != null) {
            String originalFileName = multipartFile.getOriginalFilename();

            if (!StringUtils.isEmpty(originalFileName)) {
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_DATE);

                    fileInfo = new FileInfo();
                    fileInfo.setFileOriginName(originalFileName);
                    fileInfo.setSuffix(originalFileName.substring(originalFileName.indexOf(".")));
                    fileInfo.setFileName(multipartFile.hashCode() + fileInfo.getSuffix());
                    fileInfo.setDirPath(simpleDateFormat.format(new Date()));

                    FileUtils.writeByteArrayToFile(
                        new File(fileInfo.getDirPath() + "/" + fileInfo.getFileName()), multipartFile.getBytes());

                } catch (IOException e) {
                    fileInfo = null;
                    e.printStackTrace();
                }
            }
        }

        return fileInfo;
    }

    /**
     * 多文件上传返回文件对象数组
     *
     * @param multipartFiles
     * @return
     */
    public static FileInfo[] upload(MultipartFile[] multipartFiles) {
        FileInfo[] fileArray = null;

        if (multipartFiles != null && multipartFiles.length > 0) {

            fileArray = new FileInfo[multipartFiles.length];

            for (int i = 0; i < multipartFiles.length; i++) {
                fileArray[i] = upload(multipartFiles[i]);
            }
        }
        return fileArray;
    }

    /**
     * 组合多个文件打zip包
     *
     * @param files    压缩文件源
     * @param downName 压缩包内文件名称
     * @param zipFile  目标文件
     * @return
     */
    public static File downZip(File[] files, String[] downName, File zipFile) {
        try {
            InputStream input;
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));

            for (int i = 0; i < files.length; ++i) {
                input = new FileInputStream(files[i]);
                zipOut.putNextEntry(new ZipEntry(downName[i]));

                int temp;
                while ((temp = input.read()) != -1) {
                    zipOut.write(temp);
                }
                input.close();
            }
            zipOut.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return zipFile;
    }


    /**
     * 文件对象
     */
    public static class FileInfo {
        private static final String UPLOAD_FILE = SystemProperty.get("wms.file.upload.path", "E:\\");

        /**
         * 文件原始名称
         */
        private String fileOriginName;
        /**
         * 文件名称
         */
        private String fileName;
        /**
         * 后缀
         */
        private String suffix;
        /**
         * 文件路径
         */
        private String dirPath;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getSuffix() {
            return suffix;
        }

        public void setSuffix(String suffix) {
            this.suffix = suffix;
        }

        public String getFileOriginName() {
            return fileOriginName;
        }

        public void setFileOriginName(String fileOriginName) {
            this.fileOriginName = fileOriginName;
        }

        public String getDirPath() {
            return dirPath;
        }

        public void setDirPath(String dirPath) {
            this.dirPath = UPLOAD_FILE + dirPath;
        }

        @Override
        public String toString() {
            // 文件路径+文件名+","+原始文件名
            return dirPath + "/" + fileName + "," + fileOriginName;
        }
    }


}

