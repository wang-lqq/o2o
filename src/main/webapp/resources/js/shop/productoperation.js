/**
 * 
 */
// 获取当前店铺设定的商品类别列表的URL
$(function(){
	
	// 从URL里获取shopId参数的值
	var productId = getQueryString('productId');
	//根据productId获取商品信息
	var getProductById='/o2o/shopadmin/getproductbyid?productId='+productId+''
	
	var isEdit = productId?true:false;
	//获取该店铺下的商品类别列表URL
	var categoryUrl = '/o2o/shopadmin/getproductcategorylist';
	//添加商品的URL
	var submitUrl="/o2o/shopadmin/addproduct";
	//修改商品信息URL
	var editShopUrl='/o2o/shopadmin/modifyproduct?productId='+productId+'';
	if(productId){//商品修改
		updataProduct(productId);
	}else{//商品新增
		getProductCategoryList();
	}
	function updataProduct(productId){
		$.getJSON(getProductById,function(data){
			if(data.redirect){
				window.location.href=data.url;
			}else if(data.success){
				var product = data.product;
				var optionHtml='';
				var productcategorylist=data.productcategorylist;
				$("#product-name").val(product.productName);
				$('#product-desc').val(product.productDesc);
				$('#priority').val(product.priority);
				$('#normal-price').val(product.normalPrice);
				$('#promotion-price').val(product.promotionPrice);
				productcategorylist.map(function(item,index){
					optionHtml+='<option value='+item.productCategoryId+'>'+item.productCategoryName+'</option>';
				})
				$("#category").append(optionHtml);
				$("#category").find("option[value='"+product.productCategory.productCategoryId+"']").attr("selected",true);
			}
		})
	}
	//获取某个店铺下的商品类别列表
	function getProductCategoryList(){
		$.getJSON(categoryUrl,function(data){
			//点击新增商品时进入productoperation.html时,当session失效了,则从定向到shoplist.html
			if(data.redirect){
				window.location.href=data.url;
			}else if(data.success){
				var tempHtml="";
				var productCategoryList=data.productCategoryList;
				productCategoryList.map(function(item,index){
					tempHtml+='<option value="'+item.productCategoryId+'">'+item.productCategoryName+'</option>';
				});
				$("#category").html(tempHtml);
			}
			
		})
	}
	// 针对商品详情图控件组，若该控件组的最后一个元素发生变化（即上传了图片），
	// 且控件总数未达到6个，则生成新的一个文件上传控件
	$('.detail-img-div').on('change', '.detail-img:last-child', function() {
		if ($('.detail-img').length < 6) {
			$('#detail-img').append('<input type="file" class="detail-img">');
		}
	});
	$("#submit").click(
		function(){
			product={};
			// 生成表单对象，用于接收参数并传递给后台
			var formData = new FormData();
			// 创建商品json对象，并从表单里面获取对应的属性值
			var product = {};
			product.productName = $('#product-name').val();
			product.productDesc = $('#product-desc').val();
			product.priority = $('#priority').val();
			product.normalPrice = $('#normal-price').val();
			product.promotionPrice = $('#promotion-price').val();
			// 获取选定的商品类别值
			product.productCategory ={
					productCategoryId:$("#category").val()
			}
			//product json对象转化为json字符串
			formData.append("productStr",JSON.stringify(product));
			//获取商品缩略图文件流
			var smallImg = $("#small-img")[0].files[0];
			formData.append("thumbnail",smallImg);
			//获取商品详情图文件流列表
			$(".detail-img").map(function(index,item){
				// 判断该控件是否已选择了文件
				if($(".detail-img")[index].files.length>0){
					formData.append("productImg"+index,$(".detail-img")[index].files[0]);
				}
			})
			//获取验证码输入框
			var verifyCodeActual = $("#j_captcha").val();
			if(!verifyCodeActual){
				$.toast("请输入验证码");
				return;
			}
			formData.append("verifyCodeActual",verifyCodeActual);
			$.ajax({
				type:"POST",
				url:(isEdit?editShopUrl:submitUrl),
				data:formData,
				processData : false,  //必须false才会避开jQuery对 formdata 的默认处理   
		        contentType : false,  //必须false才会自动加上正确的Content-Type 
				cache:false,
				success:function(data){
					//session失效则从定向到shoplist.html
					if(data.redirect){
						window.location.href='/o2o/shopadmin/shoplist';
					}else if(data.success){
						$.toast("提交成功");
						window.location.href='/o2o/shopadmin/productmanagement';
					}else{
						$.toast(data.errMsg);
					}
					$("#captcha_img").click();
				}
			});
		}
		
	)
})
