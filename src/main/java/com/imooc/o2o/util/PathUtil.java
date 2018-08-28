package com.imooc.o2o.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PathUtil {
	private static SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
	//获取不同操作系统文件的分隔符     Windows(\) Linux(/) Max(/)
	private static String seperator=System.getProperty("file.separator");
	
	/**
	 * 根据不同的操作系统去选择不同的跟目录,
	 * 项目图片的根路径
	 * 
	 * @return basePath
	 */
	public static String getImgBasePath() {
		//获取执行的操作系统
		String os=System.getProperty("os.name");
		String basePath="";
		if(os.toLowerCase().startsWith("win")) {//Windows
			basePath="E:/projectdev/image";
		}else {//Linux Mac
			basePath="/home/work/image";
		}
		//将系统分隔符吧   "/"替换
		basePath=basePath.replace("/", seperator);
		return basePath;
	}
	
	
	
	/**
	 * shop图片的相对值路径
	 * 将图片分别存储在店铺路径之下
	 * 
	 * @param shopId
	 * @return
	 */
	public static String getShopImagePath(long shopId) {
		String imagePath="/upload/item/shop/"+shopId+"/";
		return imagePath.replace("/", seperator);
	}
	
	public static void main(String[] args) {
		System.out.println(PathUtil.getShopImagePath(2));
		System.out.println(simpleDateFormat.format(new Date()));
	}
}
