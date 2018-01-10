package com.gionee.wms.web.action.stock;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gionee.wms.common.constant.Consts;
import com.gionee.wms.service.stock.UpdDestJsonService;
import com.gionee.wms.vo.UpdDestJsonRequestVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.common.WmsConstants.BackStatus;
import com.gionee.wms.common.WmsConstants.StockBizType;
import com.gionee.wms.common.WmsConstants.StockType;
import com.gionee.wms.dto.Page;
import com.gionee.wms.dto.StockRequest;
import com.gionee.wms.entity.Back;
import com.gionee.wms.entity.BackGoods;
import com.gionee.wms.entity.Indiv;
import com.gionee.wms.entity.IndivFlow;
import com.gionee.wms.entity.IndivScanItem;
import com.gionee.wms.entity.SalesOrder;
import com.gionee.wms.entity.SalesOrderGoods;
import com.gionee.wms.entity.Transfer;
import com.gionee.wms.entity.Warehouse;
import com.gionee.wms.service.basis.WarehouseService;
import com.gionee.wms.service.stock.BackService;
import com.gionee.wms.service.stock.SalesOrderService;
import com.gionee.wms.service.stock.StockService;
import com.gionee.wms.service.wares.IndivService;
import com.gionee.wms.vo.BackGoodsVo;
import com.gionee.wms.web.action.CrudActionSupport;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Controller("BackAction")
@Scope("prototype")
public class BackAction extends CrudActionSupport<Transfer> {

	private static final long serialVersionUID = -8940733721506429911L;
	private static Logger logger = LoggerFactory.getLogger(BackAction.class);

	@Autowired
	private WarehouseService warehouseService;
	@Autowired
	private BackService backService;
	@Autowired
	private StockService stockService;
	@Autowired
	private SalesOrderService salesOrderService;
	@Autowired
	private IndivService indivService;
    @Autowired
    private UpdDestJsonService updDestJsonService;

	/** 页面相关属性 **/
	private List<Back> backList;
	private Long backId;
	private Integer selectEnabled;// 是否允许页面select可用
	private Boolean editEnabled;// 是否允许页面数据编辑
	private Back back;
	private List<Warehouse> warehouseList;// 仓库列表
	private Page page = new Page();

	private BackGoods goods;
	private List<BackGoodsVo> goodsList;
	private Long goodsId;
	private Long warehouseId;
	
	private String backCode;
	private String orderCode;
	private Integer backStatus;
	private String shippingNo;
	private Date backTimeBegin;
	private Date backTimeEnd;
	
	private String[] goodIds;
	private Integer[] nonDefective;	// 良品
	private Integer[] defective;	// 次品
	private String[] skuCode;
	private Integer[] indivEnabled;
	private Integer[] quantity;
	private String[] goodsSids;
	private List<IndivFlow> indivList1;//良品个体信息
	private List<IndivFlow> indivList2;//次品个体信息
	
	private List<IndivScanItem> itemList;

	public String execute() throws Exception {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("backCode", backCode);
		criteria.put("orderCode", orderCode);
		criteria.put("backStatus", backStatus);
		criteria.put("shippingNo", shippingNo);
		criteria.put("backTimeBegin", backTimeBegin);
		criteria.put("backTimeEnd", backTimeEnd);
		int totalRow = backService.getBackTotal(criteria);
		page.setTotalRow(totalRow);
		page.calculate();
		criteria.put("page", page);
		backList = backService.getBackList(criteria, page);
		return SUCCESS;
	}
	
	/**
	 * 进入处理退货界面
	 */
	public String inputInit() throws Exception {
		// 初始化页面数据
		Validate.notNull(backId);
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("id", backId);
		back = backService.getBack(criteria);
		warehouseList = warehouseService.getValidWarehouses();
		warehouseId = back.getWarehouseCode()==null?0:warehouseService.getWarehouseByCode(back.getWarehouseCode()).getId();
		//查询订单下商品清单
		criteria.clear();
		criteria.put("backCode", back.getBackCode());
		goodsList = backService.getBackGoodsList(criteria);
		editEnabled = true;
		if(back.getBackStatus() == BackStatus.BACKED.getCode())
			editEnabled = false;
		
		//查询发货imei
		itemList = Lists.newArrayList();
		criteria.clear();
		criteria.put("orderCode", back.getOrderCode());
		List<Indiv> indivList = indivService.getIndivList(criteria);
		for(Indiv indiv : indivList){
			IndivScanItem item = new IndivScanItem();
			item.setIndivCode(indiv.getIndivCode());
			item.setSkuCode(indiv.getSkuCode());
			item.setSkuName(indiv.getSkuName());
			item.setNum(1);
			itemList.add(item);
		}
		//查询发货的配件
		List<SalesOrderGoods> orderGoods = salesOrderService.getOrderGoodsListByOrderCode(back.getOrderCode());
		for(SalesOrderGoods goods : orderGoods){
			if(goods.getIndivEnabled().equals(0)){
				IndivScanItem item = new IndivScanItem();
				item.setSkuCode(goods.getSkuCode());
				item.setSkuName(goods.getSkuName());
				item.setNum(goods.getQuantity());
				itemList.add(item);
			}
		}
		return "input_init";
	}
	
