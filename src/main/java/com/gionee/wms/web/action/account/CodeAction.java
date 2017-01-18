package com.gionee.wms.web.action.account;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.gionee.wms.common.JsonUtils;
import com.gionee.wms.common.excel.excelexport.module.ExcelModule;
import com.gionee.wms.common.excel.excelexport.userinterface.ExcelExpUtil;
import com.gionee.wms.common.excel.excelimport.bean.ExcelData;
import com.gionee.wms.common.excel.excelimport.userinterface.ExcelImportUtil;
import com.gionee.wms.common.excel.excelimport.util.ExcelDataUtil;
import com.gionee.wms.dto.BaseParam;
import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.AttrSet;
import com.gionee.wms.entity.Category;
import com.gionee.wms.entity.Sku;
import com.gionee.wms.entity.SkuAttr;
import com.gionee.wms.entity.SkuItem;
import com.gionee.wms.entity.Supplier;
import com.gionee.wms.entity.Wares;
import com.gionee.wms.service.stock.StockInService;
import com.gionee.wms.service.stock.StockService;
import com.gionee.wms.service.wares.SkuAttrService;
import com.gionee.wms.service.wares.WaresService;
import com.google.common.collect.Maps;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 动态生成验证码，供登录时使用
 * 
 * @author andy
 */
@Controller("CodeAction")
public class CodeAction extends ActionSupport {

	// 产生四个0~9的随机数，放在一个字符串里
	public String createRandomString() {
		String str = "";
		for (int i = 0; i < 4; i++) {
			str += Integer.toString((new Double(Math.random() * 10)).intValue());
		}
		return str;
	}

	// 随机产生一个颜色
	public Color createsRandomColor() {
		int r = (new Double(Math.random() * 256)).intValue();
		int g = (new Double(Math.random() * 256)).intValue();
		int b = (new Double(Math.random() * 256)).intValue();
		return new Color(r, g, b);
	}

	// 生成一个内存图片，将四个随机数写在图片上
	public BufferedImage createImage(String str) {
		int width = 60;
		int height = 22;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// 获取图形上下文
		Graphics g = image.getGraphics();
		// 设定背景色
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		// 画边框
		g.setColor(Color.black);
		g.drawRect(0, 0, width - 1, height - 1);
		// 将认证码显示到图象中
		g.setFont(new Font("Atlantic Inline", Font.PLAIN, 18));
		// 使用随机颜色
		g.setColor(this.createsRandomColor());
		// 将随机字符串的每个数字分别写到图片上
		g.drawString(Character.toString(str.charAt(0)), 8, 17);
		g.drawString(Character.toString(str.charAt(1)), 20, 17);
		g.drawString(Character.toString(str.charAt(2)), 33, 17);
		g.drawString(Character.toString(str.charAt(3)), 45, 17);
		// 图象生效
		g.dispose();
		return image;
	}

	// 将图片的以字节形式写到InputStream里
	public ByteArrayInputStream createInputStream() throws Exception {
		// 获取随机字符串
		String str = this.createRandomString();
		BufferedImage image = this.createImage(str);
		// 将产生的字符串写入session，供校验时使用

		HttpSession session = ServletActionContext.getRequest().getSession();
		session.setAttribute("validateCode", str);
		// System.out.println("str  "+str);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ImageOutputStream imageOut = ImageIO.createImageOutputStream(output);
		ImageIO.write(image, "JPEG", imageOut);
		imageOut.close();
		ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
		// System.out.println(input.toString());
		output.close();
		return input;
	}

	@Autowired
	private SkuAttrService skuAttrService;

	public void setSkuAttrService(SkuAttrService skuAttrService) {
		this.skuAttrService = skuAttrService;
	}

	private void skuAttr_addTest() {
		SkuAttr skuAttr = new SkuAttr();
		skuAttr.setAttrName("模式");
		AttrSet attrSet = new AttrSet();
		attrSet.setId(11L);
		skuAttr.setAttrSet(attrSet);
		List list = new ArrayList();
		SkuItem item = new SkuItem();
		item.setItemName("3G");
		list.add(item);
		item = new SkuItem();
		item.setItemName("4G");
		list.add(item);
		skuAttr.setItemList(list);
		skuAttrService.addAttr(skuAttr);
	}

