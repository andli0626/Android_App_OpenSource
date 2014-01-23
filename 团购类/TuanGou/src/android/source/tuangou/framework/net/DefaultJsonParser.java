package android.source.tuangou.framework.net;

import android.source.tuangou.framework.util.LogUtil;

import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class DefaultJsonParser extends JsonParser
{

	public DefaultJsonParser(){
		super(new String[0]);
	}

	public void parseJson(JSONObject jsonobject, Map map){
		String as[] = getArgs();
		int i;
		i = as.length;

		try {
			for (int j = 0; j < i; j++) {
				String s = as[j];
				Object obj = jsonobject.get(s);
				Object obj1 = map.put(s, obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
	}
}
