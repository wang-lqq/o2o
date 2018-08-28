package com.imooc.o2o.web.frontend;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("frontend")
public class ProductDetailController {
	@Autowired
	ProductService productService;
	
	@RequestMapping(value="getproductdetail",method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getProductDetail(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//获取productId,并查询product详细信息
		Long productId=HttpServletRequestUtil.getLong(request, "productId");
		if(productId!=-1) {
			ProductExecution pe=productService.getProductById(productId);
			modelMap.put("success", true);
			modelMap.put("product", pe.getProduct());
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "productId is empty");
		}
		return modelMap;
	}
}