	private void skuAttr_deleteTest() {

		skuAttrService.deleteAttr(32l);
	}

	private void skuAttr_updateTest() {
		SkuAttr attr = new SkuAttr();
		attr.setId(32l);
		attr.setAttrName("模式2");

		// //新选项
		// SkuItem item = new SkuItem();
		// item.setAttrId(36l);
		// item.setItemName("40G");
		// attr.getItemList().add(item);
		// //旧选项
		// item = new SkuItem();
		// item.setId(39l);
		// item.setAttrId(36l);
		// item.setItemName("4G");
		// attr.getItemList().add(item);
		skuAttrService.updateAttr(attr);

	}

	private WaresService waresService;

	@Autowired
	public void setWaresService(WaresService waresService) {
		this.waresService = waresService;
	}

	private void wares_getWaresTest() {
		BaseParam baseParam = new BaseParam();
		Page page = new Page();
		page.setPageSize(baseParam.getPageSize());
		Map<String,Object> criteria = Maps.newHashMap();
		criteria.put("waresName", "风华2");
		int total = waresService.getWaresListTotal(criteria);
		page = Page.getPage(1, total, baseParam.getPageSize()); // 计算出起始行和结束行，page对象
		baseParam.setFirstRow(page.getStartRow());
		baseParam.setLastRow(page.getEndRow());
		List waresList = waresService.getWaresList(criteria, page);
		JsonUtils jsonUtils = new JsonUtils();
		System.out.println(jsonUtils.toJson(waresList));
		Wares wares = waresService.getWares(63L);
		System.out.println(jsonUtils.toJson(wares));
	}

	private void wares_addWaresTest() {
		Wares wares = new Wares();
		wares.setWaresName("风华2");
		wares.setWaresCode("");
		wares.setWaresBrand("金立");
		wares.setCreateTime(new Date());
		wares.setEnabled(1);
		Category cat = new Category();
		cat.setId(3l);
		wares.setCategory(cat);
		AttrSet attrSet = new AttrSet();
		attrSet.setId(1l);
		wares.setAttrSet(attrSet);
		waresService.addWares(wares);
	}

	private void wares_updateWaresTest() {
		Wares wares = new Wares();
		wares.setId(63L);
		// wares.setWaresName("风华1");
		// wares.setWaresCode("");
		// wares.setWaresBrand("金立");
		wares.setEnabled(0);
		// Category cat = new Category();
		// cat.setId(18L);
		// wares.setCategory(cat);
		// AttrSet attrSet = new AttrSet();
		// attrSet.setId(2L);
		// wares.setAttrSet(attrSet);
		waresService.updateWares(wares);
	}

	private void wares_deleteWaresTest() {
		waresService.deleteWares(63L);
	}

	private void sku_addSkuTest() {
		Sku sku = new Sku();
		sku.setSkuCode("1102");
		sku.setSkuName("风华2红色4G");
		sku.setSkuBarcode("4761813743220");
//		sku.setMeasureUnit("个");
		sku.setItemIds("1,3");
		sku.setCreateTime(new Date());
//		sku.setEnabled(true);
		sku.setRemark("");
		Supplier supplier = new Supplier();
		supplier.setId(55L);
		supplier.setSupplierName("金铭");
//		sku.setSupplier(supplier);
		Wares wares = new Wares();
		wares.setId(66L);
		wares.setWaresName("风华2");
		sku.setWares(wares);
		waresService.addSku(sku);
	}

	private void sku_updateSkuTest() {
		Sku sku = new Sku();
		sku.setId(77L);
		// sku.setSkuCode("1103");
		// sku.setSkuName("风华2红色32G");
		// sku.setSkuBarcode("4761813743230");
		// sku.setMeasureUnit("个");
		// sku.setItemIds("1,3");
		// sku.setCreateTime(new Date());
//		sku.setEnabled(false);
		// sku.setRemark("");
		// Supplier supplier = new Supplier();
		// supplier.setId(56L);
		// sku.setSupplier(supplier);
		waresService.updateSku(sku);
	}

	private void sku_deleteSkuTest() {
		waresService.deleteSku(77L);
	}

