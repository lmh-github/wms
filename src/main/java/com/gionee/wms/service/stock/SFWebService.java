/**
 * Project Name:wms
 * File Name:SFService.java
 * Package Name:com.gionee.wms.service.stock
 * Date:2014年8月21日下午2:24:25
 * Copyright (c) 2014 深圳市金立通信设备有限公司 版权所有 .
 */
package com.gionee.wms.service.stock;

import com.sf.integration.warehouse.request.WmsRequest;

/**
 * 顺丰WebService接口
 * @author PengBin 00001550<br>
 * @date 2014年8月21日 下午2:24:25
 */
public interface SFWebService {

	/**
	 * 调用顺丰接口
	 * @param request
	 * @return
	 */
	<E, T extends WmsRequest> E outsideToLscmService(Class<T> requestClass, Class<E> responseClass, T request);

}
