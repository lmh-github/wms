package com.gionee.wms.web.action.stock;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.dto.CommonAjaxResult;
import com.gionee.wms.entity.Indiv;
import com.gionee.wms.entity.Transfer;
import com.gionee.wms.entity.TransferGoods;
import com.gionee.wms.entity.Warehouse;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.basis.WarehouseService;
import com.gionee.wms.service.stock.TransferService;
import com.gionee.wms.service.wares.IndivService;
import com.gionee.wms.web.action.CrudActionSupport;
import com.google.common.collect.Maps;

@Controller("TransPrepareSfAction")
@Scope("prototype")
public class TransPrepareSfAction extends CrudActionSupport<Transfer> {
	private static final long serialVersionUID = 2728587467025993326L;
	private static final int PART_CODE_MAXLENGTH = 10; // 配件编码最大长度
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private TransferService transferService;
	@Autowired
	private IndivService indivService;
	@Autowired
	private WarehouseService warehouseService;
	
	private Long transferId;
	private String logisticNo;
	private List<TransferGoods> goodsList;
	private String indivCode;
	private List<Indiv> indivList;
	private String goodsId;
	private Integer preparedNum;
	private Transfer transfer;
	private Boolean finished;
	private String logisticName;
	private String tplateNumber;
	private String driver;
	private String joiner;
	
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	public String loadGoods() throws Exception {
		boolean unfinished = false;
		if(null!=transferId) {
			goodsList = transferService.getTransferGoodsForPrep(transferId);
			for (TransferGoods goods : goodsList) {
				if(goods.getPreparedNum()==null || goods.getPreparedNum()<goods.getQuantity()) {
					unfinished = true;
				}
			}
			finished = !unfinished; // 没有未完成为已完成
		}
		return "load_goods";
	}

	/**
	 * 扫描时检查商品个体
	 */
	public String prepareIndiv() throws Exception {
		Validate.notNull(indivCode);
		CommonAjaxResult result = new CommonAjaxResult();
		Map<String, Object> resultMap = new HashMap<String, Object>();// 回传到页面的数据包括sku和对应的imei编码
		Map<String, Object> params = Maps.newHashMap();
		int prepareRst = 0;
		// 获取配货订单仓库信息
		Transfer transfer = transferService.getTransferById(transferId);
		if (!(WmsConstants.TransferStatus.UN_DELIVERYD.getCode()==transfer
				.getStatus() || WmsConstants.TransferStatus.DELIVERYING.getCode()==transfer.getStatus())) {
			// 不为未发货或者配货中
			result.setOk(false);
			result.setMessage("调拨单状态异常，请检查");
			ajaxObject(result);
			return null;
		}
		if (indivCode.length() <= PART_CODE_MAXLENGTH) {// 扫描sku编码
			result=this.dealSku(transfer, indivCode);
		} else {
			Indiv indiv = indivService.getIndivByCode(indivCode);
			if (null == indiv) {// 箱号
				result=this.dealCase(transfer, indivCode);
			} else {// 个体编码
				result=this.dealIndiv(transfer, indivCode, indiv);
			}
		}
		ajaxObject(result);
		return null;
	}

	private CommonAjaxResult dealSku(Transfer transfer, String indivCode) {
		// TODO Auto-generated method stub
		CommonAjaxResult result = new CommonAjaxResult();
		Map<String, Object> resultMap = new HashMap<String, Object>();//
		List<Map> resultList = new ArrayList<Map>();// 回传到页面的数据包括sku和对应的imei编码
		Map<String, Object> params = Maps.newHashMap();
		int prepareRst = 0;
		if (null != goodsList) {
			for (TransferGoods goods : goodsList) {
				if (indivCode.equals(goods.getSkuCode())
						&& WmsConstants.ENABLED_FALSE == goods
								.getIndivEnabled()) {
					Integer preparedNum = null == goods.getPreparedNum() ? 1
							: goods.getPreparedNum() + 1;
					if (preparedNum > goods.getQuantity()) {
						prepareRst = 1; // 已超出
						break;
					}
					goods.setPreparedNum(preparedNum);
					params.clear();
					params.put("goodsId", goods.getId());
					params.put("preparedNum", preparedNum);
					transferService.updateTransferGoods(params);
					prepareRst = 2; // 成功
					break;
				}
			}
		}
		if (0 == prepareRst) {
			result.setOk(false);
			result.setMessage("未找到sku");
		} else if (1 == prepareRst) {
			result.setOk(false);
			result.setMessage("已达到数量");
		} else if (2 == prepareRst) {
			result.setOk(true);
			resultMap.put("skuCode", indivCode);
			resultMap.put("indivCode", null);
			resultList.add(resultMap);
			result.setResult(resultList);
			result.setMessage("成功");
		}
		return result;
	}

