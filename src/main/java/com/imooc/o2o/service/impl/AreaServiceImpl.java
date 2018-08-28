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
import com.imooc.o2o.dao.AreaDao;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.exceptions.AreaOperationException;
import com.imooc.o2o.service.AreaService;
@Service
public class AreaServiceImpl implements AreaService{
	@Autowired
	AreaDao areaDao;
	@Autowired
	Keys jedisKeys;
	
	@Autowired
	Strings jedisStrings;
	private static Logger logger=LoggerFactory.getLogger(AreaServiceImpl.class) ;
	@Transactional
	public List<Area> getAreaList() {
		//定义redis的key
		String key = AREALISTKEY;
		// 定义接收对象
		List<Area> areaList=null;
		// 定义jackson数据转换操作类
		ObjectMapper mapper=new ObjectMapper();
		// 判断key是否存在
		if(!jedisKeys.exists(key)) {
			// 若不存在，则从数据库里面取出相应数据
			areaList=areaDao.queryArea();
			// 将相关的实体类集合转换成string,存入redis里面对应的key中
			String jsonStr;
			try {
				jsonStr=mapper.writeValueAsString(areaList);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new AreaOperationException(e.getMessage());
			}
			jedisStrings.set(key, jsonStr);
		}else {
			// 若存在key，则直接从redis里面取出相应数据
			String jsonStr=jedisStrings.get(key);
			// 指定要将string转换成的集合类型
			JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, Area.class);
			try {
				// 将相关key对应的value里的的string转换成对象的实体类集合
				areaList=mapper.readValue(jsonStr,javaType);
			} catch (JsonParseException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new AreaOperationException(e.getMessage());
			} catch (JsonMappingException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new AreaOperationException(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new AreaOperationException(e.getMessage());
			}
		}
		return areaList;
	}
}