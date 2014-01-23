package android.source.tuangou.framework.util;

import android.util.Log;

public class LogUtil
{

	public static final String LOG_TAG = "tuan800_android";

	public LogUtil()
	{
	}

	public static void d(String s)
	{
		d(null, s);
	}

	public static void d(Throwable throwable, String s)
	{
		try {
			System.out.println("log s = "+s);
			log(3, s, throwable);
		} catch (Exception e) {
			StringBuilder stringbuilder = (new StringBuilder()).append("Failed to d: ");
			String s1 = e.getMessage();
			String s2 = stringbuilder.append(s1).toString();
			int j = Log.e("tuan800_android", s2);
			// TODO: handle exception
		}
	}

	public static void e(Throwable throwable)
	{
		String s = throwable.getMessage();
		e(throwable, s);
	}

	public static void e(Throwable throwable, String s)
	{
		try {
			log(6, s, throwable);
		} catch (Exception e) {
			StringBuilder stringbuilder = (new StringBuilder()).append("Failed to e: ");
			String s1 = e.getMessage();
			String s2 = stringbuilder.append(s1).toString();
			int j = Log.e("tuan800_android", s2);
			// TODO: handle exception
		}
	}

	public static void i(String s){
		log(4, s, null);
	}

	private static void log(int j, String s, Throwable throwable)
	{
		StringBuilder stringbuilder = (new StringBuilder()).append(s);
		String s1 = "";
		if (throwable != null){
			String s3 = Log.getStackTraceString(throwable);
			
			try {
				s1 = s3;
				String s2 = stringbuilder.append(s1).toString();
				int k = Log.println(j, "tuan800_android", s2);
			} catch (Exception e) {
				StringBuilder stringbuilder1 = (new StringBuilder()).append("Failed to log: ");
				String s4 = e.getMessage();
				String s5 = stringbuilder1.append(s4).toString();
				int l = Log.e("tuan800_android", s5);
				// TODO: handle exception
			}
			
		
		} 
	}
}
