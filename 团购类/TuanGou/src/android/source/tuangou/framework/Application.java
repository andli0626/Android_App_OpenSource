package android.source.tuangou.framework;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.source.tuangou.framework.auth.Session;
import android.source.tuangou.framework.base.MenuMediator;
import android.source.tuangou.framework.util.LogUtil;

import java.io.File;

/*
 * 自定义Application类，用于保存全局数据
 * */
public class Application extends android.app.Application{

	private static Application instance;
	private static Session session;
	private MenuMediator globalMenuMediator;

	public Application(){
		
	}

	public static String getAppFilesPath(){
		return instance.getFilesDir().getAbsolutePath();
	}

	public static Application getInstance(){
		return instance;
	}

	//获取Session对象
	public static Session getSession(){
		return session;
	}

	public MenuMediator getGlobalMenuMediator(){
		return globalMenuMediator;
	}

	//获取当期客户端版本
	public int getVersionCode(){
		int i = -1;
		try {
			//获取包管理类
			PackageManager packagemanager = getPackageManager();
			//获取类名称
			String s = getPackageName();
			//获取版本信息
			i = packagemanager.getPackageInfo(s, 0).versionCode;
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return i;
	}

	//创建application的方法
	public void onCreate(){
		super.onCreate();
		instance = this;

		//配置文件初始化
		Config.init();
		//服务管理的初始化
		ServiceManager.init(instance);
		//第一次安装初始化--拷贝Assets文件夹到sd卡中
		FirstInstaller.checkAndCopyAssetsFolders();

		//创建session对象
		session = new Session();
	}

	public void onLowMemory(){
		super.onLowMemory();
	}

	public void onTerminate(){
		super.onTerminate();
	}

	public void setGlobalMenuMediator(MenuMediator menumediator){
		globalMenuMediator = menumediator;
	}
}
