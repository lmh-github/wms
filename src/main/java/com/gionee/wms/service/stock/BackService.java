package com.gionee.wms.service.stock;

import java.util.List;
import java.util.Map;

import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.Back;
import com.gionee.wms.entity.BackGoods;
import com.gionee.wms.entity.IndivFlow;
import com.gionee.wms.facade.request.OperateOrderRequest;
import com.gionee.wms.vo.BackGoodsVo;

public interface BackService {

	void addBack(Back back);

	void addBackGoods(List<BackGoods> goodsList);

	void cancelBack(OperateOrderRequest req);

	int getBackTotal(Map<String, Object> criteria);

	List<Back> getBackList(Map<String, Object> criteria, Page page);

	/**
	 * 级联查询单据
	 * @param criteria
	 * @return
	 */
	List<Back> getBackListForCascade(Map<String, Object> criteria);

	Back getBack(Map<String, Object> criteria);

	List<BackGoodsVo> getBackGoodsList(Map<String, Object> criteria);

	/**
	 * 处理退货
	 * @param warehouseId
	 * @param back
	 * @param goodIds
	 * @param nonDefective 良品数量
	 * @param defective 次品数量
	 * @param indivList1 良品个体
	 * @param indivList2 次品个体
	 */
	void handleBacked(Long warehouseId, Back back, String[] goodIds, Integer[] nonDefective, Integer[] defective, List<IndivFlow> indivList1, List<IndivFlow> indivList2, String[] skuCode, Integer[] indivEnabled);

	/**
	 * 处理删除单据
	 * @param backCode
	 */
	void handleDelete(String backCode);

	/**
	 * 处理新建退换货单
	 * @param back
	 * @param backGoods
	 */
	void handleAddBack(Back back, List<BackGoods> backGoods);

	/**
	 * 收到货之后确认退换货
	 * @param back
	 * @param backGoods
	 * @param indivFlowList
	 */
	void handleConfirmBack(Back back, List<BackGoods> backGoods, List<IndivFlow> indivFlowList);

	/**
	 * 确认
	 * @param back
	 */
	void handleConfirmBack(Back back);

	/**
	 * 修改
	 * @param back
	 */
	void handleUpdate(Back back);

}