	private CommonAjaxResult dealCase(Transfer transfer, String indivCode) {
		// TODO Auto-generated method stub
		CommonAjaxResult result = new CommonAjaxResult();
		Map<String, Object> resultMap = new HashMap<String, Object>();//
		List<Map> resultList = new ArrayList<Map>();// 回传到页面的数据包括sku和对应的imei编码
		int prepareRst = 0;
		// 箱号查询
		List<Indiv> indivList = indivService.getIndivListByCaseCode(indivCode);
		Warehouse warehouse = warehouseService.getWarehouse(transfer
				.getWarehouseId());
		result.setOk(true);
		if (null == indivList) {
			result.setOk(false);
			result.setMessage("未找到商品个体");
			return result;
		}
		for (Indiv indivTemp : indivList) {
			if (!(warehouse.getId().equals(indivTemp.getWarehouseId()))) {
				result.setOk(false);
				result.setMessage("商品" + indivTemp.getIndivCode() + "不属于该仓库");
				return result;
			}
			if (WmsConstants.IndivStockStatus.IN_WAREHOUSE.getCode() != indivTemp
					.getStockStatus()) {
				result.setOk(false);
				result.setMessage("商品" + indivTemp.getIndivCode() + "不在库中");
				return result;
			}
			if (null != transfer.getTransType()
					&& WmsConstants.TRANS_TYPE_DEFECTIVE == transfer
							.getTransType().intValue()) {
				// 次品单
				if (WmsConstants.IndivWaresStatus.NON_DEFECTIVE.getCode() == indivTemp
						.getWaresStatus()) {
					result.setOk(false);
					result.setMessage("次品调拨的物品必须为次品" + indivTemp.getIndivCode()
							+ "非次品");
					return result;
				}
			} else {
				// 良品单
				if (WmsConstants.IndivWaresStatus.DEFECTIVE.getCode() == indivTemp
						.getWaresStatus()) {
					result.setOk(false);
					result.setMessage("良品调拨的物品必须为良品" + indivTemp.getIndivCode()
							+ "非良品");
					return result;
				}
			}
		}
		for (TransferGoods goods : goodsList) {
			int count = 0;
			for (Indiv indiv : indivList) {
				if (indiv.getSkuCode().equals(goods.getSkuCode())
						&& WmsConstants.ENABLED_TRUE == goods.getIndivEnabled()) {
					count = count + 1;
					resultMap.clear();
					resultMap.put("skuCode", indiv.getSkuCode());
					resultMap.put("indivCode", indiv.getIndivCode());
					resultList.add(resultMap);
					if (count + goods.getPreparedNum() > goods.getQuantity()) {
						result.setOk(false);
						result.setMessage("箱号所含sku：" + goods.getSkuCode()
								+ "商品个数超出尚需配货的数量");
						return result;
					}
				}
			}
		}
		transferService.addIndivs(transfer.getTransferId(),transfer.getTransferId() + "", indivList);
		result.setOk(true);
//		resultList.add(resultMap);
		result.setResult(resultList);
		return result;

	}

