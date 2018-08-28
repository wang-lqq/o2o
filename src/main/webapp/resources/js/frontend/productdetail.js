/**
 * 
 */
$(function(){
	var productId=getQueryString("productId");
	var detailUrl="/o2o/frontend/getproductdetail?productId="+productId;
	getProductDetail();
	//商品信息及商品详情图列表
	function getProductDetail(){
		$.getJSON(detailUrl,function(data){
			if(data.success){
				var product=data.product;
				$("#product-name").text(product.productName);
				$("#product-img").attr('src',product.imgAddr);
				$("#product-time").text(new Date(product.lastEditTime).Format('yyyy-MM-dd'));
				$("#product-desc").text(product.productDesc);
				if(product.normalPrice!=undefined && product.promotionPrice!=undefined){
					$("#price").show();
					$("#normalPrice").html('<del>'+ '￥' +product.normalPrice+'</del>');
					$("#promotionPrice").text('￥' +product.promotionPrice);
				}else if (product.normalPrice != undefined
						&& product.promotionPrice == undefined) {
					// 如果原价不为空而现价为空则只展示原价
					$('#price').show();
					$('#promotionPrice').text('￥' + product.normalPrice);
				} else if (product.normalPrice == undefined
						&& product.promotionPrice != undefined) {
					// 如果现价不为空而原价为空则只展示现价
					$('#promotionPrice').text('￥' + product.promotionPrice);
				}
				var productImgList=product.productImgList;
				var imgHtml='';
				productImgList.map(function(item,index){
					imgHtml+='<div><img src="'+item.imgAddr+'" width="100%"/></div>';
				})
				$("#imgList").html(imgHtml);
			}
		})
	}
	// 点击后打开右侧栏
	$('#me').click(function() {
		$.openPanel('#panel-right-demo');
	});
	//$.init();
})
