package android.source.tuangou.framework.net;

import android.source.tuangou.framework.util.LogUtil;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class JsonParser
{

	private String args[];

	public  JsonParser(String as[]){
		args = as;
	}

	public String[] getArgs()
	{
		return args;
	}

	//字符串对象转换成Map对象
	public Map parse(String s){
		HashMap hashmap = null;
		try {
			hashmap = new HashMap();
			JSONObject jsonobject = new JSONObject(s);
			parseJson(jsonobject, hashmap);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return hashmap;
	}

	public abstract void parseJson(JSONObject jsonobject, Map map)
		throws JSONException;
}
