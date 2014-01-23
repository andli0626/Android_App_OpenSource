package android.source.tuangou.framework.util;

import java.io.*;
import java.util.*;
import org.json.JSONException;
import org.json.JSONObject;

public final class StringUtil
{

	public StringUtil()
	{
	}

	public static String fromBytes(byte abyte0[])
	{
		StringBuffer stringbuffer = new StringBuffer("");
		int i = 0;
		do
		{
			int j = abyte0.length;
			if (i < j)
			{
				int k = abyte0[i];
				if (k < 0)
					k += 256;
				StringBuffer stringbuffer1;
				if (k < 16)
					stringbuffer1 = stringbuffer.append("0");
				String s = Integer.toHexString(k);
				StringBuffer stringbuffer2 = stringbuffer.append(s);
				i++;
			} else
			{
				return stringbuffer.toString();
			}
		} while (true);
	}

	/*
	 * 获取相应值或采用默认值
	 * */
	public static String getValueOrDefault(String s, String s1){
		String result = isEmpty(s).booleanValue()?s1:s;
		
		return result;
	}

	public static String inputStreamToString(InputStream inputstream)
		throws IOException
	{
		InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
		BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
		StringBuilder stringbuilder = new StringBuilder();
		do
		{
			String s = bufferedreader.readLine();
			if (s != null)
			{
				String s1 = (new StringBuilder()).append(s).append("\n").toString();
				StringBuilder stringbuilder1 = stringbuilder.append(s1);
			} else
			{
				bufferedreader.close();
				return stringbuilder.toString();
			}
		} while (true);
	}

	public static Boolean isEmpty(String s)
	{
		boolean flag;
		if (s == null || s.length() == 0)
			flag = true;
		else
			flag = false;
		return Boolean.valueOf(flag);
	}

	public static String join(Collection collection, String s)
	{
		String s1;
		if (collection.size() == 0)
		{
			s1 = "";
		} else
		{
			StringBuilder stringbuilder = new StringBuilder();
			for (Iterator iterator = collection.iterator(); iterator.hasNext();)
			{
				String s2 = (String)iterator.next();
				StringBuilder stringbuilder1 = stringbuilder.append(s2).append(s);
			}

			if (stringbuilder.length() > 0)
			{
				int i = stringbuilder.length() - 1;
				int j = stringbuilder.length();
				StringBuilder stringbuilder2 = stringbuilder.delete(i, j);
			}
			s1 = stringbuilder.toString();
		}
		return s1;
	}

	public static Map parseHttpParamsToHash(String s)
	{
		HashMap hashmap = new HashMap();
		String as[] = s.split("&");
		int i = as.length;
		for (int j = 0; j < i; j++)
		{
			String s1 = as[j];
			if (s1.indexOf("=") > 0)
			{
				String as1[] = s1.split("=");
				String s2 = as1[0];
				String s3 = as1[1];
				Object obj = hashmap.put(s2, s3);
			}
		}

		return hashmap;
	}

	//字符串转换成JSONObject对象
	public static JSONObject parseJSON(String s){
		JSONObject jsonobject;
		try {
			jsonobject = new JSONObject(s);
			return jsonobject;
		} catch (Exception e) {
			e.printStackTrace();
			jsonobject = new JSONObject();
			// TODO: handle exception
		}
		return jsonobject;
	}

	public static Map parseJSONToHash(String s){
		HashMap hashmap;
		try {
			JSONObject jsonobject;
			Iterator iterator;
			jsonobject = parseJSON(s);
			iterator = jsonobject.keys();
			hashmap = new HashMap();
			while (iterator.hasNext()) {
				String s1 = (String) iterator.next();
				Object obj = jsonobject.get(s1);
				Object obj1 = hashmap.put(s1, obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
			// TODO: handle exception
		}
		return hashmap;
	}

	public static  String simpleFormat(String s, Object aobj[]){
		String as[] = s.split("%s");
		String s1;
		if (as.length < 2)
		{
			s1 = s;
		} else
		{
			StringBuilder stringbuilder = new StringBuilder();
			int i = aobj.length;
			int j = 0;
			do
			{
				int k = as.length;
				if (j >= k)
					break;
				String s2 = as[j];
				StringBuilder stringbuilder1 = stringbuilder.append(s2);
				if (j < i)
				{
					Object obj = aobj[j];
					StringBuilder stringbuilder2 = stringbuilder.append(obj);
				}
				j++;
			} while (true);
			s1 = stringbuilder.toString();
		}
		return s1;
	}
}
