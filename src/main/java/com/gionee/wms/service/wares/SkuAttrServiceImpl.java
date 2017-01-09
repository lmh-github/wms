package com.gionee.wms.service.wares;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.gionee.wms.common.JsonUtils;
import com.gionee.wms.dao.SkuAttrDao;
import com.gionee.wms.entity.SkuAttr;
import com.gionee.wms.entity.SkuItem;
import com.gionee.wms.service.ServiceException;
import com.google.common.collect.Maps;

@Service("skuAttrService")
public class SkuAttrServiceImpl implements SkuAttrService {
	private SkuAttrDao attrDao;

	@Override
	public List<SkuAttr> getAttrList(Map<String, Object> criteria) {
		return attrDao.queryAttrList(criteria);
	}

	@Override
	public SkuAttr getAttr(Long attrId) {
		Validate.notNull(attrId);
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("id", attrId);
		List<SkuAttr> list = getAttrList(criteria);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		} else if (list.size() == 1) {
			return list.get(0);
		} else {
			throw new RuntimeException("数据异常");
		}
	}

	@Override
	public void addAttr(SkuAttr skuAttr) throws ServiceException {
		try {
			// 保存属性
			if (attrDao.addAttr(skuAttr) == 0) {
				throw new ServiceException("同一商品类型下的属性名不能重复");
			}
			if (skuAttr.getId() != null) {
				List<SkuItem> items = skuAttr.getItemList();
				if (CollectionUtils.isNotEmpty(items)) {
					// 保存属性可选项
					for (SkuItem item : items) {
						item.setAttrId(skuAttr.getId());
					}
					attrDao.addItem(items);
				}
			} else {
				throw new ServiceException("添加属性失败");
			}
		} catch (DataAccessException e) {
			throw new ServiceException("添加属性及可选项时出错", e);
		}

	}

	@Override
	public void deleteAttr(Long attrId) throws ServiceException {
		try {
			if (attrDao.deleteAttr(attrId) == 0) {
				throw new ServiceException("此属性已经被商品SKU所绑定，不能删除");
			}
			attrDao.deleteItemByAttrId(attrId);

		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateAttr(SkuAttr skuAttr) throws ServiceException {
		try {
			if (attrDao.updateAttr(skuAttr) == 0) {
				throw new ServiceException("同一商品类型下的属性名不能重复");
			}
			List<SkuItem> invalidItems = new ArrayList<SkuItem>();// 失效可选项(用户在界面上删除或修改)
			List<SkuItem> requestItems = skuAttr.getItemList();// 请求可选项(用户在界面上新增)
			List<SkuItem> oldItems = getAttr(skuAttr.getId()).getItemList();// 原有可选项
			// 分别过虑出待删除和待增加的可选项
			filterItems(oldItems, requestItems, invalidItems);
			if (CollectionUtils.isNotEmpty(invalidItems)) {
				// 删除属性旧可选项
				List<Long> itemIdList = new ArrayList<Long>();
				for (SkuItem item : invalidItems) {
					itemIdList.add(item.getId());
				}
				attrDao.deleteItem(itemIdList);
			}
			if (CollectionUtils.isNotEmpty(requestItems)) {
				// 添加属性新可选项
				for (SkuItem item : requestItems) {
					item.setAttrId(skuAttr.getId());
				}
				attrDao.addItem(requestItems);
			}
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("更新SKU属性时出现未知异常", e);
		}
	}

	/**
	 * 分别过虑出失效和新增的可选项
	 * 
	 * @param oldItems
	 *            原有可选项
	 * @param requestItems
	 *            请求可选项
	 * @param invalidItems
	 *            失效可选项
	 */
	private void filterItems(List<SkuItem> oldItems, List<SkuItem> requestItems, List<SkuItem> invalidItems) {
		Iterator<SkuItem> itr = oldItems.iterator();
		while (itr.hasNext()) {
			SkuItem item = itr.next();
			int i = 0;
			Iterator<SkuItem> itr2 = requestItems.iterator();
			while (itr2.hasNext()) {
				SkuItem item2 = itr2.next();
				if (item2.getItemName().equalsIgnoreCase(item.getItemName())) {
					i++;
					itr2.remove();
				}
			}
			if (i == 0) {
				invalidItems.add(item);
				itr.remove();
			}
		}
	}

	@Autowired
	public void setSkuAttrDao(SkuAttrDao skuAttrDao) {
		this.attrDao = skuAttrDao;
	}

	public static void main(String[] args) {
		SkuAttrServiceImpl service = new SkuAttrServiceImpl();
		List<SkuItem> oldItems = new ArrayList<SkuItem>();
		SkuItem item = new SkuItem();
		item.setId(1l);
		item.setItemName("一");
		oldItems.add(item);

		item = new SkuItem();
		item.setId(2l);
		item.setItemName("二");
		oldItems.add(item);

		item = new SkuItem();
		item.setId(3l);
		item.setItemName("三");
		oldItems.add(item);

		List<SkuItem> requestItems = new ArrayList<SkuItem>();
		item = new SkuItem();
		item.setId(2l);
		item.setItemName("二");
		requestItems.add(item);

		item = new SkuItem();
		item.setId(4l);
		item.setItemName("四");
		requestItems.add(item);

		item = new SkuItem();
		item.setId(5l);
		item.setItemName("五");
		requestItems.add(item);

		// 失效对象集合
		List<SkuItem> invalidItems = new ArrayList<SkuItem>();
		service.filterItems(oldItems, requestItems, invalidItems);
		JsonUtils jsonUtils = new JsonUtils();
		System.out.println("失效对象集合:" + jsonUtils.toJson(invalidItems));
		System.out.println("新增对象集合:" + jsonUtils.toJson(requestItems));
		List a = new ArrayList();
		a.add("1");
		a.add("2");
		a.add("3");
		a.add("3");
		a.add("4");
		a.add("5");
		List b = new ArrayList();
		b.add("3");
		b.add("4");
		b.add("4");
		b.add("5");
		b.add("6");
		b.add("7");
		System.out.println(CollectionUtils.subtract(a, b));

	}

}
