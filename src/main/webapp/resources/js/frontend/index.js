/**
 * 
 */
$(function(){
	var url="/o2o/frontend/listmainpageinfo";
	$.getJSON(url,function(data){
		if(data.success){
			//一级大类别列表
			var shopCategoryList=data.shopCategoryList;
			//轮播图列表
			var headLineList=data.headLineList;
			var swipeHtml='';
			
			headLineList.map(function(item,index){
				swipeHtml+='<div class="swiper-slide img-wrap">'
					+'<a href="'+item.lineLink+'" external>'
					+'<img class="banner-img" src="'+item.lineImg+'" alt="'+item.lineName+'">'
					+'</a>'+'</div>';
			})
			
			$(".swiper-wrapper").html(swipeHtml);
			
			//3秒自动轮播
			$(".swiper-container").swiper({
				autoplay:3000,
				autoplayDisableOnInteraction:false
			})
			//一级店铺类别列表
			var categoryHtml='';
			shopCategoryList.map(function(item,index){
				categoryHtml+='<div class="col-50 shop-classify" data-category="'+item.shopCategoryId+'">'
					+'<div class="word">'
					+'<p class="shop-title">'+item.shopCategoryName+'</p>'
					+'<p class="shop-desc">'+item.shopCategoryDesc+'</p>'
					+'</div>'
					+'<div class="shop-classify-img-warp">'
					+'<img class="shop-img" src="'+item.shopCategoryImg+'"/>'
					+'</div>'
					+'</div>';
			})
			//将拼接好的一级店铺类别
			$(".row").html(categoryHtml);
			
			$("#me").click(function(){
				$.openPanel("#panel-right-demo");
			})
			//某个大类下面的店铺列表
			$('.row').on('click','.shop-classify',function(e){
				var categoryId=e.currentTarget.dataset.category;
				var newUrl="/o2o/frontend/shoplist?parentId="+categoryId;
				window.location.href=newUrl;
			})
		}
	})
})