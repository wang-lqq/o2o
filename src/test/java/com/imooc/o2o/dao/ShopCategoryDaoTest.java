package com.imooc.o2o.dao;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.ShopCategory;

public class ShopCategoryDaoTest extends BaseTest{
	@Autowired
	ShopCategoryDao shopCategoryDao;
	
	@Test
	public void testQueryShopCategoryList() {
		ShopCategory shopCategory=new ShopCategory();
		List<ShopCategory> list = shopCategoryDao.queryShopCategoryList(null);
		for (ShopCategory sCategory : list) {
			System.out.println(sCategory);
		}
	}

}
