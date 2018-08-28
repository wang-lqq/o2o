package com.imooc.o2o.util;

public class PageCalculator {
	/**
	 * 将pageIndex页数转化为行数
	 * @param pageIndex
	 * @param pageSize
	 * @return rowIndex
	 */
	public static int calculateRowIndex(int pageIndex,int pageSize) {
		return pageIndex>0?(pageIndex-1)*pageSize:0;
	}
}
