package com.gionee.wms.web.ws;

import javax.jws.WebParam;
import javax.jws.WebService;

import org.apache.cxf.annotations.WSDLDocumentation;

import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.web.ws.response.WSResult;
import com.gionee.wms.web.ws.response.dto.PurPreRecvDTO;

/**
 * @ClassName: PurchaseSoapService
 * @Description: 采购服务soap接口
 * @author Kevin
 * @date 2013-8-5 下午05:30:35
 * 
 */
@WebService(name = "PurchaseSoapService", targetNamespace = WmsConstants.TARGET_NS)
public interface PurchaseSoapService {
	/**
	 * 测试
	 */
	@WSDLDocumentation("测试方法")
	String helloWorld();

	/**
	 * 提交采购预收单
	 */
	@WSDLDocumentation("提交采购预收单(即SAP借机发货单)。返回编号说明:1.成功,400.参数错误,401.仓库编号不存在,402.SKU编号不存在,403.商品为空,404.商品个体编码为空,405.商品个体编码数量与对应SKU数量不一致,406.仓库已被禁用,500.系统错误,501.数据重复")
	WSResult putPurPreRecv(@WebParam(name = "purPreRecv") PurPreRecvDTO purPreRecv);
}
