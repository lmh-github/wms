package com.xiu.rm.web.action;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.xiu.common.command.result.Result;
import com.xiu.rm.common.util.SecurityBase64;
import com.xiu.rm.core.util.GranUtil;
import com.xiu.rm.facade.dto.ResourceDTO;
import com.xiu.rm.facade.dto.ResourceQueryDTO;
import com.xiu.rm.facade.dto.RmResult;
import com.xiu.rm.facade.util.RmPage;
import com.xiu.rm.manager.ResourceManager;
import com.xiu.rm.manager.util.ResourceManagerLog;
import com.xiu.rm.web.action.admin.CasAO;
import com.xiu.rm.web.util.ResourceActionUtil;
import com.xiu.rm.web.util.ResourcePropertyUtil;
import com.xiu.usermanager.provider.bean.LocalOperatorsDO;


public class ResourceExportAction extends BaseAction implements ModelDriven<ResourceQueryDTO> {

	private static final long serialVersionUID = -2104240307391256041L;

	private static final Logger logger = Logger.getLogger(ResourceExportAction.class);

	@Autowired
	private ResourceManager resourceManager;
	
	@Autowired
	private ResourceManagerLog resourceManagerLog;
	
	@Autowired
	private CasAO casAO;
	
	/**
	 * Model
	 */
	private ResourceQueryDTO resourceQueryDTO = new ResourceQueryDTO();

	/**
	 * 查询条件
	 */
	private String selectOne;
	private String selectOneValueMin;
	private String selectOneValueMax;
	
	/**
	 * 返回结果
	 */
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private RmPage page;
	private ResourcePropertyUtil resourcePropertyUtil = new ResourcePropertyUtil();
	
	/**
	 * 卡类型及描述
	 */
	private String cardType;
	private String cardTypeDesc;

	
	/**
	 * 导出资源Excel
	 * @Title: exportResourceList 
	 * @param   
	 * @return String    返回类型 
	 * @throws
	 */
	@SuppressWarnings({ "unchecked" })
	public String exportResourceList() throws Exception {				
		//组装查询条件
		Date selectOneValueMinDate = null;
		Date selectOneValueMaxDate = null;
		if(StringUtils.isNotBlank(selectOneValueMin)){
			selectOneValueMinDate = sdf.parse(selectOneValueMin);
		}
		if(StringUtils.isNotBlank(selectOneValueMax)){
			selectOneValueMaxDate = sdf.parse(selectOneValueMax);
		}

		if ("bindTime".equals(selectOne)) {
			resourceQueryDTO.setBindTimeMin(selectOneValueMinDate);
			resourceQueryDTO.setBindTimeMax(selectOneValueMaxDate);
		}else if ("begTime".equals(selectOne)) {
			resourceQueryDTO.setBegTimeMin(selectOneValueMinDate);
			resourceQueryDTO.setBegTimeMax(selectOneValueMaxDate);
		} else if ("endTime".equals(selectOne)) {
			resourceQueryDTO.setEndTimeMin(selectOneValueMinDate);
			resourceQueryDTO.setEndTimeMax(selectOneValueMaxDate);
		}
		
		//设置PageSize(查询所有)
		resourceQueryDTO.setPageSize(100000000);
		
		//处理卡类型
		resourceQueryDTO.setResTypeCode(ResourceActionUtil.getResTypeCodeFromCardType(cardType));

		try {
			//判断查询条件是否为空
			boolean isConditionNull = true;
			if(StringUtils.isNotBlank(selectOneValueMin)
					|| StringUtils.isNotBlank(selectOneValueMax)
					|| StringUtils.isNotBlank(resourceQueryDTO.getBatchCode())				
					|| StringUtils.isNotBlank(resourceQueryDTO.getBegResSeq())
					|| StringUtils.isNotBlank(resourceQueryDTO.getEndResSeq())
					|| StringUtils.isNotBlank(resourceQueryDTO.getResCode())
					|| StringUtils.isNotBlank(resourceQueryDTO.getResStatus())){
				isConditionNull = false;
			}
			if(isConditionNull){
				throw new Exception("导出条件不能为空!");
			}
						
			//判断是否有权限
			GranUtil.isGranModuble("export"+cardType+"List");	
			
			//判断文件加密密码是否符合规则(至少12位,包含有数字，字母，特殊字符)
			String filePassword = request.getParameter("filePassword");
			if(!isPasswordRule(filePassword)){
				throw new Exception("输入的文件加密密码不符合规则!");
			}
			
			//判断用户登录密码			
			LocalOperatorsDO localOperatorsDO = (LocalOperatorsDO) ActionContext.getContext().getSession().get("localOperator");
			if ((null != localOperatorsDO)) {
				String loginPassword = request.getParameter("loginPassword");
				Result logResult = casAO.authenticate(localOperatorsDO.getLoginName(), loginPassword);				
				if(!logResult.isSuccess()){
					throw new Exception("输入的登录密码错误!");
				}				
			}else{
				throw new Exception("得不到用户信息!");
			}
			
			//导出
			long startTime=System.currentTimeMillis();
			logger.info("****************开始导出Excel****************");
			RmResult result = resourceManager.queryResourceList(resourceQueryDTO);
			List<ResourceDTO> list = (ArrayList<ResourceDTO>)result.getData();			
			this.listToXML(list);
			logger.info("****************导出Excel成功，总共花费"+(System.currentTimeMillis()-startTime)/1000+"秒钟****************");
			
			//插入业务操作日志
			resourceManagerLog.logExportResourceList(resourceQueryDTO);
			
			//返回
			return null;
		} catch (Exception e) {
			//返回			
			this.infoMessages.put("errorMsg", e.getMessage());
			logger.error(e.getMessage());
			return ERROR;
		}
	}	
	
