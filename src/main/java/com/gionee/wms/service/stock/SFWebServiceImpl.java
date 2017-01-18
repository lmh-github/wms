/**
 * Project Name:wms
 * File Name:SFServiceImpl.java
 * Package Name:com.gionee.wms.service.stock
 * Date:2014年8月21日下午2:28:03
 * Copyright (c) 2014 深圳市金立通信设备有限公司 版权所有 .
 */
package com.gionee.wms.service.stock;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.wms.common.JaxbUtil;
import com.gionee.wms.common.WmsConstants;
import com.sf.integration.warehouse.request.WmsRequest;
import com.sf.integration.warehouse.service.IOutsideToLscmService;

/**
 * 
 * @author PengBin 00001550<br>
 * @date 2014年8月21日 下午2:28:03
 */
@Service
public class SFWebServiceImpl implements SFWebService {

	private final Logger logger = LoggerFactory.getLogger(SFWebServiceImpl.class);

	@Autowired
	private IOutsideToLscmService service;

	/** {@inheritDoc} */
	@Override
	public <E, T extends WmsRequest> E outsideToLscmService(Class<T> requestClass, Class<E> responseClass, T request) {
		try {
			request.setCheckword(WmsConstants.SF_CHECKWORD);

			String requestXML = JaxbUtil.marshToXmlBinding(requestClass, request);
			logger.info("invoke outsideToLscmService requestXML:" + SystemUtils.LINE_SEPARATOR + requestXML);
			String responseXML = service.outsideToLscmService(requestXML);
			logger.info("invoke outsideToLscmService responseXML:" + SystemUtils.LINE_SEPARATOR + responseXML);
			E response = JaxbUtil.unmarshToObjBinding(responseClass, responseXML);
			return response;

		} catch (JAXBException e) {
			e.printStackTrace();
			logger.error("invoke outsideToLscmService error!" + SystemUtils.LINE_SEPARATOR, e);
			return null;
		}

	}

}
