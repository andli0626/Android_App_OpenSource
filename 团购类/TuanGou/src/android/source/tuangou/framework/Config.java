package android.source.tuangou.framework;

import android.content.res.AssetManager;
import android.source.tuangou.framework.util.LogUtil;
import android.source.tuangou.framework.util.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/*
 * 配置文件类
 * */
public class Config{

	/*
	 * 对Config.xml文件的解析类
	 * 
	 * DefaultHandler类是SAX2事件处理程序的默认基类
	 * */
	static class ConfigXmlHandler extends DefaultHandler{

		Boolean inAssetsTag;
		String preTag;

		//获取element元素之间content方法
		public void characters(char ac[], int i, int j)
			throws SAXException{
			String s = preTag;
			//remote相关配置信息获取
			if (!"remote-prefix".equalsIgnoreCase(s)){
				String s3 = preTag;
				
				if ("remote-update-path".equalsIgnoreCase(s3)){
					String s4 = new String(ac, i, j);
					String s5 = Config.REMOTE_UPDATE_PATH;
					Config.REMOTE_UPDATE_PATH = StringUtil.getValueOrDefault(s4, s5);
				} else{
					String s6 = preTag;
					if ("remote-version-url".equalsIgnoreCase(s6)){
						String s7 = new String(ac, i, j);
						String s8 = Config.REMOTE_VERSION_URL;
						Config.REMOTE_VERSION_URL = StringUtil.getValueOrDefault(s7, s8);
					}
				}
				return;
			}else{
				String s1 = new String(ac, i, j);
				String s2 = Config.REMOTE_PREFIX;
				Config.REMOTE_PREFIX = StringUtil.getValueOrDefault(s1, s2);
			}
		}

		//元素解析完成函数
		public void endElement(String s, String s1, String s2)
			throws SAXException{
			if ("assets".equalsIgnoreCase(s1)){
				Boolean boolean1 = Boolean.valueOf(false);
				inAssetsTag = boolean1;
			}
			preTag = null;
		}

		//元素开始解析函数
		public void startElement(String s, String s1, String s2, Attributes attributes)
			throws SAXException{
			if (!"config".equalsIgnoreCase(s1)){
				//assets标签下放置了关于assets文件夹下文件的设置
				if ("assets".equalsIgnoreCase(s1)){
					//Assets文件夹list对象清楚
					Config.ASSETS_FOLDERS_TO_COPY.clear();
					
					Boolean boolean1 = Boolean.valueOf(true);
					//标识是否在Assets配置
					inAssetsTag = boolean1;
				}
				//asset标签关于assets文件夹下文件的具体设置
				else if ("asset".equalsIgnoreCase(s1)){
					//获取相应属性值
					String sCopy = attributes.getValue("copy");
					String sPath = attributes.getValue("path");
					String sConfig = attributes.getValue("config");
					
					boolean flag;
					if ("true".equalsIgnoreCase(sCopy) && !StringUtil.isEmpty(sPath).booleanValue()){
						flag = Config.ASSETS_FOLDERS_TO_COPY.add(sPath);
					}
					
					if ("WEB_FILE_FOLDER".equalsIgnoreCase(sConfig)){
						Config.WEB_FILE_FOLDER = sPath;
					}
					else if ("UPDATE_FILE_FOLDER".equalsIgnoreCase(sConfig)){
						Config.UPDATE_FILE_FOLDER = sPath;
					}
				}
				//数据库的相关配置
				else if ("database".equalsIgnoreCase(s1)){
					String s13 = attributes.getValue("name");
					String s14 = Config.DEFAULT_DATABASE;
					//获取默认数据库的名称
					Config.DEFAULT_DATABASE = StringUtil.getValueOrDefault(s13, s14);
				} 
				//javascript-bridge的配置
				else if ("javascript-bridges".equalsIgnoreCase(s1)){
					Config.JAVASCRIPT_BRIDGES.clear();
				}
				else if ("javascript-bridge".equalsIgnoreCase(s1)){
					String s15 = attributes.getValue("name");
					String s16 = attributes.getValue("class");
					Object obj;
					if (!StringUtil.isEmpty(s15).booleanValue() && !StringUtil.isEmpty(s16).booleanValue())
						obj = Config.JAVASCRIPT_BRIDGES.put(s15, s16);
				}
				preTag = s1;
				return;
			}
			//获取客户端相关信息
			else{
				//Client_tag信息
				String s3 = attributes.getValue("client-tag");
				String s4 = Config.CLIENT_TAG;
				Config.CLIENT_TAG = StringUtil.getValueOrDefault(s3, s4);
				
				//Client_version版本信息
				String s5 = attributes.getValue("client-version");
				String s6 = Config.CLIENT_VERSION;
				Config.CLIENT_VERSION = StringUtil.getValueOrDefault(s5, s6);
				
				//Page_source信息
				String s7 = attributes.getValue("page-source");
				String s8 = Config.PAGE_SOURCE;
				Config.PAGE_SOURCE = StringUtil.getValueOrDefault(s7, s8);
				
				//debug信息
				String s9 = attributes.getValue("debug");
				Config.DEBUG = Boolean.valueOf("true".equalsIgnoreCase(s9));
			}
		 }

		public ConfigXmlHandler(){
			super();
			Boolean boolean1 = Boolean.valueOf(false);
			inAssetsTag = boolean1;
		}

	}


