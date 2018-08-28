package com.imooc.o2o.web.frontend;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.o2o.enums.ProductEnums;
import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.service.ProductCategoryService;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("frontend")
public class ShopDetailController {
	@Autowired
	ShopService shopService;
	
	@Autowired
	ProductCategoryService productCategoryService;
	
	@Autowired
	ProductService productService;
	/**
	 * 店铺详情页里的店铺详情和店铺下的商品类别列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="shopwithproductcategory",method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> shopWithProductCategory(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//获取从前端传递的shopId
		Long shopId=HttpServletRequestUtil.getLong(request, "shopId");
		ProductCategoryExecution pe=null;
		Shop shop = null;
		if(shopId!=-1) {
			shop=shopService.getShopById(shopId);
			pe=productCategoryService.getProductCategoryList(shopId);
			modelMap.put("success", true);
			modelMap.put("shop", shop);
			modelMap.put("productCategoryList", pe.getProductCategoryList());
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "shopId is null");
		}
		return modelMap;
	}
	
	
	/**
	 * 根据查询条件获取店铺列表,条件包括商品类别和商品名称查询
	 * @param request
	 * @return
	 */
	@RequestMapping(value="listproductbyshop",method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> listProductByShop(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//这三个值必须有
		int pageIndex=HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize=HttpServletRequestUtil.getInt(request, "pageSize");
		Long shopId=HttpServletRequestUtil.getLong(request, "shopId");
		if(pageIndex>-1 && pageSize>-1 && shopId>-1) {
			//当点击了某个商品类别后,获取商品类别ID
			Long productCategoryId=HttpServletRequestUtil.getLong(request, "productCategoryId");
			//输入商品名查询商品
			String productName=HttpServletRequestUtil.getString(request, "productName");
			Product productCondition=createProductCondition(shopId,productCategoryId,productName);
			//根据productCondition查询条件获取商品列表
			ProductExecution pe=productService.getProductList(productCondition, pageIndex, pageSize);
			//查询成功
			if(pe.getState()==ProductEnums.SUCCESS.getState()) {
				modelMap.put("success", true);
				modelMap.put("productList", pe.getProductList());
				modelMap.put("count", pe.getCount());
			}else {
				modelMap.put("success", false);
				modelMap.put("errMsg", pe.getStateInfo());
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "pageSize or pageIndex is null");
		}
		return modelMap;
	}


	private Product createProductCondition(Long shopId, Long productCategoryId, String productName) {
		Product productCondition= new Product();
		Shop shop = new Shop();
		shop.setShopId(shopId);
		//给商品指定店铺
		productCondition.setShop(shop);
		if(productName!=null) {
			productCondition.setProductName(productName);
		}
		if(productCategoryId>-1) {
			ProductCategory productCategory=new ProductCategory();
			productCategory.setProductCategoryId(productCategoryId);
			//为商品指定商品类别
			productCondition.setProductCategory(productCategory);
		}
		//查询上架的商品
		productCondition.setEnableStatus(1);
		return productCondition;
	}
}
