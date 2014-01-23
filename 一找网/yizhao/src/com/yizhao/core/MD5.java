package com.yizhao.core;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Log;

public class MD5 {
	
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

      private static String toHexString(byte[] b) {  //String to  byte
                 StringBuilder sb = new StringBuilder(b.length * 2);  
                 for (int i = 0; i < b.length; i++) {  
                     sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);  
                     sb.append(HEX_DIGITS[b[i] & 0x0f]);  
                 }  
                 return sb.toString();  
      }
      
      public static String toMd5(String s) {
    	  String res = "";
            try {
                // Create MD5 Hash
                MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
                digest.update(s.getBytes());
                byte messageDigest[] = digest.digest();
                res = toHexString(messageDigest);
                Log.d(Const.TAG, "MD5:defult_str="+s);
                Log.d(Const.TAG, "MD5:convet_str"+res);
            } catch (NoSuchAlgorithmException e) {
            	Log.e(Const.TAG, "MD5.toMd5|NoSuchAlgorithmException",e);
            }    
            return res;
      }
}
