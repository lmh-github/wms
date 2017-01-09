package com.gionee.wms.dao;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.Category;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.vo.CategoryVo;

@BatisDao
public interface CategoryDao {
	Category queryCategory(Long id);

	Integer addCategory(Category cat);

	Integer updateCategoryInfo(Category cat);

	Integer updateCategoryPath(Category cat);

	Integer deleteCategory(Long id);

	List<Category> queryCategoryList(Map<String, Object> criteria);
	
	/**
	 * 取分类列表.(包括商品)
	 * 
	 * @return
	 * @throws ServiceException
	 */
	List<CategoryVo> queryCategoryVoList(Map<String, Object> criteria);

}
