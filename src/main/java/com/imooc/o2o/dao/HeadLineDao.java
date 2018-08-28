package com.imooc.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.HeadLine;

public interface HeadLineDao {
	List<HeadLine> queryHeadLineList(@Param("headLineCondition") HeadLine headLineCondition);
}