	private CommonAjaxResult dealIndiv(Transfer transfer, String indivCode, Indiv indiv) {
		// TODO Auto-generated method stub
		CommonAjaxResult result = new CommonAjaxResult();
		Map<String, Object> resultMap = new HashMap<String, Object>();//
		List<Map> resultList = new ArrayList<Map>();// 回传到页面的数据包括sku和对应的imei编码
		Map<String, Object> params = Maps.newHashMap();
		int prepareRst = 0;
		Warehouse warehouse = warehouseService.getWarehouse(transfer
				.getWarehouseId());
		result.setOk(true);
		if (WmsConstants.IndivStockStatus.IN_WAREHOUSE.getCode() != indiv
				.getStockStatus()) {
			result.setOk(false);
			result.setMessage("商品不在库中");
		} else if (!(warehouse.getId().equals(indiv.getWarehouseId()))) {
			result.setOk(false);
			result.setMessage("该商品不属于该仓库");
		} else {
			if (null != transfer.getTransType()
					&& WmsConstants.TRANS_TYPE_DEFECTIVE == transfer
							.getTransType().intValue()) {
				// 次品单
				if (WmsConstants.IndivWaresStatus.NON_DEFECTIVE.getCode() == indiv
						.getWaresStatus()) {
					result.setOk(false);
					result.setMessage("次品调拨的物品必须为次品");
				}
			} else {
				// 良品单
				if (WmsConstants.IndivWaresStatus.DEFECTIVE.getCode() == indiv
						.getWaresStatus()) {
					result.setOk(false);
					result.setMessage("良品调拨的物品必须为良品");
				}
			}
		}
		if (result.getOk()) {
			String skuCode = indiv.getSkuCode();
			if (null != goodsList) {
				for (TransferGoods goods : goodsList) {
					if (skuCode.equals(goods.getSkuCode())
							&& WmsConstants.ENABLED_TRUE == goods
									.getIndivEnabled()) {
						Integer preparedNum = null == goods.getPreparedNum() ? 1
								: goods.getPreparedNum() + 1;
						if (preparedNum > goods.getQuantity()) {
							prepareRst = 1; // 超出
							break;
						}
						transferService.addIndiv(transfer.getTransferId(),
								transfer.getTransferId() + "", indiv.getId());
						prepareRst = 2; // 成功
						break;
					}
				}
			}
			if (0 == prepareRst) {
				result.setOk(false);
				result.setMessage("未找到sku");
			} else if (1 == prepareRst) {
			result.setOk(false);
				result.setMessage("已达到数量");
			} else if (2 == prepareRst) {
				result.setOk(true);
				resultMap.put("skuCode", skuCode);
				resultMap.put("indivCode", indivCode);
				resultList.add(resultMap);
				result.setResult(resultList);
				result.setMessage("成功");
			}
		}
		return result;
	}

	public String readyPrepare() throws Exception {

		Validate.notNull(transferId);

		CommonAjaxResult result=new CommonAjaxResult();


		try {
			Map<String,Object> criteria=new HashMap<String,Object>();
			criteria.put("transferId",transferId);
			criteria.put("transferTo","8610752");
			int t=transferService.getTransferListTotalSf(criteria);
			if(t==0){
				result.setMessage("调拨单不属于分仓管理下的单号");
				ajaxObject(result);
				return null;
			}
			transfer = transferService.getTransferSfById(transferId); // 获得调货单
			//if(type.equals("0")){
			//	result.setMessage("调拨单不属于分仓管理下的单号");
			//}else{
				if(null!=transfer) {
					if(!(WmsConstants.TransferStatus.UN_DELIVERYD.getCode()==transfer.getStatus() || WmsConstants.TransferStatus.DELIVERYING.getCode()==transfer.getStatus())) {
						result.setMessage("未发货的单才能进行配货");
					} else {
						Transfer tr=new Transfer();
						tr.setTransferId(transfer.getTransferId());
						tr.setStatus(WmsConstants.TransferStatus.DELIVERYING.getCode());
						transferService.updateTransferSf(tr);
						result.setOk(true);
						result.setResult(transfer);
					}
				} else {
					result.setMessage("调拨单" + transferId + "不存在");
				}
			//}
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			result.setMessage("获取调拨单失败：" + e.getMessage());
		}
		ajaxObject(result);
		return null;
	}

