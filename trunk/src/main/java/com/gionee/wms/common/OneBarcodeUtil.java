package com.gionee.wms.common;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jbarcode.JBarcode;
import org.jbarcode.encode.Code128Encoder;
import org.jbarcode.paint.TextPainter;
import org.jbarcode.paint.WideRatioCodedPainter;
import org.jbarcode.paint.WidthCodedPainter;
import org.jbarcode.util.ImageUtil;

import com.thoughtworks.xstream.core.util.Base64Encoder;

/**
 * 
 * 描述: 条码生成
 * 作者: milton.zhang
 * 日期: 2013-9-18
 */
public class OneBarcodeUtil {

	private static final int BARCODE_HEIGHT = 12;
	private static final int BARCODE_DPI = ImageUtil.DEFAULT_DPI;
	private static final String FONT_FAMILY = "consola";
	private static final int FONT_SIZE = 12;

	/**
	 * 生成条码，返回文件名
	 * @param barCode
	 * @param barCodePath
	 * @return
	 */
	public static String generateBar(String barCode, String barCodePath) {
		try {
			File path = new File(barCodePath);
			if (!path.exists()) {
				path.mkdirs();
			}
			JBarcode localJBarcode = new JBarcode(Code128Encoder.getInstance(),
					WidthCodedPainter.getInstance(),
					CustomTextPainter.getInstance());
			//生成. 欧洲商品条码(=European Article Number)
			BufferedImage localBufferedImage = localJBarcode
					.createBarcode(barCode);
			//			localJBarcode.setEncoder(Code39Encoder.getInstance());
			localJBarcode.setEncoder(Code128Encoder.getInstance());
			localJBarcode.setPainter(WideRatioCodedPainter.getInstance());
			localJBarcode.setTextPainter(CustomTextPainter.getInstance());
			localJBarcode.setShowCheckDigit(false);
			localJBarcode.setBarHeight(BARCODE_HEIGHT);
			saveToPNG(localBufferedImage, barCode + ".png", barCodePath);
			return barCode + ".png";
		} catch (Exception localException) {
			localException.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 生成条码，返回图片的base64
	 * @param barCode
	 * @param barCodePath
	 * @return
	 */
	public static String generateBarBase64(String barCode, String barCodePath) {
		try {
			File path = new File(barCodePath);
			if (!path.exists()) {
				path.mkdirs();
			}
			JBarcode localJBarcode = new JBarcode(Code128Encoder.getInstance(),
					WidthCodedPainter.getInstance(),
					CustomTextPainter.getInstance());
			//生成. 欧洲商品条码(=European Article Number)
			BufferedImage localBufferedImage = localJBarcode
					.createBarcode(barCode);
			//			localJBarcode.setEncoder(Code39Encoder.getInstance());
			localJBarcode.setEncoder(Code128Encoder.getInstance());
			localJBarcode.setPainter(WideRatioCodedPainter.getInstance());
			localJBarcode.setTextPainter(CustomTextPainter.getInstance());
			localJBarcode.setShowCheckDigit(false);
			localJBarcode.setBarHeight(BARCODE_HEIGHT);
			saveToPNG(localBufferedImage, barCode + ".png", barCodePath);
			
			String imgFile = barCodePath + barCode + ".png";
			return GetImageStr(imgFile);
		} catch (Exception localException) {
			localException.printStackTrace();
			return null;
		}
	}
	
	//图片转化成base64字符串  
    public static String GetImageStr(String imgFile)  
    {
    	//将图片文件转化为字节数组字符串，并对其进行Base64编码处理  
        InputStream in = null;  
        byte[] data = null;  
        //读取图片字节数组  
        try   
        {  
            in = new FileInputStream(imgFile);          
            data = new byte[in.available()];  
            in.read(data);  
            in.close();  
        }   
        catch (IOException e)   
        {  
            e.printStackTrace();  
        }  
        //对字节数组Base64编码  
        Base64Encoder encoder = new Base64Encoder();  
        return encoder.encode(data);//返回Base64编码过的字节数组字符串  
    }

	static void saveToJPEG(BufferedImage paramBufferedImage,
			String paramString, String barCodePath) {
		saveToFile(paramBufferedImage, paramString, "jpeg", barCodePath);
	}

	static void saveToPNG(BufferedImage paramBufferedImage, String paramString,
			String barCodePath) {
		saveToFile(paramBufferedImage, paramString, "png", barCodePath);
	}

	static void saveToGIF(BufferedImage paramBufferedImage, String paramString,
			String barCodePath) {
		saveToFile(paramBufferedImage, paramString, "gif", barCodePath);
	}

	static void saveToFile(BufferedImage paramBufferedImage,
			String paramString1, String paramString2, String barCodePath) {
		try {
			FileOutputStream localFileOutputStream = new FileOutputStream(
					barCodePath + paramString1);
			ImageUtil.encodeAndWrite(paramBufferedImage, paramString2,
					localFileOutputStream, BARCODE_DPI, BARCODE_DPI);
			localFileOutputStream.close();
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	/**
	 * 自定义的 TextPainter， 允许定义字体，大小等等。
	 */
	static class CustomTextPainter implements TextPainter {
		public static CustomTextPainter instance = new CustomTextPainter();

		public static CustomTextPainter getInstance() {
			return instance;
		}

		@Override
		public void paintText(BufferedImage barCodeImage, String text, int width) {
			Graphics g2d = barCodeImage.getGraphics();
			Font font = new Font(FONT_FAMILY, Font.PLAIN, FONT_SIZE * width);
			g2d.setFont(font);
			FontMetrics fm = g2d.getFontMetrics();
			int height = fm.getHeight();
			int center = (barCodeImage.getWidth() - fm.stringWidth(text)) / 2;
			g2d.setColor(Color.WHITE);
			g2d.fillRect(0, 0, barCodeImage.getWidth(),
					barCodeImage.getHeight() * 1 / 20);
			g2d.fillRect(0, barCodeImage.getHeight() - (height * 9 / 10),
					barCodeImage.getWidth(), (height * 9 / 10));
			g2d.setColor(Color.BLACK);
			g2d.drawString(text, center, barCodeImage.getHeight()
					- (height / 10) - 2);
		}
	}

	public static void main(String[] args) {
		OneBarcodeUtil.generateBar("2013091244220500540201", "d:/data/");
	}

}
