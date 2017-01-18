package com.gionee.wms.dao;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.Back;
import com.gionee.wms.entity.BackGoods;
import com.gionee.wms.vo.BackGoodsVo;

@BatisDao
public interface BackDao {

	void addBack(Back back);

	void addBackGoods(List<BackGoods> goodsList);

	void updateBack(Back back);

	int getBackTotal(Map<String, Object> criteria);

	List<Back> getBackList(Map<String, Object> criteria);

	/**
	 * 级联查询单据
	 * @param criteria
	 * @return
	 */
	List<Back> getBackListForCascade(Map<String, Object> criteria);

	List<BackGoodsVo> getBackGoodsList(Map<String, Object> criteria);

	Back getBack(Map<String, Object> criteria);

	void updateBackGoods(Map<String, Object> params);

	/**
	 * 根据退货单号删除
	 * @param backCode
	 */
	void deleteBackGoods(String backCode);

	/**
	 * 删除
	 * @param backCode
	 */
	void delete(String backCode);

}
