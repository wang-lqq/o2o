package com.imooc.o2o.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.ProductCategory;

public class ProductCategoryDaoTest extends BaseTest {
	@Autowired
	ProductCategoryDao productCategoryDao;
	
	@Test
	public void testQueryProductCategoryList() {
		List<ProductCategory> list = productCategoryDao.queryProductCategoryList(1);
		for (ProductCategory productCategory : list) {
			System.out.println(productCategory);
		}
	}
	
	@Test
	public void testBatchInsertProductCategory() {
		ProductCategory productCategory1=new ProductCategory();
		productCategory1.setShopId(1L);
		productCategory1.setProductCategoryName("测试店铺1");
		productCategory1.setPriority(23);
		productCategory1.setCreateTime(new Date());
		
		ProductCategory productCategory2=new ProductCategory();
		productCategory2.setShopId(1L);
		productCategory2.setProductCategoryName("测试店铺2");
		productCategory2.setPriority(22);
		productCategory2.setCreateTime(new Date());
		List<ProductCategory> list = new ArrayList<ProductCategory>();
		list.add(productCategory2);
		list.add(productCategory1);
		int insertCount = productCategoryDao.batchInsertProductCategory(list);
		System.out.println(insertCount);
	}
}