	private void listToXML(List<ResourceDTO> list) throws Exception {		
		File dir = new File("/excelFile");
		if(dir.exists()){
			this.deleteFile(dir);
		}
		if(!dir.exists()){
    		dir.mkdirs();
    	}
		
		int size = list.size();
		int count = 50000;
		int m = size/count;
		
		for (int i = 0; i <= m; i++) {
			int n = i;
			int j = n * count;
			int k = (n + 1) * count;
			if (k > size) {
				k = size;
			}
			if(j==k){
				break;
			}

			HSSFWorkbook book = new HSSFWorkbook();
	        HSSFSheet sheet0 = book.createSheet("卡信息");
	        
	        //设置单元格宽度
	        this.setSheetColumnWidth(sheet0);
	        
	        //表头
	        String[] titles = new String[] { "批次号", "序列号", "卡号", "密码", "面额(元)", "生效开始时间", "生效结束时间" };
	        HSSFRow row0 = sheet0.createRow(0);
	        for (int a = 0; a < titles.length; a++) {
	            HSSFCell cell = row0.createCell(a);  
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(titles[a]);
			}
	        
	        //内容
	        int a = 1;
			for (; j < k; j++) {	        	
	        	HSSFRow row = sheet0.createRow(a);
	        	a++;	        	
	        	this.setRowWithDTO(row, list.get(j));
			}
		  
	        //输出
	        ByteArrayOutputStream output = new ByteArrayOutputStream(); 
	        try {
	        	File tempFile = new File(dir+"/card_"+(i+1)+".xls");
	    		FileOutputStream fileOut = new FileOutputStream(tempFile);
	    		book.write(fileOut);
	    		fileOut.close();
			} catch (Exception e) {
				throw e;
			}finally{
				if (output != null){
					try {
						output.flush();
						output.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}   				
		}	
		
		//创建ZIP
		String filePassword = request.getParameter("filePassword");
		this.createZip(dir.getAbsolutePath(), filePassword);
	}
	
	private void createZip(String dirPath, String password) throws Exception{
		String zipFileName = "cards";
		File zipFile = new File(zipFileName+".zip");
		this.deleteFile(zipFile);
		
		try {			
			//将dirPath文件夹压缩成带有密码的zipFileName文件
			logger.info("***开始创建压缩文件"+zipFileName+"***");
			Process proc = Runtime.getRuntime().exec("zip -P " + password + " -r "+zipFileName+" "+dirPath);

			if(proc.waitFor()!=0){
				throw new Exception("创建zip文件失败!");
			}
			
			response.setCharacterEncoding("utf-8");
			response.setHeader("Content-disposition", "attachment; filename=cards.zip");
			response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0,private, max-age=0");
			response.setHeader("Content-Type", "application/octet-stream"); 
			response.setHeader("Content-Type", "application/zip"); 
			response.setHeader("Content-Type", "application/force-download");
			response.setHeader("Pragma", "public"); 
			response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
			FileInputStream fis = new FileInputStream(zipFile);		
			DataOutputStream dos = new DataOutputStream(response.getOutputStream());

	    	int len;
	    	byte[] buffer = new byte[1024];
	    	while ((len = fis.read(buffer)) > 0){
	    		dos.write(buffer, 0, len);
	    	}

	    	dos.flush(); 
	    	dos.close();
		    fis.close(); 
		} catch (Exception e) {
			if(zipFile.exists()){
				this.deleteFile(zipFile);
			}
			throw e;
		}finally{
			this.deleteFile(new File(dirPath));
		    this.deleteFile(zipFile);
		}

	}
	
	private void setRowWithDTO(HSSFRow row, ResourceDTO dto) throws Exception {
    	dto.setPassword(SecurityBase64.decodeAsString(dto.getPassword()));//解密
    	
        HSSFCell cell0 = row.createCell(0);  
        cell0.setCellType(HSSFCell.CELL_TYPE_STRING);   
        cell0.setCellValue(dto.getBatchCode());
        
        HSSFCell cell1 = row.createCell(1);  
        cell1.setCellType(HSSFCell.CELL_TYPE_STRING);   
        cell1.setCellValue(dto.getResSeq());
        
        HSSFCell cell2 = row.createCell(2);  
        cell2.setCellType(HSSFCell.CELL_TYPE_STRING);   
        cell2.setCellValue(dto.getResCode());
        
        HSSFCell cell3 = row.createCell(3);  
        cell3.setCellType(HSSFCell.CELL_TYPE_STRING);   
        cell3.setCellValue(dto.getPassword());
        
        HSSFCell cell4 = row.createCell(4);  
        cell4.setCellType(HSSFCell.CELL_TYPE_STRING);   
        cell4.setCellValue(dto.getAmount()/100);
        
        HSSFCell cell5 = row.createCell(5);  
        cell5.setCellType(HSSFCell.CELL_TYPE_STRING);   
        cell5.setCellValue(sdf.format(dto.getBegTime()));
        
        HSSFCell cell6 = row.createCell(6);  
        cell6.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell6.setCellValue(sdf.format(dto.getEndTime()));
	}


	/**
	 * 设置列宽
	 * @Title: setSheetColumnWidth 
	 * @param   
	 * @return void    返回类型 
	 * @throws
	 */
	private void setSheetColumnWidth(HSSFSheet sheet) {
		sheet.setColumnWidth(0, 15 * 256);
		sheet.setColumnWidth(1, 20 * 256);
		sheet.setColumnWidth(2, 20 * 256);
		sheet.setColumnWidth(3, 15 * 256);
		sheet.setColumnWidth(4, 8 * 256);
		sheet.setColumnWidth(5, 20 * 256);
		sheet.setColumnWidth(6, 20 * 256);
	}
	
	private void deleteFile(File file) {
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					this.deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
				}
			}
			file.delete();
		}
	}
	
    
	private boolean isPasswordRule(String password) {
		boolean flag = false;
		if (password.length() >= 12) {
			/** 必须包含数字 */
			String digit = "\\S*[0-9]+\\S*";
			/** 必须包含英文字符 */
			String letter = "\\S*[a-zA-Z]+\\S*";
			/** 必须包含特殊字符 */
			String especialChar = "\\S*\\W+\\S*|\\S*[_]+\\S*";
			
			if (Pattern.matches(digit, password)
					&& Pattern.matches(letter, password)
					&& Pattern.matches(especialChar, password)) {
				flag = true;
			}
		}
		return flag;
	}


	@Override
	public ResourceQueryDTO getModel() {
		return resourceQueryDTO;
	}

	public String getSelectOne() {
		return selectOne;
	}

	public void setSelectOne(String selectOne) {
		this.selectOne = selectOne;
	}
	
	public String getSelectOneValueMin() {
		return selectOneValueMin;
	}

	public void setSelectOneValueMin(String selectOneValueMin) {
		this.selectOneValueMin = selectOneValueMin;
	}

	public String getSelectOneValueMax() {
		return selectOneValueMax;
	}

	public void setSelectOneValueMax(String selectOneValueMax) {
		this.selectOneValueMax = selectOneValueMax;
	}

	public RmPage getPage() {
		return page;
	}
	
	public void setPage(RmPage page) {
		this.page = page;
	}
	
	public ResourcePropertyUtil getResourcePropertyUtil() {
		return resourcePropertyUtil;
	}
	
	public void setResourcePropertyUtil(ResourcePropertyUtil resourcePropertyUtil) {
		this.resourcePropertyUtil = resourcePropertyUtil;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardTypeDesc() {
		return cardTypeDesc;
	}

	public void setCardTypeDesc(String cardTypeDesc) {
		this.cardTypeDesc = cardTypeDesc;
	}

}
