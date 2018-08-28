package com.imooc.o2o.dao;

import java.util.List;

import com.imooc.o2o.entity.Area;

public interface AreaDao {
	/**
	 * 查询区域列表
	 * @return areaList
	 */
	List<Area> queryArea();
}
