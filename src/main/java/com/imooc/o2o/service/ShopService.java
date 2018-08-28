package com.imooc.o2o.service;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.exceptions.ShopOperationException;

public interface ShopService {
	/**
	 * 注册店铺信息,包括图片处理
	 * @param shop
	 * @param shopImgInputStream
	 * @param fileName
	 * @return
	 */
	ShopExecution addShop(Shop shop,ImageHolder thumbnail);
	
	/**
	 * 店铺信息
	 * @param shopId
	 * @return
	 */
	Shop getShopById(long shopId);
	
	/**
	 * 更新店铺
	 * 
	 * 1.若传入图片,则先删除原来图片再新生成
	 * 2.更新shop
	 * 
	 * @param shop
	 * @return
	 */
	ShopExecution modifyShop(Shop shop,ImageHolder thumbnail) throws ShopOperationException;
	
	/**
	 * 根据shopCondition分页返回相应店铺列表
	 * @param shopCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	ShopExecution getShopList(Shop shopCondition,int pageIndex,int pageSize);
}