	/**
	 * 处理已退货信息
	 */
	public String handledBack() throws Exception {
		logger.info("BackAction--handledBack--start");
		try {
			Validate.notNull(back);
			Validate.notNull(warehouseId);
			if(goodIds == null || goodIds.length == 0){
				ajaxError("请选择退货商品");
				return null;
			}
			Map<String, Object> criteria = Maps.newHashMap();
			Map<String, String> goodsToId = Maps.newHashMap();	// 记录商品sku与goodsSid的关系
			criteria.put("backCode", back.getBackCode());
			goodsList = backService.getBackGoodsList(criteria);
			for (BackGoodsVo backGoodsVo : goodsList) {
				if(null!=backGoodsVo.getGoodsSid())
					goodsToId.put(backGoodsVo.getSkuCode(), backGoodsVo.getGoodsSid());
			}
			List<StockRequest> stockList = Lists.newArrayList();
			List<String> goodIdList = Lists.newArrayList();
			int nonDefectiveNum = 0;// 良品个体数量
			int defectiveNum = 0;// 次品个体数量
			for (int i = 0; i < goodIds.length; i++) {
				String goodIdTemp = goodIds[i];
				goodIdList.add(goodIdTemp.split("_")[0]);
				int index = Integer.valueOf(goodIdTemp.split("_")[1]);
				int indivFlag = indivEnabled[index];
				if (indivFlag == 1) {
					nonDefectiveNum += nonDefective[index];
					defectiveNum += defective[index];
				}
				if (quantity[index] != (nonDefective[index] + defective[index])) {
					ajaxError("请核对良次品数量信息");
					return null;
				}
				if (nonDefective[index] > 0) {
					// 变更良品库存
					StockRequest stockRequest = new StockRequest(warehouseId,
							skuCode[index], StockType.STOCK_SALES,
							nonDefective[index], StockBizType.IN_RMA,
							back.getOrderCode());
					stockRequest.setGoodsSid(goodsToId.get(skuCode[index]));
					stockList.add(stockRequest);
				}
				if (defective[index] > 0) {
					// 变更次品库存
					StockRequest stockRequest = new StockRequest(warehouseId,
							skuCode[index], StockType.STOCK_UNSALES,
							defective[index], StockBizType.IN_RMA,
							back.getOrderCode());
					stockRequest.setGoodsSid(goodsToId.get(skuCode[index]));
					stockList.add(stockRequest);
				}
			}

			Map<String, Object> params = Maps.newHashMap();
			params.put("orderCode", back.getOrderCode());
			List<Indiv> list = indivService.getIndivList(params);
			HashSet<String> hasIndivs = new HashSet<String>();
			for (Indiv indiv : list) {
				hasIndivs.add(indiv.getIndivCode());
			}
			// 过滤商品个体
			int indivNum1 = 0;// 良品数量
			int indivNum2 = 0;// 次品数量
			if (CollectionUtils.isNotEmpty(indivList1)) {
				Iterator<IndivFlow> itr1 = indivList1.iterator();
				while (itr1.hasNext()) {
					IndivFlow indiv = itr1.next();
					if(!hasIndivs.contains(indiv.getIndivCode())) {
						ajaxError("个体编码跟原单不符");
						return null;
					}
					if (indiv == null
							|| StringUtils.isBlank(indiv.getIndivCode())) {
						itr1.remove();
					} else {
						indiv.setIndivCode(indiv.getIndivCode().trim());
						indivNum1 += 1;
					}
				}
				if (indivNum1 != nonDefectiveNum) {
					ajaxError("请核对良品数量与个体编码信息");
					return null;
				}
			}

			if (CollectionUtils.isNotEmpty(indivList2)) {
				Iterator<IndivFlow> itr2 = indivList2.iterator();
				while (itr2.hasNext()) {
					IndivFlow indiv = itr2.next();
					if(!hasIndivs.contains(indiv.getIndivCode())) {
						ajaxError("个体编码跟原单不符");
						return null;
					}
					if (indiv == null
							|| StringUtils.isBlank(indiv.getIndivCode())) {
						itr2.remove();
					} else {
						indiv.setIndivCode(indiv.getIndivCode().trim());
						indivNum2 += 1;
					}
				}
				if (indivNum2 != defectiveNum) {
					ajaxError("请核对次品数量与个体编码信息");
					return null;
				}
			}

			if (CollectionUtils.isEmpty(indivList1) && nonDefectiveNum > 0) {
				ajaxError("请添加良品商品个体信息");
				return null;
			}
			if (CollectionUtils.isEmpty(indivList2) && defectiveNum > 0) {
				ajaxError("请添加次品商品个体信息");
				return null;
			}

			backService.handleBacked(warehouseId, back, goodIds, nonDefective,
					defective, indivList1, indivList2, skuCode, indivEnabled);
			// 库存变更处理
			for (StockRequest stockRequest : stockList) {
				stockService.increaseStock(stockRequest, true);
			}

			// 向订单系统推送退货信息
			params.clear();
			params.put("goodIds", goodIdList);
			SalesOrder order = salesOrderService.getSalesOrderByCode(back.getOrderCode());
			salesOrderService.notifyOrder(Lists.newArrayList(order), params);
			logger.info("BackAction--handledBack--end");
			ajaxSuccess("处理退货成功");

            // 发送IMEI退货信息到第三方
            UpdDestJsonRequestVo vo = new UpdDestJsonRequestVo();
            for (Indiv indiv : list) {
                vo.put(indiv.getIndivCode(), Consts.BACK_DEST_NAME_DEFAULT);
            }
            updDestJsonService.sendIMEI(vo);

		} catch (Exception e) {
			logger.error("处理退货时出错", e);
			ajaxError("处理退货单失败：" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 查询调拨单列表
	 */
	@Override
	public String list() throws Exception {
		return null;
	}

	@Override
	public Transfer getModel() {
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

	public Page getPage() {
		if (page == null) {
			page = new Page();
		}
		return page;
	}

	public List<Back> getBackList() {
		return backList;
	}

	public void setBackList(List<Back> backList) {
		this.backList = backList;
	}

	public Long getBackId() {
		return backId;
	}

	public void setBackId(Long backId) {
		this.backId = backId;
	}

	public Integer getSelectEnabled() {
		return selectEnabled;
	}

	public void setSelectEnabled(Integer selectEnabled) {
		this.selectEnabled = selectEnabled;
	}

	public Boolean getEditEnabled() {
		return editEnabled;
	}

	public void setEditEnabled(Boolean editEnabled) {
		this.editEnabled = editEnabled;
	}

	public Back getBack() {
		return back;
	}

	public void setBack(Back back) {
		this.back = back;
	}

	public List<Warehouse> getWarehouseList() {
		return warehouseList;
	}

	public void setWarehouseList(List<Warehouse> warehouseList) {
		this.warehouseList = warehouseList;
	}

	public BackGoods getGoods() {
		return goods;
	}

	public void setGoods(BackGoods goods) {
		this.goods = goods;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getBackCode() {
		return backCode;
	}

	public void setBackCode(String backCode) {
		this.backCode = backCode;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Integer getBackStatus() {
		return backStatus;
	}

	public void setBackStatus(Integer backStatus) {
		this.backStatus = backStatus;
	}

	public Date getBackTimeBegin() {
		return backTimeBegin;
	}

	public void setBackTimeBegin(Date backTimeBegin) {
		this.backTimeBegin = backTimeBegin;
	}

	public Date getBackTimeEnd() {
		return backTimeEnd;
	}

	public void setBackTimeEnd(Date backTimeEnd) {
		this.backTimeEnd = backTimeEnd;
	}

	public String getShippingNo() {
		return shippingNo;
	}

	public void setShippingNo(String shippingNo) {
		this.shippingNo = shippingNo;
	}

	public List<BackGoodsVo> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<BackGoodsVo> goodsList) {
		this.goodsList = goodsList;
	}

	public List<IndivFlow> getIndivList1() {
		return indivList1;
	}

	public void setIndivList1(List<IndivFlow> indivList1) {
		this.indivList1 = indivList1;
	}

	public List<IndivFlow> getIndivList2() {
		return indivList2;
	}

	public void setIndivList2(List<IndivFlow> indivList2) {
		this.indivList2 = indivList2;
	}

	public String[] getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String[] skuCode) {
		this.skuCode = skuCode;
	}

	public Integer[] getNonDefective() {
		return nonDefective;
	}

	public void setNonDefective(Integer[] nonDefective) {
		this.nonDefective = nonDefective;
	}

	public Integer[] getDefective() {
		return defective;
	}

	public void setDefective(Integer[] defective) {
		this.defective = defective;
	}

	public Integer[] getIndivEnabled() {
		return indivEnabled;
	}

	public void setIndivEnabled(Integer[] indivEnabled) {
		this.indivEnabled = indivEnabled;
	}

	public Integer[] getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer[] quantity) {
		this.quantity = quantity;
	}

	public String[] getGoodIds() {
		return goodIds;
	}

	public void setGoodIds(String[] goodIds) {
		this.goodIds = goodIds;
	}

	public List<IndivScanItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<IndivScanItem> itemList) {
		this.itemList = itemList;
	}

	public String[] getGoodsSids() {
		return goodsSids;
	}

	public void setGoodsSids(String[] goodsSids) {
		this.goodsSids = goodsSids;
	}

}
