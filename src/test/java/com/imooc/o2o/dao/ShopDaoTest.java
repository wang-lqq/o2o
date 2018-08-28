package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;

public class ShopDaoTest extends BaseTest{
	@Autowired
	ShopDao shopDao;
	
	@Test
	@Ignore
	public void testInsertShop() {
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
		int isOk = shopDao.insertShop(shop);
		assertEquals(1, isOk);
	}
	
	@Test
	public void testUpdateShop() {
		Shop shop = shopDao.queryShopById(1l);
		System.out.println(shop);
//		Shop [shopId=1, shopName=coco, shopDesc=咖啡可可, shopAddr=襄阳, 
//		phone=null, shopImg=\\upload\\item\\shop\\12\\2018052623060916698.png,
//		priority=100, createTime=null, lastEditTime=Sat May 26 23:05:10 GMT+08:00 2018, enableStatus=1, 
//		advice=null, area=Area [areaId=2, areaName=东苑, priority=null, createTime=null, lastEditTime=null],
//		owner=null, shopCategory=ShopCategory [shopCategoryId=1, shopCategoryName=奶茶, shopCategoryDesc=null,
//		shopCategoryImg=null, priority=null, createTime=null, lastEditTime=null, parent=null]]

	}
	@Test
	public void testQueryShopList() {
		Shop shopCondition = new Shop();
		/*Area area = new Area();
		area.setAreaId(3);
		shopCondition.setArea(area);
		shopCondition.setShopName("co");
		//shopCondition.setEnableStatus(0);
		PersonInfo owner = new PersonInfo();
		owner.setUserId(1l);
		shopCondition.setOwner(owner);*/
		
		ShopCategory childCategory=new ShopCategory();
		//childCategory.setShopCategoryId(2l);
		ShopCategory parentCategory=new ShopCategory();
		parentCategory.setShopCategoryId(12l);
		childCategory.setParent(parentCategory);
		shopCondition.setShopCategory(childCategory);
		
		
		List<Shop> shopList = shopDao.queryShopList(shopCondition, 0, 100);
		for (Shop shop2 : shopList) {
			System.out.println(shop2);
		}
	}
	@Test
	public void testQueryShopCount() {
		Shop shopCondition = new Shop();
		PersonInfo owner = new PersonInfo();
		owner.setUserId(1l);
		shopCondition.setOwner(owner);
		int i= shopDao.queryShopCount(shopCondition);
		System.out.println(i);
	}
}
