package com.yizhao.core;

import android.content.ContentValues;
import android.content.Context;   
import android.database.Cursor;   
import android.database.sqlite.SQLiteDatabase;   
import android.util.Log;   

/**  
 * 数据库操作类
 */  
public class DBHelper {
    
    SQLiteDatabase db;   
    Context context;//应用环境上下文   Activity 是其子类   
  
    public DBHelper(Context _context) {
    	
    	context = _context;
    	
    	try{
        	if(db!=null){
        		close();
        	}
        	
        	db = context.openOrCreateDatabase(Const.DATABASE_NAME, Context.MODE_PRIVATE,null);//开启数据库 
        	
        	createTable();
        	
        	Log.d(Const.TAG, "DBHelper|db path=" + db == null ? "db is null!" : db.getPath());  
    	}catch (Exception e) {
    		Log.e(Const.TAG, "DBHelper instance|error="+e);
    	}   
    }   
  
    /**  
     * 建表  
     * 列名 区分大小写？  
     * 都有什么数据类型？  
     * SQLite 3   
     *  TEXT    文本  
     *  VARCHAR 字符串
        NUMERIC 数值  
        INTEGER 整型  
        REAL    小数  
        NONE    无类型  
     * 查询可否发送select ?  
     */  
    private void createTable(){
        try {
        	
            db.execSQL("CREATE TABLE IF NOT EXISTS "+Const.TABLE_NAME+" (" +   
                    "_id INTEGER PRIMARY KEY autoincrement,"
                    + "pid VARCHAR,"
                    + "name VARCHAR,"
                    + "price INTEGER,"
                    + "shops INTEGER,"
                    + "reviews INTEGER,"
                    + "picurl TEXT"
                    + ");");

            Log.d(Const.TAG, "DBHelper.initTable| "+Const.TABLE_NAME+" ok");   
            
            db.execSQL("CREATE TABLE IF NOT EXISTS "+Const.TABLE_NAME_USER+"("+
                    "_id INTEGER primary key,"+
                    "type VARCHAR,"+
                    "utoken VARCHAR,"+
                    "tokensecret VARCHAR"+
                    ");");
            
            Log.d(Const.TAG, "DBHelper.initTable"+Const.TABLE_NAME_USER+" ok");

        } catch (Exception e) {
            Log.e(Const.TAG, "DBHelper.initTable|error.");   
            e.printStackTrace();
        }   
    }   
    
    
//    /**  
//     * 增加/更新数据到参数表
//     * @param id  
//     * @param uname  
//     * @return  
//     */  
//    public boolean updateIRVersion(String name,int newVersion){
//    	
//      
//        
//        try{
//        	
//        	Cursor cur=db.query(Const.TABLE_PARAM, new String[]{"name","value"}, "name=?",new String[]{name}, null, null,null);
//        	
//        	if(cur!=null){
//	    		
//        		if(!cur.isAfterLast()){//有记录
//	    			
//	    			cur.moveToFirst();
//	    			
//	    			String v = cur.getString(1);
//	    			
//	    			Log.d(Const.TAG, "DBHelper.updateParam|find it! name="+name+",value="+v);
//	    			 
//	    			if(v!=null){
//	    				
//	    				int localVersion = Integer.parseInt(v);
//	    				
//	    				if(newVersion > localVersion){//判断版本，如果新版本大于本地版本，则更新
//	    					
//	    					 ContentValues values = new ContentValues();
//	    					 values.put("name", name);
//	    					 values.put("value", String.valueOf(newVersion));
//	    					 
//	    					 db.update(Const.TABLE_PARAM, values, "name=?", new String[]{name});
//	    					 
//	    					 Log.d(Const.TAG,"DBHelper.updateParam|insert Table t_param ok,name="+name+",value="+newVersion);   
//	    	            	 
//	    				}
//	    				
//	    			}
//		            
//	            }else{
//	            	String sql = "insert into "+Const.TABLE_PARAM+" values('"+name+"','"+Const.IR_LOCAL_VERSION+"')";  
//	            	db.execSQL(sql);
//	            	Log.d(Const.TAG,"DBHelper.updateParam|insert Table t_param ok,name="+name+",value="+newVersion);   
//	            }
//	        }
//        	
//            return true;
//            
//        }catch(Exception e){
//            Log.e(Const.TAG,"DBHelper.saveUser|insert Table t_param err ,name="+name+",value="+newVersion);   
//            return false;   
//        }
//        
//    }
    
    
    /**  
     * 增加数据到User
     * @param id  
     * @param uname  
     * @return  
     */  
    public boolean saveUser(String type,String utoken,String tokensecret){
        String sql="";   
        try{   
            sql="insert into "+Const.TABLE_NAME_USER+" values(null,'"+type+"','"+utoken+"','"+tokensecret+"')";   
            db.execSQL(sql);   
            Log.d(Const.TAG,"DBHelper.saveUser|insert Table t_user ok");   
            return true;
        }catch(Exception e){
            Log.e(Const.TAG,"DBHelper.saveUser|insert Table t_user err ,sql: "+sql);   
            return false;   
        }   
    }
    