	/**
	 * 确认调拨
	 * 
	 * @return
	 * @throws Exception
	 */
	public String confirm() throws Exception {
		try {
			Validate.notNull(transferId); // 调拨单号
			Validate.notNull(logisticNo); // 物流单号
			Transfer transfer = transferService.getTransferById(transferId);
			if(WmsConstants.TransferStatus.DELIVERYED.getCode() == transfer.getStatus()) {
				ajaxError("调拨单已发货，请不要重复发货");
			} else if(WmsConstants.TransferStatus.CANCELED.getCode() == transfer.getStatus()) {
				ajaxError("调拨单已取消，无法发货");
			} else {
				List<TransferGoods> goodsList = transferService
						.getTransferGoodsForPrep(transfer.getTransferId()); // 调拨的商品
				if(goodsList==null) {
					ajaxError("调拨商品为空，无法发货");
					return null;	
				}
				for (TransferGoods goods : goodsList) {
					if(goods.getQuantity()!=null && goods.getPreparedNum()!=null && goods.getQuantity().intValue()!=goods.getPreparedNum().intValue()) {
						ajaxError("配货数量不正确，无法提交");
						return null;
					}
				}
				transfer.setHandledBy(ActionUtils.getLoginName());
				transfer.setLogisticNo(logisticNo); // 加入物流单号
				transferService.confirmDeliverySf(transfer, goodsList);
				ajaxSuccess("");
			}
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			ajaxError("配货失败：" + e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ajaxError("系统错误");
		}
		return null;
	}
	
	public String batchPrepare() {
		CommonAjaxResult result=new CommonAjaxResult();
		Map<String,  Object> params = Maps.newHashMap();
		try {
			params.put("preparedNum", preparedNum);
			params.put("goodsId", goodsId);
			transferService.updateTransferGoods(params);
			result.setOk(true);
		} catch(Exception e) {
			result.setOk(false);
			result.setMessage(e.getMessage());
		}
		ajaxObject(result);
		return null;
	}

	@Override
	public Transfer getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String list() throws Exception {
		Validate.notNull(transferId);
		// 获取调拨商品列表
		transfer = transferService.getTransferById(transferId);
		goodsList = transferService.getTransferGoodsForPrep(transferId);
		indivList = transferService.getIndivList(transferId);
		return LIST;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String add() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String update() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void prepareInput() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void prepareUpdate() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void prepareAdd() throws Exception {
		// TODO Auto-generated method stub
		
	}

	public List<TransferGoods> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<TransferGoods> goodsList) {
		this.goodsList = goodsList;
	}

	public String getIndivCode() {
		return indivCode;
	}

	public void setIndivCode(String indivCode) {
		this.indivCode = indivCode;
	}

	public String getLogisticNo() {
		return logisticNo;
	}

	public void setLogisticNo(String logisticNo) {
		this.logisticNo = logisticNo;
	}
	
	public Long getTransferId() {
		return transferId;
	}
	public void setTransferId(Long transferId) {
		this.transferId = transferId;
	}

	public List<Indiv> getIndivList() {
		return indivList;
	}

	public void setIndivList(List<Indiv> indivList) {
		this.indivList = indivList;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getPreparedNum() {
		return preparedNum;
	}

	public void setPreparedNum(Integer preparedNum) {
		this.preparedNum = preparedNum;
	}

	public Transfer getTransfer() {
		return transfer;
	}

	public void setTransfer(Transfer transfer) {
		this.transfer = transfer;
	}

	public Boolean getFinished() {
		return finished;
	}

	public void setFinished(Boolean finished) {
		this.finished = finished;
	}

	public String getLogisticName() {
		return logisticName;
	}

	public void setLogisticName(String logisticName) {
		this.logisticName = logisticName;
	}

	public String getTplateNumber() {
		return tplateNumber;
	}

	public void setTplateNumber(String tplateNumber) {
		this.tplateNumber = tplateNumber;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getJoiner() {
		return joiner;
	}

	public void setJoiner(String joiner) {
		this.joiner = joiner;
	}
	
}
