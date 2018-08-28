package com.imooc.o2o.web.shopadmin; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductCategoryStateEnum;
import com.imooc.o2o.exceptions.ProductCategoryOperationException;
import com.imooc.o2o.service.ProductCategoryService;

/**
 * 店铺管理下的店铺商品类别管理
 * @author Administrator
 *
 */
@Controller
@RequestMapping("shopadmin")
public class ProductCategoryManagementController {
	@Autowired
	ProductCategoryService productCategoryService;
	
	/**
	 * 商品类别列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="getproductcategorylist")
	@ResponseBody
	public Map<String, Object> getProductcategoryList(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<String,Object>();
		//查询某个店铺下的所有商品类别
		Object object=request.getSession().getAttribute("currentShop");
		if(object==null) {
			modelMap.put("redirect", true);
			modelMap.put("url", "/o2o/shopadmin/shoplist");
			return modelMap;
		}else {
			Shop currentShop =(Shop)object;
			long shopId = currentShop.getShopId();
			//productCategoryList可为null
			ProductCategoryExecution pe = productCategoryService.getProductCategoryList(shopId);
			modelMap.put("success", true);
			modelMap.put("productCategoryList", pe.getProductCategoryList());
			return modelMap;
		}
		
	}
	
	/**
	 * 批量添加商品类别
	 * @param productCategoryList
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "addproductcategorys")
	@ResponseBody
	public Map<String, Object> addProductCategorys(@RequestBody List<ProductCategory> productCategoryList,
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
		for (ProductCategory productCategory : productCategoryList) {
			productCategory.setShopId(currentShop.getShopId());
		}
		if(productCategoryList!=null && productCategoryList.size()>0) {
			try {
				ProductCategoryExecution pc =productCategoryService.batchAddProductCategory(productCategoryList);
				if(pc.getState()==ProductCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pc.getState());
				}
			} catch (ProductCategoryOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg",e.getMessage());
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", ProductCategoryStateEnum.EMPTY_LIST);
		}
		return modelMap;
	}
	
	
	/**
	 * 删除商品类别
	 * @param productCategoryId
	 * @param productCategoryList
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "deleteproductcategorys",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteProductCategorys(Long productCategoryId,
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
		if(productCategoryId!=null && productCategoryId>0) {
			try {
				ProductCategoryExecution pc =productCategoryService.deleteProductCategory(productCategoryId, currentShop.getShopId());
				if(pc.getState()==ProductCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pc.getState());
				}
			} catch (ProductCategoryOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg",e.getMessage());
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请选择删除商品类别");
		}
		return modelMap;
	}
}
