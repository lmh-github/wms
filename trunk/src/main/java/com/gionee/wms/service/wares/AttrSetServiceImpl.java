package com.gionee.wms.service.wares;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.gionee.wms.dao.AttrSetDao;
import com.gionee.wms.entity.AttrSet;
import com.gionee.wms.service.ServiceException;

@Service("attrSetService")
public class AttrSetServiceImpl implements AttrSetService {
	private AttrSetDao attrSetDao;

	@Override
	public void addAttrSet(AttrSet attrSet) throws ServiceException {
		try {
			if (attrSetDao.addAttrSet(attrSet) == 0) {
				throw new ServiceException("商品类型名不能重复");
			}
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}

	}

	@Override
	public void deleteAttrSet(Long id) throws ServiceException {
		Validate.notNull(id);
		try {
			if (attrSetDao.deleteAttrSet(id) == 0) {
				throw new ServiceException("商品类型下面已经绑定属性，不能删除");
			}

		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}

	}

	@Override
	public AttrSet getAttrSet(Long id) {
		return attrSetDao.queryAttrSet(id);
	}

	@Override
	public List<AttrSet> getAttrSetList(Map<String, Object> criteria) {
		return attrSetDao.queryAttrSetList(criteria);
	}

	@Override
	public void updateAttrSet(AttrSet attrSet) throws ServiceException {
		try {
			if (attrSetDao.updateAttrSet(attrSet) == 0) {
				throw new ServiceException("商品类型名不能重复");
			}
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}

	}

	@Autowired
	public void setAttrSetDao(AttrSetDao attrSetDao) {
		this.attrSetDao = attrSetDao;
	}

}
