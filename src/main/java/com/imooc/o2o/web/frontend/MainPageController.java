package com.imooc.o2o.web.frontend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.o2o.entity.HeadLine;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.service.HeadLineService;
import com.imooc.o2o.service.ShopCategoryService;

@Controller
@RequestMapping("frontend")
public class MainPageController {
	@Autowired
	ShopCategoryService shopCategoryService;
	
	@Autowired
	HeadLineService headLineService;
	
	@RequestMapping("listmainpageinfo")
	@ResponseBody
	public Map<String, Object> listMainPageInfo(){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<ShopCategory> shopCategoryList =new ArrayList<ShopCategory>();
		try {
			shopCategoryList=shopCategoryService.getShopCategoryList(null);
			modelMap.put("shopCategoryList", shopCategoryList);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage().toString());
			return modelMap;
		}
		List<HeadLine> headLineList =new ArrayList<HeadLine>();
		HeadLine headLineCondition = new HeadLine();
		//设置查询状态为1的头条
		headLineCondition.setEnableStatus(1);
		try {
			headLineList = headLineService.getHeadLineList(headLineCondition);
			modelMap.put("headLineList", headLineList);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage().toString());
			return modelMap;
		}
		modelMap.put("success", true);
		return modelMap;
	}
}
