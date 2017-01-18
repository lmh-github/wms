package com.gionee.wms.service.stock;

import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.dao.BackDao;
import com.gionee.wms.dao.TransferRmoDao;
import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.Back;
import com.gionee.wms.entity.BackGoods;
import com.gionee.wms.entity.TransferRemove;
import com.gionee.wms.entity.TransferRemoveDetail;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.common.CommonService;
import com.gionee.wms.service.common.CommonServiceImpl;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service("transferRmoService")
class TransferRmoServiceImpl extends CommonServiceImpl implements TransferRmoService {


	@Autowired
	private TransferRmoDao transferRmoDao;
	@Override
	public List<TransferRemove> getTransferList(Map<String, Object> criteria, Page page) {
		if (criteria == null) {
			criteria = Maps.newHashMap();
		}
		criteria.put("page", page);
		try {
			return transferRmoDao.getTransferRemoveList(criteria);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public int getTransferTotal(Map<String, Object> criteria) {
		return transferRmoDao.getTransferTotal(criteria);
	}

	@Override
	public void addTransfer(TransferRemove transferRemove,  List<TransferRemoveDetail> transferRemoveDetail) {
		//checkBacking(back);
		String backCode = getBizCode(CommonService.ORDER_BACK);
		transferRemove.setTransferCode(backCode); // 生成单号
		transferRemove.setStatus(1);
		 transferRmoDao.addTransfer(transferRemove); // 新增退货单
		 System.out.println(transferRemove.getId());
		for (TransferRemoveDetail s : transferRemoveDetail) {
			s.setId((long) 1);
			s.setTransferId(backCode);

			s.setRemark("");
			transferRmoDao.addTransferDetail(s);
		}

	}

	@Override
	public List<TransferRemoveDetail> getTransferRemoveDetailList(Map<String, Object> code) {

		return transferRmoDao.getTransferRemoveDetailList(code);
	}

	@Override
	public TransferRemove getTransferRemove(Map<String, Object> criteria) {
		if(criteria==null){
			criteria = Maps.newHashMap();
		}
		try {
			return transferRmoDao.getTransferRemove(criteria);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateTransferRemove(TransferRemove transferRemove) {
		if(transferRemove==null){
			transferRemove=new TransferRemove();
		}
		try {
			transferRmoDao.updateTransferRemove(transferRemove);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Map<String, Object>> getTransferRemoveForCascade(Map<String, Object> code) {

		try {
			return	transferRmoDao.getTransferRemoveForCascade(code);
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

}
