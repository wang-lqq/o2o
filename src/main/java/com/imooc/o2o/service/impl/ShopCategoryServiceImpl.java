package com.imooc.o2o.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.cache.JedisUtil.Keys;
import com.imooc.o2o.cache.JedisUtil.Strings;
import com.imooc.o2o.dao.ShopCategoryDao;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.exceptions.ShopCategoryOperationException;
import com.imooc.o2o.service.ShopCategoryService;
@Service
public class ShopCategoryServiceImpl implements ShopCategoryService{
	private static Logger logger=LoggerFactory.getLogger(ShopCategoryServiceImpl.class) ;
	@Autowired
	ShopCategoryDao shopCategoryDao;
	
	@Autowired
	Keys jedisKeys;
	
	@Autowired
	Strings jedisStrings;
	
	@Override
	@Transactional
	public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) {
		//定义redis的key
		String key = SCLISTKEY;
		// 定义接收对象
		List<ShopCategory> shopCategoryList=null;
		// 定义jackson数据转换操作类
		ObjectMapper mapper=new ObjectMapper();
		// 判断key是否存在
		if(shopCategoryCondition==null) {
			// 若查询条件为空，则列出所有首页大类，即parentId为空的店铺类别
			key = key + "_allfirstlevel";
		}else if(shopCategoryCondition!=null && shopCategoryCondition.getParent()!=null
				&& shopCategoryCondition.getParent().getShopCategoryId()!=null) {
			key=key+"_parent"+shopCategoryCondition.getParent().getShopCategoryId();
		}else if(shopCategoryCondition!=null) {
			key=key+"_allsecondlevel";
		}
		if(!jedisKeys.exists(key)) {
			// 若不存在，则从数据库里面取出相应数据
			shopCategoryList=shopCategoryDao.queryShopCategoryList(shopCategoryCondition);
			// 将相关的实体类集合转换成string,存入redis里面对应的key中
			String jsonStr;
			try {
				jsonStr=mapper.writeValueAsString(shopCategoryList);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new ShopCategoryOperationException(e.getMessage());
			}
			jedisStrings.set(key, jsonStr);
		}else {
			// 若存在key，则直接从redis里面取出相应数据
			String jsonStr=jedisStrings.get(key);
			// 指定要将string转换成的集合类型
			JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, ShopCategory.class);
			try {
				// 将相关key对应的value里的的string转换成对象的实体类集合
				shopCategoryList=mapper.readValue(jsonStr,javaType);
			} catch (JsonParseException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new ShopCategoryOperationException(e.getMessage());
			} catch (JsonMappingException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new ShopCategoryOperationException(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new ShopCategoryOperationException(e.getMessage());
			}
		}
		return shopCategoryList;
	}

}
