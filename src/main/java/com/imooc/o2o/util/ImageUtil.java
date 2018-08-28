package com.imooc.o2o.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.imooc.o2o.dto.ImageHolder;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

public class ImageUtil {
	private static SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
	
	private static String basePath ="/home/work/image";
	private static final Random r = new Random();
	private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);
	
	/**
	 * 将CommonsMultipartFile转换成File类
	 * 
	 * @param cFile
	 * @return
	 */
	public static File transferCommonsMultipartFileToFile(CommonsMultipartFile cFile) {
		File newFile = new File(cFile.getOriginalFilename());
		try {
			cFile.transferTo(newFile);
		} catch (IllegalStateException e) {
			logger.error(e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		return newFile;
	}

	/**
	 * 存储前端上传的shop图片,并返回该图片的相对值路径
	 * 
	 * @param shopImgInputStream
	 * @param targetAddr
	 * @return
	 */
	public static String generateThumbnail(ImageHolder thumbnail,String targetAddr) {
		String realFileName=getRandomFileName();
		String extension=getFileExtension(thumbnail.getImageName());
		/*创建图片存储的绝对路径Windows下:E:\\projectdev\\image\\upload\\item\\shop\\+shopId+\\ */
		makeDirPath(targetAddr);
		String relativeAddr=targetAddr+realFileName+extension;
		logger.debug("current relativeAddr is:"+relativeAddr);
		//根路径+相对路径=图片存储绝对路径
		File dest=new File(PathUtil.getImgBasePath()+relativeAddr);
		logger.debug("current complete and is:"+PathUtil.getImgBasePath()+relativeAddr);
		try {
			Thumbnails.of(thumbnail.getImage()).size(200, 200)
			.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.25f)
			.outputQuality(0.8f).toFile(dest);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return relativeAddr;
	}
	
	/**
	 * 存储图片并返回图片存储的相对路径
	 * @param imageHolder
	 * @param targetAddr
	 * @return
	 */
	public static String generateNormalThumbnail(ImageHolder imageHolder,String targetAddr) {
		String realFileName=getRandomFileName();
		String extension=getFileExtension(imageHolder.getImageName());
		/*创建图片存储的绝对路径Windows下:E:\\projectdev\\image\\upload\\item\\shop\\+shopId+\\ */
		makeDirPath(targetAddr);
		String relativeAddr=targetAddr+realFileName+extension;
		logger.debug("current relativeAddr is:"+relativeAddr);
		//根路径+相对路径=图片存储绝对路径
		File dest=new File(PathUtil.getImgBasePath()+relativeAddr);
		logger.debug("current complete and is:"+PathUtil.getImgBasePath()+relativeAddr);
		try {
			Thumbnails.of(imageHolder.getImage()).size(337, 640)
			.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.25f)
			.outputQuality(0.9f).toFile(dest);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return relativeAddr;
	}
	
	/**
	 * 创建目标路径所涉及的目录
	 * 
	 * @param targetAddr
	 */
	private static void makeDirPath(String targetAddr) {
		//shop图片存储的绝对路径
		String realFileParentPath=PathUtil.getImgBasePath()+targetAddr;
		File dirPath =new File(realFileParentPath);
		if(!dirPath.exists()) {
			dirPath.mkdirs();
		}
	}
	
	/**
	 * 生成随机文件名，当前年月日小时分钟秒钟+五位随机数
	 * 
	 * @return
	 */
	private static String getRandomFileName() {
		int rannum = r.nextInt(90000)+10000;
		String nowTimeStr = simpleDateFormat.format(new Date());
		return nowTimeStr+rannum;
		
	}

	/**
	 * 获取输入文件流的扩展名
	 * @param thumbnail
	 * @return
	 */
	private static String getFileExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}
	/**
	 * 删除目录或者文件
	 * @param storePath
	 * @return 
	 */
	public static void deleteFileOrPath(String storePath) {
		File fileOrPath = new File(PathUtil.getImgBasePath()+storePath);
		if(fileOrPath.exists()) {
			//若是目录删除目录下所有文件
			if(fileOrPath.isDirectory()) {
				File[] files = fileOrPath.listFiles();
				for (File file2 : files) {
					file2.delete();
				}
			}
			fileOrPath.delete();
		}
	}
	
	public static void main(String[] args) throws IOException {
		//basePath:    /E:/eclipse%e6%96%87%e4%bb%b6/o2o/target/classes/
		//basePath:    /E:/eclipse文件/o2o/target/classes/
		//String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		Thumbnails.of(new File("E:\\2345截图\\dd.png")).size(200, 200)
				.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "\\watermark.jpg")), 0.25f)
				.outputQuality(0.8f).toFile("E:\\2345截图\\ddnew.png");
	}
	
}
