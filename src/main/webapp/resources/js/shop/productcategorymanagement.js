/**
 * 
 */
$(function(){
	var deleteUrl="/o2o/shopadmin/deleteproductcategorys";
	var addUrl="/o2o/shopadmin/addproductcategorys";
	//获取店铺下的商品类比列表
	var listUrl="/o2o/shopadmin/getproductcategorylist";
	getList();
	function getList(){
		$.getJSON(listUrl,function(data){
			if(data.success){
				var tempHtml='';
				var ProductCategoryList=data.productCategoryList;
				ProductCategoryList.map(function(item,index){
					tempHtml+='<div class="row row-product-category now">'+
					'<div class="col-33 product-category-name">'+item.productCategoryName+'</div>'+
					'<div class="col-33">'+item.priority+'</div>'+
					'<div class="col-33">'+'<a data-id='+item.productCategoryId+' class="button delete">删除</a>'+'</div>'+
					'</div>';
				})
				$("ul").empty();
				$(".category-wrap").html(tempHtml);
			}
		})
	}
	$("#new").click(function(){
		var tempHtml=
			'<div class="row row-product-category temp">'+
				'<div class="col-33"><input type="text" class="category-input category" placeholder="分类名"/></div>'+
				'<div class="col-33"><input type="number" class="category-input priority" placeholder="优先级"/></div>'+
				'<div class="col-33"><a class="button delete" id="d">删除</a></div>'+
			'</div>';
		$(".category-wrap").append(tempHtml);
	})
	$("#submit").click(function(){
		var tempArr = $(".temp");
		var productCategoryList=[];
		tempArr.map(function(index,item){
			var tempObj={}
			tempObj.productCategoryName=$(item).find(".category").val();
			tempObj.priority=$(item).find(".priority").val();
			if(tempObj.productCategoryName && tempObj.priority){
				productCategoryList.push(tempObj);
			}
		});
		$.ajax({
			url : addUrl,
			type : 'POST',
			data : JSON.stringify(productCategoryList),
			contentType : 'application/json',
			success:function(data){
				if(data.success){
					$.toast('提交成功！');
					getList();
				}else{
					$.toast('提交失败！');
				}
			}
		})
	})
	$('.category-wrap').on('click', '.row-product-category.temp .delete',
			function(e) {
				console.log($(this).parent().parent());
				//parent()返回被选元素的所有祖先元素
				$(this).parent().parent().remove();
	});
	
	$('.category-wrap').on('click', '.row-product-category.now .delete',
			function(e) {
				console.log(e.currentTarget.dataset.id);
				console.log($(this).parent().parent());
				//parent()返回被选元素的所有祖先元素
				//$(this).parent().parent().remove();
				$.confirm("确定删除吗?",function(){
					$.ajax({
						url : deleteUrl,
						type : 'POST',
						data : {"productCategoryId":e.currentTarget.dataset.id},
						dataType:'json',
						success:function(data){
							if(data.success){
								$.toast('删除成功！');
								getList();
							}else{
								$.toast('删除失败！');
							}
						}
					})
				})
	});
})