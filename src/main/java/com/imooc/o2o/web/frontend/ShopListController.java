package com.imooc.o2o.web.frontend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("frontend")
public class ShopListController {
	@Autowired
	ShopCategoryService shopCategoryService;
	
	@Autowired
	AreaService areaService;
	
	@Autowired
	ShopService shopService;
	
	/**
	 * 获取一级店铺类别,获取一级店铺类别下的所有的二级店铺类别和区域信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="listshopspageinfo",method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> listShopsPageInfo(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long shopCategoryId=HttpServletRequestUtil.getLong(request, "parentId");
		List<ShopCategory> shopCategoryList=null;
		//则,点击的是首页的一级店铺类别
		if(shopCategoryId!=-1) {
			try {
				ShopCategory shopCategoryCondition=new ShopCategory();
				ShopCategory parent=new ShopCategory();
				parent.setShopCategoryId(shopCategoryId);
				//设置父店铺类别(一级店铺类别)
				shopCategoryCondition.setParent(parent);
				//获取一级店铺类别下所有的二级店铺类别
				shopCategoryList=shopCategoryService.getShopCategoryList(shopCategoryCondition);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
		}else {//点击的是全部店铺
			try {
				//获取全部的一级店铺类别
				shopCategoryList=shopCategoryService.getShopCategoryList(null);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
		}
		modelMap.put("shopCategoryList", shopCategoryList);
		List<Area> areaList=null;
		try {
			areaList=areaService.getAreaList();
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
			return modelMap;
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
	
	@RequestMapping(value="listshops",method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> listShops(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		int pageIndex=HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize=HttpServletRequestUtil.getInt(request, "pageSize");
		if(pageIndex>-1 && pageSize>-1) {
			//获取一级店铺店铺类别ID
			Long parentId=HttpServletRequestUtil.getLong(request, "parentId");
			//获取二级店铺类别ID
			Long shopCategoryId=HttpServletRequestUtil.getLong(request, "shopCategoryId");
			//获取区域ID
			int areaId=HttpServletRequestUtil.getInt(request, "areaId");
			//获取搜索框输入的店铺名
			String shopName=HttpServletRequestUtil.getString(request, "shopName");
			Shop shopCondition=createShopCondition(parentId,shopCategoryId,areaId,shopName);
			try {
				ShopExecution se=shopService.getShopList(shopCondition, pageIndex, pageSize);
				modelMap.put("shopList", se.getShopList());
				modelMap.put("count", se.getCount());
				modelMap.put("success", true);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		}else {
			modelMap.put("errMsg", "pageSize or pageIndex is null");
		}
		return modelMap;
	}
	/**
	 * 创建shopCondition查询店铺列表的条件
	 * @param parentId
	 * @param shopCategoryId
	 * @param areaId
	 * @param shopName
	 * @return
	 */
	/**
	 * @param parentId
	 * @param shopCategoryId
	 * @param areaId
	 * @param shopName
	 * @return
	 */
	private Shop createShopCondition(Long parentId, Long shopCategoryId, int areaId, String shopName) {
		Shop shopCondition = new Shop();
		//若有区域查询条件
		if(areaId!=-1) {
			Area area = new Area();
			area.setAreaId(areaId);
			shopCondition.setArea(area);
		}
		//若条件是某个一级店铺类别,则查询类别下的所有二级店铺类别下的店铺
		if(parentId!=-1) {
			ShopCategory childCategory=new ShopCategory();
			ShopCategory parentCategory=new ShopCategory();
			parentCategory.setShopCategoryId(parentId);
			childCategory.setParent(parentCategory);
			shopCondition.setShopCategory(childCategory);
		}
		//某个二级店铺类别下的所有店铺
		if(shopCategoryId!=-1) {
			ShopCategory shopCategory=new ShopCategory();
			shopCategory.setShopCategoryId(shopCategoryId);
			shopCondition.setShopCategory(shopCategory);
		}
		if(shopName!=null) {
			shopCondition.setShopName(shopName);
		}
		shopCondition.setEnableStatus(1);
		return shopCondition;
	}
}
