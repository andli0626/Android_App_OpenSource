package com.yizhao.core;

public class Const {

	public static final float APK_Version = 1.0f;//当前APK版本，更新的时候也需更新此处
	
	public static final int IR_LOCAL_VERSION = 1; //当前IR版本，安装包更新的时候也需更新此处，例如ir文件到了ir3,那么这里更改为3
	
	//BLOG
	public static final String APPKEY_SINA = "3928749414";
	
	public static final String APPSECRET_SINA = "f0e864ad601739d1e0cab5bd158c6e65";
	
	public static final String APPKEY_TENCENT = "600a26ce26954624aec604d5d29b198c";
	
	public static final String APPSECRET_TENCENT = "ac3fcef0b498140c3d2576028d664d32";

	public static final  String SINA_REQUEST_TOKEN_URL = "http://api.t.sina.com.cn/oauth/request_token";
	public static final  String SINA_ACCESS_TOKEN_URL = "http://api.t.sina.com.cn/oauth/access_token";
	public static final  String SINA_AUTHRIZE_URL = "http://api.t.sina.com.cn/oauth/authorize";
	

	public static final int BYTE_SIZE_INT = 1024;
	
	//http
	public static final String HTTPHEAD = "http://";
	
	//log tag
	public static final String TAG = "System.out";

	//database name
	public static final String DATABASE_NAME = "yizhaowang.db";
	
	//time format
	public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm";
    
	//table name
	public static final String TABLE_NAME = "t_sc";
	public static final String TABLE_NAME_USER = "t_user";
	
	//sdcard dir
	public static final String SD_DIR = "yizhaowang";
	
	public static final String SD_DIR_TMP = "yizhaowang/tmp";
    
	//dialog
	public static final int DIALOG_YES_NO_MESSAGE = 1;
	public static final int DIALOG_APK_UPDATE = 2;
	public static final int PROGRESSBAR_WAIT = 3;
	public static final int CHOOSE_FX = 4;
	public static final int URL_WAIT = 5;
	public static final int DIALOG_IR_UPDATE = 6;
	
	//page size
	public static final int PAGE_SIZE_INT = 7;
	public static final String PAGE_SIZE = "7";
	
	//APK版本查询
	public static final String APKURL = "http://www.yeezhao.com/mobilefile/update.json";
	
	//IR提示库文件
	public static final String IR_CHECK = "http://www.yeezhao.com/mobile/mircheck.htm";
	
	//IR提示库文件下载地址,从ir1.txt - ir8.txt
	public static final String IR_URL = "http://www.yeezhao.com/mobilefile/irfile/";
	
    //新浪微博发布URL
    public static final String BLOG_SINA_WRITE = "http://api.t.sina.com.cn/statuses/update.json";
    
	//首页数据
	public static final String HOT_URL = "http://www.yeezhao.com/mobilefile/index.json";
	
	//产品详情，参数id=***
	public static final String DETAIL_URL = "http://www.yeezhao.com/mobile/minfo.htm";
	
	//产品搜索URL,参数为：n=encode(中文)&p=当前页&psize=条/页,对于商品分类导航,固定关键字：化妆品.数码.家电.母婴.图书
	public static final String SEARCH_URL = "http://www.yeezhao.com/mobile/msearch.htm";

	//产品评论URL,参数为：id=id值&p=当前页&psize=条/页
	public static final String REVIEWS_URL = "http://www.yeezhao.com/mobile/mreview.htm";
	
	//商家报价查询URL,参数为：id=id值&p=当前页&psize=条/页
	public static final String SHOPS_URL = "http://www.yeezhao.com/mobile/mshop.htm";

	//图片获取URL
	public static final String PIC_URL = "http://www.yeezhao.com/img/";

	//取压缩图片大小
	public static final String PIC_OPTION = "_s_?x?";
	
	public static final String LOADING = "数据读取中,请稍候...";
	
	public static final String SEARCHING = "搜索中,请稍候...";
	
	public static final String SAVHING = "收藏中,请稍候...";

	//http_data_total
	public static final int TOTAL = 5;

	public static final int TIMEOUT_10 = 10000;  
	public static final int TIMEOUT_15 = 15000;  
	
	public final static String[] PRODUCT_NAME = {
		"化妆品",
		"数码",
		"家电",
		"母婴",
		"图书"
		};
	
}