	/*
	 * 相关变量的定义
	 * 
	 * API_ANDROID_PREFIX--android的api地址
	 * API_IPHONE_PREFIX--ipone的api地址
	 * ASSETS_FOLDERS_TO_COPY--Assets文件夹下拷贝的list
	 * CLIENT_TAG--客户端的标识
	 * CLIENT_VERSION--客户端的版本
	 * DEBUG--是否调试标识
	 * DEFAULT_DATABASE-默认数据库
	 * JAVASCRIPT_BRIDGES--javascript的map对象
	 * JS_ADAPTER--jd数据源标识
	 * LOCAL_VERSION_FILE--本地关于版本信息的配置文件
	 * PAGE_SOURCE--页面来源
	 * REMOTE_PREFIX--
	 * REMOTE_UPDATE_PATH--更新的路径
	 * REMOTE_VERSION_URL--获取版本的url
	 * UPDATE_FILE_FOLDER--更新文件夹
	 * WEB_FILE_FOLDER-web文件夹
	 * WEB_FILE_PREFIX--保存web数据源路径，分为网络与本地
	 * 
	 * */
	public static final String API_ANDROID_PREFIX = "http://api.tuan800.com/mobile_api/android/";
	public static final String API_IPHONE_PREFIX = "http://api.tuan800.com/iphone/";
	public static List ASSETS_FOLDERS_TO_COPY = null;
	public static String CLIENT_TAG = "";
	public static String CLIENT_VERSION = "";
	public static Boolean DEBUG = false;
	public static String DEFAULT_DATABASE = "";
	public static Map JAVASCRIPT_BRIDGES = null;
	public static final String JS_ADAPTER = "android";
	public static final String LOCAL_VERSION_FILE = "ver.xml";
	public static String PAGE_SOURCE;
	public static String REMOTE_PREFIX;
	public static String REMOTE_UPDATE_PATH;
	public static String REMOTE_VERSION_URL;
	public static String UPDATE_FILE_FOLDER = "update";
	public static String WEB_FILE_FOLDER = "web";
	public static String WEB_FILE_PREFIX;

	public Config(){
	
	}

	/*
	 * 配置文件的初始化
	 * */
	public static void init(){
		try {
			//获取assets目录下的文件管理类
			AssetManager assetmanager = Application.getInstance().getAssets();
			//打开assets目录下的config.xml文件
			InputStream inputstream = assetmanager.open("config.xml");
			//采用了SAx方式对xml文件进行解析
			SAXParser saxparser = SAXParserFactory.newInstance().newSAXParser();
			//用于解析xml的handler
			ConfigXmlHandler configxmlhandler = new ConfigXmlHandler();
			saxparser.parse(inputstream, configxmlhandler);
			
			String s = PAGE_SOURCE;
			//判断数据源是本地还是网络
			if ("remote".equalsIgnoreCase(s)) {
				StringBuilder stringbuilder = new StringBuilder();
				String s1 = REMOTE_PREFIX;
				StringBuilder stringbuilder1 = stringbuilder.append(s1);
				String s2 = CLIENT_TAG;
				WEB_FILE_PREFIX = stringbuilder1.append(s2).append("/")
						.toString();
			} else {
				StringBuilder stringbuilder2 = (new StringBuilder())
						.append("file://");
				String s3 = Application.getAppFilesPath();
				StringBuilder stringbuilder3 = stringbuilder2.append(s3)
						.append("/");
				String s4 = WEB_FILE_FOLDER;
				WEB_FILE_PREFIX = stringbuilder3.append(s4).append("/")
						.toString();
				
			}
			inputstream.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return;
	}
	
	static {
		/*
		 * 相关变量的初始化
		 * */
		PAGE_SOURCE = "local";
		CLIENT_TAG = "android_web";
		CLIENT_VERSION = "3.0";
		REMOTE_PREFIX = "http://d.tuan800.com/dl/mobile/webapp/";
		StringBuilder stringbuilder2;
		String s3;
		StringBuilder stringbuilder3;
		String s4;
		//判断数据源是本地还是网络
		if ("remote".equalsIgnoreCase(PAGE_SOURCE)){
			StringBuilder stringbuilder = new StringBuilder();
			String s1 = REMOTE_PREFIX;
			StringBuilder stringbuilder1 = stringbuilder.append(s1);
			String s2 = CLIENT_TAG;
			WEB_FILE_PREFIX = stringbuilder1.append(s2).append("/").toString();
		} else{
			//数据源为本地
			WEB_FILE_PREFIX = "file:///data/data/com.tuan800.android/files/web/";
		}
		
		DEBUG = Boolean.valueOf(false);
		stringbuilder2 = new StringBuilder();
		s3 = REMOTE_PREFIX;
		REMOTE_UPDATE_PATH = stringbuilder2.append(s3).append("update/").toString();
		stringbuilder3 = new StringBuilder();
		s4 = REMOTE_UPDATE_PATH;
		REMOTE_VERSION_URL = stringbuilder3.append(s4).append("ver.xml").toString();
		//Assets数据list对象创建
		ASSETS_FOLDERS_TO_COPY = new ArrayList();
		DEFAULT_DATABASE = "tuan800";
		JAVASCRIPT_BRIDGES = new HashMap();
	}

}
