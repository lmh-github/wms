package com.gionee.wms.service.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.gionee.wms.common.DateConvert;
import com.gionee.wms.dao.CommonDao;
import com.gionee.wms.service.ServiceException;

@Service("commonService")
public class CommonServiceImpl implements CommonService {
	// private static String jhBatchCode = "";
	private CommonDao commonDao;

	@Override
	public String getBizCode(final int bizType, String... args) {
		String bizCode = "";
		switch (bizType) {
			case PURCHASE_IN:
				bizCode = generateCode("11");
				break;
			case SHORTCUT_IN:
				bizCode = generateCode("11");
				break;
			case RMA_IN:
				bizCode = generateCode("12");
				break;
			case BATCH_OUT:
				bizCode = generateJHCode();
				break;
			case DELIVERY:
				bizCode = generateCode("14");
				break;
			case PUR_PRE_RECV:
				bizCode = generateCode("15");
				break;
			case STOCK_CHECK:
				bizCode = generateCode("16");
				break;
			case PURCHASE_NUM:
				bizCode = generateCode("CG");
				break;
			case TRANSFER:
				bizCode = generateCode("17");
				break;
			case REFUSE_IN:
				bizCode = generateCode("18");
				break;
			case ORDER_BACK:
				bizCode = generateCode("19");
				break;
			case ORDER_EXCHANGE:
				bizCode = generateCode("20");
				break;
			default:
				break;
		}
		return bizCode;
	}

	private String generateCode(String codeHead) {
		try {
			DateFormat df = new SimpleDateFormat("yyMMdd");
			long currval = commonDao.querySequenceNextval("WMS_SEQ_BIZ");
			return codeHead + df.format(new Date()) + StringUtils.leftPad(String.valueOf(currval), 5, '0');
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 生成拣货批次号
	 * 前缀：用JH表示
	 * 年：用字母表示, A表示2014, B表示2015, C表示2016……依次类推
	 * 月：用字母表示, A表示1月, B表示2月, C表示3月……依次类推
	 * 日：用两位数字表示，比如01表示1号……
	 * 批次号：用三位数字表示，比如001表示某天的第1批次
	 * @param codeHead
	 * @return
	 */
	private String generateJHCode() {
		int iBaseYear = 51;
		int iYear = Integer.valueOf(DateConvert.convertD2String(new Date(), "yy"));
		char cYear = (char) (iBaseYear + iYear);
		// System.out.println(cYear);

		int iBaseMonth = 64;
		int iMonth = Integer.valueOf(DateConvert.convertD2String(new Date(), "MM"));
		char cMonth = (char) (iBaseMonth + iMonth);
		// System.out.println(cMonth);

		String sDay = DateConvert.convertD2String(new Date(), "dd");
		// System.out.println(sDay);
		StringBuilder jhCode = new StringBuilder("JH");
		jhCode.append(cYear).append(cMonth).append(sDay);
		// System.out.println(jhCode.toString());
		String jhBatchCode = "001";
		BufferedReader reader = null;
		BufferedWriter writer = null;
		try {
			String basePath = CommonServiceImpl.class.getResource("/").toString();
			System.out.println("basePath=" + basePath + "jhBatchCode.txt");
			// int endIndex=basePath.indexOf("WEB-INF");//file:/D:/Tomcat/webapps/wms/WEB-INF/classes/
			// if(endIndex==-1){
			// endIndex=basePath.indexOf("target");//兼容用Eclipse中的Jetty插件跑项目
			// }
			int beginIndex = "file:".length();
			basePath = basePath.substring(beginIndex);// 去掉前缀file:/
			reader = new BufferedReader(new FileReader(basePath + "jhBatchCode.txt"));
			String code = "";
			while ((code = reader.readLine()) != null) {
				jhBatchCode = code;
				// System.out.println(jhBatchCode);
			}
			int jhBC = Integer.valueOf(jhBatchCode) + 1;
			String newJHBatchCode = StringUtils.leftPad(String.valueOf(jhBC), 3, '0');
			writer = new BufferedWriter(new FileWriter(basePath + "jhBatchCode.txt"));
			writer.write(newJHBatchCode);
			// System.out.println(newJHBatchCode);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
				}
			}
		}
		System.out.println(jhCode.toString() + jhBatchCode);
		return jhCode.toString() + jhBatchCode;
	}

	@Autowired
	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}

	/**
	 * 系统启动时完成相关初始化工作
	 */
	@PostConstruct
	public void initSequence() {

	}

	public static void main(String[] args) {
		new CommonServiceImpl().generateJHCode();
	}

}
