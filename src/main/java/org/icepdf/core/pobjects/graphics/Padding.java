package org.icepdf.core.pobjects.graphics;

import org.icepdf.core.application.ProductInfo;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * 去掉icePdf水印标识
 */
public class Padding {
    // private static byte[] padding1 = new byte[]{54, 48, 50, 93, 81, 83, 13};
    private static byte[] padding1 = new byte[]{};
    // private static byte[] padding3 = new byte[]{51, 92, 95, 13, 50, 99, 78, 89, 98, 78, 97, 86, 92, 91, 13, 61, 98, 95, 93, 92, 96, 82, 96, 13, 60, 91, 89, 102};
    private static byte[] padding3 = new byte[]{};

    static {
        int var0;
        for (var0 = 0; var0 < padding1.length; ++var0) {
            padding1[var0] = (byte) (padding1[var0] + 19);
        }

        for (var0 = 0; var0 < padding3.length; ++var0) {
            padding3[var0] = (byte) (padding3[var0] + 19);
        }

    }

    public Padding() {
    }

    public static final void getPadding(Graphics2D var0, Rectangle2D.Float var1) {
        var0.scale(1.0D, -1.0D);
        var0.setColor(new Color(186, 0, 0));
        String var2 = (new ProductInfo()).getVersion();
        String var3 = new String(padding1) + var2;
        String var4 = new String(padding3);
        byte var5 = 5;
        var0.setFont(new Font("Dialog", 1, 14));
        FontMetrics var6 = var0.getFontMetrics();
        Rectangle2D var7 = var6.getStringBounds(var4.toCharArray(), 0, var4.length(), var0);
        int var8 = (int) ((double) var1.x + ((double) var1.width - var7.getWidth()) / 2.0D);
        int var9 = -1 * var5;
        var0.drawString(var4, var8, var9);
        var7 = var6.getStringBounds(var3.toCharArray(), 0, var3.length(), var0);
        var8 = (int) ((double) var1.x + ((double) var1.width - var7.getWidth()) / 2.0D);
        var9 = -1 * (int) ((double) var1.height - var7.getHeight() - (double) var5);
        var0.drawString(var3, var8, var9);
    }

}