	private void sku_getSkuTest() {
		BaseParam baseParam = new BaseParam();
		Page page = new Page();
		page.setPageSize(baseParam.getPageSize());
		Map<String,Object> criteria = Maps.newHashMap();
		criteria.put("skuName", "风华2");
		int total = waresService.getSkuListTotal(criteria);
		page = Page.getPage(1, total, baseParam.getPageSize()); // 计算出起始行和结束行，page对象
		List waresList = waresService.getSkuList(criteria, page);
		JsonUtils jsonUtils = new JsonUtils();
		System.out.println(jsonUtils.toJson(waresList));
		// Wares wares = waresService.getWares(63L);
		// System.out.println(jsonUtils.toJson(wares));
	}

	private StockService stockService;

	@Autowired
	public void setStockService(StockService stockService) {
		this.stockService = stockService;
	}

	private void stock_addStockTest() {
//		Stock stock = new Stock(1L, 77L);
		// Sku sku = new Sku();
		// sku.setId(77L);
		// stock.setSku(sku);
		// Warehouse wh = new Warehouse();
		// wh.setId(1L);
		// stock.setWarehouse(wh);
		// stock.setLimitUpper(1000);
		// stock.setLimitLower(20);
//		stock.setSalesQuantity(80);
//		stock.setTotalQuantity(stock.getSalesQuantity());
//		stockService.addStock(stock);
	}

	private void stock_getStockTest() {
//		BaseParam baseParam = new BaseParam();
//		Page page = new Page();
//		page.setPageSize(baseParam.getPageSize());
//		int total = stockService.getStockListTotal("风华2");
//		page = Page.getPage(1, total, baseParam.getPageSize()); // 计算出起始行和结束行，page对象
//		baseParam.setFirstRow(page.getStartRow());
//		baseParam.setLastRow(page.getEndRow());
//		List waresList = stockService.getStockList("风华2", baseParam);
//		JsonUtils jsonUtils = new JsonUtils();
//		System.out.println(jsonUtils.toJson(waresList));
		// Wares wares = waresService.getWares(63L);
		// System.out.println(jsonUtils.toJson(wares));
	}

	private void stock_updateStockQuantityTest() {
//		stockService.increaseStock(1L, 77L, StockType.STOCK_OCCUPY, 1, StockBizType.IN_DEBIT, "1111111111");
//		stockService.decreaseStock(1L, 77L, StockType.STOCK_SALES, 1, StockBizType.OUT_SALES, "22222222222");
	}

	private void stock_getStockChangeTest() {
//		BaseParam baseParam = new BaseParam();
//		Page page = new Page();
//		page.setPageSize(baseParam.getPageSize());
//		int total = stockService.getStockChangeListTotal("1103");
//		page = Page.getPage(1, total, baseParam.getPageSize()); // 计算出起始行和结束行，page对象
//		baseParam.setFirstRow(page.getStartRow());
//		baseParam.setLastRow(page.getEndRow());
//		List waresList = stockService.getStockChangeList("1103", baseParam);
//		JsonUtils jsonUtils = new JsonUtils();
//		System.out.println(jsonUtils.toJson(waresList));
		// Wares wares = waresService.getWares(63L);
		// System.out.println(jsonUtils.toJson(wares));
	}

	private void indiv_addIndivTest() {
//		Indiv stockIndiv = new StockIndiv();
//		Warehouse warehouse = new Warehouse();
//		warehouse.setId(1L);
//		Sku sku = new Sku();
//		sku.setId(77L);
//		stockIndiv.setWarehouse(warehouse);
//		stockIndiv.setSku(sku);
//		stockIndiv.setIndivCode("1111111111");
//		stockIndiv.setBatchNumber("20130506");
//		stockIndiv.setWaresStatus(IndivWaresStatus.DEFECTIVE.toString());
//		stockIndiv.setStockStatus(IndivStockStatus.IN_WAREHOUSE.toString());
//		stockIndiv.setRemark("毁坏了");
//		stockService.addStockIndiv(stockIndiv);
	}

