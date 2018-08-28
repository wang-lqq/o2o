/**
 * 
 */
$(function(){
	// 从URL里获取shopId参数的值
	var shopId = getQueryString('shopId');
	var listUrl = "/o2o/shopadmin/getproductlistbyshop?pageIndex=1&pageSize=100";
	//修改商品状态
	var modifyStatus="/o2o/shopadmin/modifyproduct";
	
	getProductList();
	function getProductList(){
		$.getJSON(listUrl,function(data){
			if(data.redirect){
				window.location.href=data.url;
			}else if(data.success){
				var tempHtml='';
				var productList=data.productList;
				productList.map(function(item,index){
					var textOP='下架';
					var contraryStatus = 0;
					//商品属于下架
					if(item.enableStatus==0){
						//则显示上架
						textOP="上架";
						contraryStatus=1;
					}else{//商品属于上架
						//显示下架
						textOP="下架";
						contraryStatus=0;
					}
					tempHtml+='<div class="row row-product">'
					+'<div class="col-33">'+item.productName+'</div>'
					+'<div class="col-20">'+item.priority+'</div>'
					+'<div class="col-40">'
					+'<a data-id='+item.productId+' data_status='+item.enableStatus+' class="edit">编辑</a>&nbsp;&nbsp;'
					+'<a data-id='+item.productId+' data-status='+contraryStatus+' class="status">'+textOP+'</a>&nbsp;&nbsp;'
					+'<a data-id='+item.productId+' data_status='+item.enableStatus+' class="preview">预览</a>&nbsp;&nbsp;'
					+'</div>'
					+'</div>';
				})
				$(".product-wrap").empty();
				$(".product-wrap").append(tempHtml);
		    }
					
		})
	}
	// 将class为product-wrap里面的所有a标签绑定上点击的事件
	$(".product-wrap").on('click','a',function(e){
		//获取选中的a标签
		var target=$(e.currentTarget);
		//点击编辑
		if(target.hasClass("edit")){
			window.location.href='/o2o/shopadmin/productoperation?productId='+e.currentTarget.dataset.id+'';
		}else if(target.hasClass("status")){//上架或下架
			changeProductStatus(e.currentTarget.dataset.id,e.currentTarget.dataset.status);
		}else if(target.hasClass("preview")){//预览
			
		}
	})
	function changeProductStatus(productId,status){
		var product={};
		product.productId=productId;
		product.enableStatus=status;
		$.confirm('确定么?', function() {
			// 上下架相关商品
			$.ajax({
				url : modifyStatus,
				type : 'POST',
				data : {
					productStr : JSON.stringify(product),
					statusChange : true
				},
				dataType : 'json',
				success : function(data) {
					if (data.success) {
						$.toast('操作成功！');
						getProductList();
					} else {
						$.toast('操作失败！');
					}
				}
			});
		});
	}
})