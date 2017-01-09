package com.gionee.wms.service.wares;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.Category;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.vo.CategoryVo;

public interface CategoryService {
	/**
	 * 取分类.
	 */
	Category getCategory(Long id) throws ServiceException;

	/**
	 * 添加分类.
	 */
	void addCategory(Category cat) throws ServiceException;

	/**
	 * 更新分类.
	 */
	void updateCategory(Category cat) throws ServiceException;

	/**
	 * 删除分类.
	 * 
	 * @param id
	 * @throws ServiceException
	 */
	void deleteCategory(Long id) throws ServiceException;

	/**
	 * 取分类列表.
	 * 
	 * @return
	 * @throws ServiceException
	 */
	List<Category> getCategoryList(Map<String, Object> criteria) throws ServiceException ;
	
	/**
	 * 取分类列表.(包括商品)
	 * 
	 * @return
	 * @throws ServiceException
	 */
	List<CategoryVo> queryCategoryVoList(Map<String, Object> criteria) throws ServiceException ;

	/**
	 * 取全部分类，且以json格式返回.
	 * 
	 * @return
	 * @throws ServiceException
	 */
	String getAllCategoryWithJson() throws ServiceException;
}