    /**  
     * 查询是否有登录微博key
     * @return Cursor 指向结果记录的指针，类似于JDBC 的 ResultSet  
     */  
    public Cursor readUser(String type){
        Cursor cur=db.query(Const.TABLE_NAME_USER, new String[]{"type","utoken","tokensecret"}, "type=?",new String[]{type}, null, null,"_id");
        return cur;   
    }
    
    /**  
     * 收藏商品
     * @param id  
     * @param uname  
     * @return  
     */  
    public boolean save(String pid,String name,int price,int shops,int reviews,String picurl){
        String sql="";   
        try{   
        	ContentValues cv = new ContentValues();
        	cv.put("pid", pid);
        	cv.put("name", name);
        	cv.put("price", price);
        	cv.put("shops", shops);
        	cv.put("reviews", reviews);
        	cv.put("picurl", picurl);
        	
        	db.insert(Const.TABLE_NAME, "_id", cv);
        	
            //sql="insert into "+Const.TABLE_NAME+" values(null,'"+pid+"','"+name+"',"+price+","+shops+","+reviews+",'"+picurl+"')";   
            //db.execSQL(sql);   
            Log.d(Const.TAG,"DBHelper.save|insert Table t_sc ok");   
            return true;
        }catch(Exception e){
            Log.e(Const.TAG,"DBHelper.save|insert Table t_sc err ,sql: "+sql);   
            return false;   
        }   
    }
    
    /**  
     * 查询收藏商品记录  
     *   
     * @return Cursor 指向结果记录的指针，类似于JDBC 的 ResultSet  
     */  
    public Cursor loadALL(){
        Cursor cur=db.query(Const.TABLE_NAME, new String[]{"pid","name","price","shops","reviews","picurl"}, null,null, null, null,"_id");
        return cur;   
    }
    
    
    /**  
     * 删除某条收藏数据  
     * @param id  
     * @param uname  
     * @return  
     */  
    public boolean delData(String pid) {
    	boolean res = false;
    	try{
	    	int re = db.delete(Const.TABLE_NAME, "pid = '"+pid+"'", null);
	    	close();
	    	if(re > 0){
	    		res = true;
	    	}
	    }catch(Exception e){
            Log.e(Const.TAG,"DBHelper.delData|t_sc pid="+pid+",err:"+e);
        }
	    return res;
    }
    
    
    /**
     * 查询某条收藏记录是否存在,根据pid查询
     * @param pid
     * @return
     */
    public boolean queryData(String pid){
    	boolean res = false;
    	Cursor cur=db.query(Const.TABLE_NAME, new String[]{"pid"}, "pid=?",new String[]{pid}, null, null,null);
    	if(cur!=null && cur.getCount() > 0){
    		res = true;
    	}else{
    		res = false;
    	}
    	cur.close();
    	return res;
    }
    
    /**
     * 清空表数据
     * @return
     */
    public boolean deleteAllData(String tableName){
    	try{
	    	db.delete(tableName, null, null);
	    	return true;
	    }catch(Exception e){
            Log.e(Const.TAG,"DBHelper.deleteAllData|t_sc err:"+e);
            return false;
        }
    }
    
    /**
     * 关闭数据库
     */
    public void close(){  
        db.close();
    	Log.d(Const.TAG, "DBHelper.close|success"); 
        db = null;
    }
    

    
//  private void initTable(){
//  	
//  	if(!tabIsExist(Const.TABLE_NAME)){
//  		CreateTable();
//  	}
//  }
//  
//  /**
//   * 判断某张表是否存在
//   * @param tabName 表名
//   * @return
//   */
//  private boolean tabIsExist(String tablename){
//  	
//  	boolean result = false;
//      
//  	Cursor cursor = null;
//  	try {
//          String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"+tablename+"' ";
//          cursor = db.rawQuery(sql, null);
//          if(cursor.moveToNext()){
//          	int count = cursor.getInt(0);
//          	if(count>0){
//          		result = true;
//          	}
//          }
//          cursor.close();
//  	} catch (Exception e) {
//  		Log.e(Const.TAG, "DBHelper.tabIsExist|error="+e);
//  		if(cursor!=null && !cursor.isClosed()){
//  			cursor.close();
//  		}
//  	}               
//  	return result;
//  } 
  
}  
