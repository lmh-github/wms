package com.gionee.wms.service.stock;

import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.*;
import com.gionee.wms.service.ServiceException;
import com.google.common.collect.Maps;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface TransferRmoService {


	List<TransferRemove> getTransferList(Map<String, Object> criteria, Page page);


	int getTransferTotal(Map<String, Object> criteria);

	void addTransfer(TransferRemove transferRemove,  List<TransferRemoveDetail> transferRemoveDetail);


	List<TransferRemoveDetail> getTransferRemoveDetailList(Map<String ,Object> code);

	//获取退货订单
	TransferRemove getTransferRemove(Map<String,Object> criteria);

	//更新transferRmo
	void updateTransferRemove(TransferRemove transferRemove);

	List<Map<String,Object>> getTransferRemoveForCascade(Map<String,Object> code);

}
