package com.gionee.wms.dao;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.AttrSet;

@BatisDao
public interface AttrSetDao {
	List<AttrSet> queryAttrSetList(Map<String, Object> criteria);

	AttrSet queryAttrSet(Long id);

	int addAttrSet(AttrSet attrSet);

	int updateAttrSet(AttrSet attrSet);

	int deleteAttrSet(Long id);
}
