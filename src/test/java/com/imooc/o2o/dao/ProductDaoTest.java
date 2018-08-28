package com.imooc.o2o.dao;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.Shop;

public class ProductDaoTest extends BaseTest{
	@Autowired
	ProductDao productDao;
	
	@Test
	@Ignore
	public void testInsertProduct() {
		fail("Not yet implemented");
	}

	@Test
	public void testQueryProductList() {
		Product productCondition = new Product();
		Shop shop = new Shop();
		shop.setShopId(1l);
		productCondition.setShop(shop);
		List<Product> productList = productDao.queryProductList(productCondition, 0, 100);
		for (Product product : productList) {
			System.out.println(product);
		}
		
	}

	@Test
	public void testQueryProductCount() {
		Product productCondition = new Product();
		Shop shop = new Shop();
		shop.setShopId(1l);
		productCondition.setShop(shop);
		int i = productDao.queryProductCount(productCondition);
		System.out.println(i);
	}
	@Test
	public void testQueryProduct() {
		Product product=productDao.queryProduct(8);
		System.out.println(product);
	}
}
