package com.imooc.o2o.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.ShopDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PageCalculator;
import com.imooc.o2o.util.PathUtil;
@Service
public class ShopServiceImpl implements ShopService {
	@Autowired
	ShopDao shopDao;
	
	@Override
	@Transactional
	public ShopExecution addShop(Shop shop, ImageHolder thumbnail) {
		//店铺空值判断
		if(shop==null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}
		try {
			//店铺信息初始化
			shop.setCreateTime(new Date());
			shop.setLastEditTime(new Date());
			//添加店铺信息
			int effectedNum = shopDao.insertShop(shop);
			if(effectedNum<=0) {
				throw new ShopOperationException("店铺新增失败");
			}else {
				//传入的文件流不等于null
				if(thumbnail.getImage()!=null) {
					//存储图片---文件操作
					try {
						addShopImg(shop,thumbnail);
					} catch (Exception e) {
						e.printStackTrace();
						throw new ShopOperationException("创建图片失败:"+e.getMessage());
					}
					int effectenNum = shopDao.updateShop(shop);
					if(effectenNum<=0) {
						throw new ShopOperationException("更新shop图片地址失败");
					}
				}
			}
		} catch (Exception e) {
			throw new ShopOperationException("addShop error"+e.getMessage());
		}
		return new ShopExecution(ShopStateEnum.CHECK, shop);
	}
	
	/**
	 * 创建新生成的shop图片,并给shop设置图片相对路径
	 * 
	 * @param shop
	 * @param shopImg
	 */
	private void addShopImg(Shop shop, ImageHolder thumbnail) {
		//获得图片存储的相对路径 /upload/item/shop/
		String dest = PathUtil.getShopImagePath(shop.getShopId());
		String shopImgAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		//向shop set图片相对路径
		shop.setShopImg(shopImgAddr);
	}

	@Override
	public Shop getShopById(long shopId) {
		return shopDao.queryShopById(shopId);
	}
	
	@Override
	public ShopExecution modifyShop(Shop shop,ImageHolder thumbnail) throws ShopOperationException{
		if(shop==null || shop.getShopId()==null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}else {
			try {
				//判断是否需要处理图片
				if(thumbnail.getImage()!=null && thumbnail.getImageName()!=null && !thumbnail.getImageName().equals("")
						&& thumbnail.getImageName()!=null) {
					//删除原来图片
					Shop tempShop = getShopById(shop.getShopId());
					if(tempShop.getShopImg()!=null) {
						ImageUtil.deleteFileOrPath(tempShop.getShopImg());
					}
					//存储用户上传地图片,并给shop设置店铺图片的相对路径shop.setShopImg()
					addShopImg(shop, thumbnail);
				}
				//更新店铺操作
				shop.setLastEditTime(new Date());
				int effectedNum = shopDao.updateShop(shop);
				if(effectedNum>0) {
					shop = shopDao.queryShopById(shop.getShopId());
					return new ShopExecution(ShopStateEnum.SUCCESS,shop);
				}else {
					return new ShopExecution(ShopStateEnum.INNER_ERROR);
				}
			} catch (Exception e) {
				throw new ShopOperationException("modifyShop error"+e.getMessage());
			}
		}
	}

	@Override
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
		//将页码转化为行数
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
		//获得同等查询条件下所有店铺数量
		int count = shopDao.queryShopCount(shopCondition);
		ShopExecution se = new ShopExecution();
		if(shopList!=null) {
			se.setShopList(shopList);
			se.setCount(count);
		}else {
			se.setState(ShopStateEnum.INNER_ERROR.getState());
		}
		return se;
	}
}
