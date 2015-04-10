package com.qf.teach.project.zhihudaily.c;

public class API {
	static {
		System.loadLibrary("api");
	}
	
	/**
	 * 主题
	 * @return
	 */
	public static native String getThemesUrl();
	
	/**
	 * 欢迎图片
	 * @return
	 */
	public static native String getStartImageUrl();
	
	/**
	 * 今日热闻
	 * @return
	 */
	public static native String getLatestUrl();
	
	/**
	 * 历史新闻<br />
	 * 返回的字符串需要使用String.format();方法格式化<br />
	 * 例如：String.format(API.getBefore(), "20141201");
	 * @return
	 */
	public static native String getBefore();
	
	/**
	 * 主题列表<br />
	 * 返回的字符串需要使用String.format();方法格式化<br />
	 * 例如：String.format(API.getTheme(), "11");
	 * @return
	 */
	public static native String getTheme();
	
	/**
	 * 详情
	 * 返回的字符串需要使用String.format();方法格式化<br />
	 * 例如：String.format(API.getStory(), "4354228");
	 * @return
	 */
	public static native String getStory();
	
	/**
	 * 详情-评论
	 * 返回的字符串需要使用String.format();方法格式化<br />
	 * 例如：String.format(API.getStoryExtra(), "4354228");
	 * @return
	 */
	public static native String getStoryExtra();
}
