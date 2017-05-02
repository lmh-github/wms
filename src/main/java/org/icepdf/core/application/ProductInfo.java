package org.icepdf.core.application;

/**
 * 去掉icePdf水印标识
 */
public class ProductInfo {
    public static String COMPANY = "";
    public static String PRODUCT = "";
    public static String PRIMARY = "";
    public static String SECONDARY = "";
    public static String TERTIARY = "";
    public static String RELEASE_TYPE = "";
    public static String BUILD_NO = "";
    public static String REVISION = "";

    public static void main(String[] paramArrayOfString) {
        ProductInfo localProductInfo = new ProductInfo();
        System.out.println(localProductInfo.toString());
    }

    public String toString() {

        return "";
    }

    public String getVersion() {

        return "";
    }
}
