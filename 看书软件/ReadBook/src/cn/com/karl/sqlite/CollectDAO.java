package cn.com.karl.sqlite;

import java.util.ArrayList;
import java.util.List;

import cn.com.karl.model.Book;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CollectDAO {
	SQLiteDatabase db;
	BookCollectOpenHelper collectHelper;
	public CollectDAO(Context context){
		collectHelper = new BookCollectOpenHelper(context);
	}
	public List<Book> query() {
		List<Book> list = new ArrayList<Book>();
		db = collectHelper.getWritableDatabase();
		String sql = "select * from collect";
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Book book = new Book(cursor.getInt(0), cursor.getString(1),
					cursor.getString(2));
			list.add(book);
			cursor.moveToNext();
		}
		db.close();
		return list;
	}
	public List<Book> check(String bookName){
		List<Book> list = new ArrayList<Book>();
		Log.d("~~~~~~","!!!!!书名"+bookName);
		db = collectHelper.getReadableDatabase();
		String sql = "select * from collect where co_name = ?";
		Cursor cursor = db.rawQuery(sql,new String[] {bookName});
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Book book = new Book(cursor.getInt(0), cursor.getString(1),
					cursor.getString(2));
			list.add(book);
			cursor.moveToNext();
		}
		Log.d("!!!!!!!","XXXXXXXXXXList的值"+list.size());
		return list;
	}
	
	public long insert(String name,String path){
		db = collectHelper.getWritableDatabase();
		long l = 0;
		Log.d("@@@@@@@@","!!!!!!!查询的返回值："+check(name).size());
		if(check(name).size()>0){
			return 0;
		}else{
			ContentValues values = new ContentValues();
			values.put("co_name", name);
			values.put("co_path", path);
//			values.put("iscollect", temp);
			l = db.insert("collect", null, values);
			db.close();
			return l;
		}
	}
}
