package eriji.com.oauth;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.ch_linghu.fanfoudroid.AppParam;

public class OAuthSharedPreferencesStore implements OAuthStore {
	private static final String TAG = "OAuthSharedPreferencesStore";

	@Override
	public void store(String key, OAuthToken token) throws OAuthStoreException {
		Log.d(TAG, "store oauth token: " + token);
		SharedPreferences pref = AppParam.mPref;
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, encodeToken(token));
		editor.commit();
	}

	@Override
	public OAuthToken get(String key, String tokenType)
			throws OAuthStoreException {
		SharedPreferences pref = AppParam.mPref;
		return decodeToken(pref.getString(key, ""));
	}

	@Override
	public boolean isExists(String key, String tokenType) {
		SharedPreferences pref = AppParam.mPref;
		return pref.contains(key);
	}
	
	private String encodeToken(OAuthToken token) {
		return token.getToken() + "&" + token.getTokenSecret();
	}
	
	private OAuthToken decodeToken(String tokenStr) {
		String[] token = TextUtils.split(tokenStr, "&");
		if (token.length == 2) {
			return new OAuthToken(token[0], token[1]);
		}
		return null;
	}

}
