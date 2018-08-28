package com.imooc.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.ShopCategory;

/**
 * 查询店铺类别列表
 * @author Administrator
 *
 */
public interface ShopCategoryDao {
	List<ShopCategory> queryShopCategoryList(@Param("shopCategoryCondition")ShopCategory shopCategoryCondition);
}
