package com.imooc.o2o.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imooc.o2o.cache.JedisUtil.Keys;
import com.imooc.o2o.service.CacheService;
@Service
public class CacheServiceImpl implements CacheService {
	@Autowired
	Keys keys;
	
	@Override
	public void removeFromCache(String keyPrefix) {
		Set<String> keySet=keys.keys(keyPrefix+"*");
		for (String key : keySet) {
			keys.del(key);
		}
	}

}
