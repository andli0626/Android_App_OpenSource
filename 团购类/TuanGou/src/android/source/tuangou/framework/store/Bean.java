package android.source.tuangou.framework.store;

import android.source.tuangou.framework.Config;
import android.source.tuangou.framework.ServiceManager;

public abstract class Bean{

	protected Database db;

	//构造函数
	public Bean(){
		
		//创建数据库管理对象
		DatabaseManager databasemanager = ServiceManager.getDatabaseManager();
		//获取数据库名称
		String s = getDatabaseName();
		//打开数据库
		db = databasemanager.openDatabase(s);
		//数据库中创建表
		createTable();
	}

	public abstract void createTable();

	public Database getDatabase(){
		return db;
	}

	//获取数据库的名称
	public String getDatabaseName(){
		return Config.DEFAULT_DATABASE;
	}
}
