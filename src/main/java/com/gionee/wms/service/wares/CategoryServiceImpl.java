package com.gionee.wms.service.wares;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.gionee.wms.common.JsonUtils;
import com.gionee.wms.dao.CategoryDao;
import com.gionee.wms.entity.Category;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.vo.CategoryVo;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {
	private CategoryDao categoryDao;
	private static final int CATETORY_LEVEL_MAX = 10;

	@Override
	public Category getCategory(Long id) throws ServiceException {
		return categoryDao.queryCategory(id);
	}

	@Override
	public List<Category> getCategoryList(Map<String, Object> criteria) throws ServiceException  {
		return categoryDao.queryCategoryList(criteria);
	}

	/**
	 * 取分类列表.(包括商品)
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public List<CategoryVo> queryCategoryVoList(Map<String, Object> criteria) throws ServiceException {
		return categoryDao.queryCategoryVoList(criteria);
	}
	
	@Override
	public String getAllCategoryWithJson() throws ServiceException {
		JsonUtils jsonUtils = new JsonUtils();
		return jsonUtils.toJson(getCategoryList(null));
	}

	@Override
	public void addCategory(Category cat) throws ServiceException {
		Category catParent = getCategory(cat.getCatPid());
		//分类深度控制
		if (StringUtils.split(catParent.getCatPath(), ",").length >= CATETORY_LEVEL_MAX+1) {
			throw new ServiceException("分类深度不能越过" + CATETORY_LEVEL_MAX);
		}
		try {
			if (categoryDao.addCategory(cat) == 0) {
				throw new ServiceException("分类名称重复");
			}
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateCategory(Category cat) throws ServiceException {
		try {
			if (categoryDao.updateCategoryInfo(cat) == 0) {
				throw new ServiceException("分类名称重复");
			}

			// 更新包括子孙节点在内的分类节点路径
			categoryDao.updateCategoryPath(cat);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void deleteCategory(Long id) throws ServiceException {
		Validate.notNull(id);
		try {
			if (categoryDao.deleteCategory(id) == 0) {
				throw new ServiceException("分类下面包含子分类或分类已绑定商品，不能删除");
			}

		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Autowired
	public void setCategoryDao(CategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}

}