	private void indiv_updateStockIndivTest() {
//		StockIndiv indiv = new StockIndiv();
//		indiv.setId(117L);
//		indiv.setIndivCode("222222222");
//		indiv.setWaresStatus("1");
//		indiv.setStockStatus("2");
//		indiv.setBatchNumber("20130507");
//		indiv.setRemark("修好了");
//		stockService.updateStockIndiv(indiv);
	}

	private void indiv_getStockIndivTest() {
//		BaseParam baseParam = new BaseParam();
//		Page page = new Page();
//		page.setPageSize(baseParam.getPageSize());
//		int total = stockService.getStockIndivListTotal(87L);
//		page = Page.getPage(1, total, baseParam.getPageSize()); // 计算出起始行和结束行，page对象
//		baseParam.setFirstRow(page.getStartRow());
//		baseParam.setLastRow(page.getEndRow());
//		List waresList = stockService.getStockIndivList(87L, baseParam);
//		JsonUtils jsonUtils = new JsonUtils();
//		System.out.println(jsonUtils.toJson(waresList));
		// Wares wares = waresService.getWares(63L);
		// System.out.println(jsonUtils.toJson(wares));
	}

	private StockInService stockInService;

	@Autowired
	public void setStockInService(StockInService stockInService) {
		this.stockInService = stockInService;
	}


	private void stockCheck_excelTest() throws Exception {
		// 导入
		String xmlFile = "config/excel/excel_desc.xml";
		InputStream importExcelStream = getClass().getClassLoader().getResourceAsStream("config/excel/info_CRM.xls");// .getResource("config/spring/spring-wms-jdbc.xml")
		ExcelData data = ExcelImportUtil.readExcel(xmlFile, importExcelStream);
		System.out.println(data);
		
		
		// 导出
		ExcelModule excelModule = new ExcelModule(ExcelDataUtil.changeExcelDataToSimple(data).getRepeatData());
		// 导出excel时使用的模板：exp_templete.xls
		// String templeteFile = new File(System.getProperty("user.dir"),
		// "exp_templete.xls").getAbsolutePath();
		String templeteFile = getClass().getClassLoader().getResource("/config/excel/exp_templete.xls").getPath()
				.substring(1);
		System.out.println(templeteFile);
		// 导出的文件名：exp_out.xls
		HttpServletResponse response = ServletActionContext.getResponse();
		response.reset();
		// 清空输出流
		response.reset();
		// 设置响应头和下载保存的文件名
		response.setHeader("content-disposition", "attachment;filename=" + "exp_out.xls");
		// 定义输出类型
		response.setContentType("APPLICATION/msexcel");

		File outFile = ExcelExpUtil.expExcel(excelModule, templeteFile, getClass().getClassLoader().getResource("/")
				.getPath().substring(1)
				+ "config/excel/exp_out.xls");
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(outFile);
			out = response.getOutputStream();
			int i = 0;
			while ((i = in.read()) != -1) {
				out.write(i);
			}
			out.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭流，不可少
			if (in != null)
				in.close();
			if (out != null)
				out.close();
		}
	}

	@Override
	public String execute() throws Exception {
		// sku属性服务测试
		// skuAttr_addTest();
		// skuAttr_deleteTest();
		// skuAttr_updateTest();
		// wares商品服务测试
		// wares_addWaresTest();
		// wares_getWaresTest();
		// wares_updateWaresTest();
		// wares_deleteWaresTest();
		// sku_addSkuTest();
		// sku_updateSkuTest();
		// sku_deleteSkuTest();
		// sku_getSkuTest();
		// stock_addStockTest();
		// stock_getStockTest();
		// stock_updateStockQuantityTest();
		// stock_getStockChangeTest();
		// indiv_addStockIndivTest();
		// indiv_updateStockIndivTest();
		// indiv_getStockIndivTest();
		// stockIn_addStockInTest();
		// stockIn_getStockInTest();
//		 stockCheck_addStockCheckTest();
//		stockCheck_getStockCheckTest(401L);
//		stockCheck_excelTest();

		setInputStream(createInputStream());
		return SUCCESS;
	}

	public ByteArrayInputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(ByteArrayInputStream inputStream) {
		this.inputStream = inputStream;
	}

	private static final long serialVersionUID = 1L;
	private ByteArrayInputStream inputStream;

}
