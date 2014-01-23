package cn.com.karl.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;
import cn.com.karl.model.Book;

public class BookDAO {

	SQLiteDatabase db;
	BookOpenHelper bookhelper;
	public BookDAO(Context context){
		bookhelper = new BookOpenHelper(context);
	}
	public List<Book> query() {
		List<Book> list = new ArrayList<Book>();
		db = bookhelper.getWritableDatabase();
		String sql = "select * from book";
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Book book = new Book(cursor.getInt(0), cursor.getString(1),
					cursor.getString(2), cursor.getInt(3));
			list.add(book);
			cursor.moveToNext();
		}
		db.close();
		return list;
	}
	public long insert(String name,String path,int temp){
		db = bookhelper.getWritableDatabase();
		long l = 0;
		Log.d("@@@@@@@@","!!!!!!!查询的返回值："+check(name).size());
		if(check(name).size()>0){
			return 0;
		}else{
			ContentValues values = new ContentValues();
			values.put("name", name);
			values.put("path", path);
			values.put("iscollect", temp);
			l = db.insert("book", null, values);
			db.close();
			return l;
		}
	}
	public List<Book> check(String bookName){
		List<Book> list = new ArrayList<Book>();
		Log.d("~~~~~~","!!!!!书名"+bookName);
		db = bookhelper.getReadableDatabase();
		String sql = "select * from book where name = ?";
		Cursor cursor = db.rawQuery(sql,new String[] {bookName});
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Book book = new Book(cursor.getInt(0), cursor.getString(1),
					cursor.getString(2), cursor.getInt(3));
			list.add(book);
			cursor.moveToNext();
		}
		Log.d("!!!!!!!","XXXXXXXXXXList的值"+list.size());

		return list;
		
	}
	public boolean delete(int id){
		boolean boo = false;
		db = bookhelper.getWritableDatabase();
		String sql = "delete from student where id = ?";
		try{
			db.execSQL(sql,new Object[] { id });
			boo = true;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		db.close();
		
		return boo;
	}
}
