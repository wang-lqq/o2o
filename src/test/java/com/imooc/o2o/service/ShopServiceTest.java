package com.imooc.o2o.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.dao.ShopDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;

public class ShopServiceTest extends BaseTest{
	@Autowired
	ShopService shopService;
	
	@Autowired
	ShopDao shopDao;
	@Test
	public void testAddShop() {
		Shop shop = new Shop();
		//店铺所属用户
		PersonInfo personInfo = new PersonInfo();
		personInfo.setUserId(1l);
		//店铺类别
		ShopCategory shopCategory= new ShopCategory();
		shopCategory.setShopCategoryId(1l);
		//店铺区域
		Area area = new Area();
		area.setAreaId(2);
		shop.setArea(area);
		shop.setShopCategory(shopCategory);
		shop.setOwner(personInfo);
		shop.setShopName("coco");
		shop.setShopDesc("咖啡可可");
		shop.setShopAddr("襄阳");
		shop.setPriority(100);
		shop.setEnableStatus(1);
		File shopImg=new File("E:\\2345截图\\dd.png");
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(shopImg);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ImageHolder thumbnail = new ImageHolder(inputStream, shopImg.getName());
		shopService.addShop(shop, thumbnail);
		//assertEquals(1, isOk);
	}

}
