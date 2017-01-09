package com.gionee.wms.dao;

import com.gionee.wms.entity.TransferRemove;
import com.gionee.wms.entity.TransferRemoveDetail;

import java.util.List;
import java.util.Map;

@BatisDao
public interface TransferRmoDao {


	List<TransferRemove> getTransferRemoveList(Map<String, Object> criteria);

	int getTransferTotal(Map<String, Object> criteria);

	int addTransfer(TransferRemove transferRemove);

	int addTransferDetail(TransferRemoveDetail transferRemoveDetail);


	List<TransferRemoveDetail> getTransferRemoveDetailList(Map<String ,Object> code);


	TransferRemove getTransferRemove(Map<String,Object> criteria);


	void updateTransferRemove(TransferRemove transferRemove);

	List<Map<String,Object>> getTransferRemoveForCascade(Map<String,Object> code);

}
