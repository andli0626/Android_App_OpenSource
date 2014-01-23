package android.source.tuangou.framework.file;

import java.io.File;

public class FileHelper{
	public FileHelper(){
		
	}

	public static boolean delete(String s){
		File file = new File(s);
		boolean flag;
		if (!file.exists()){
			flag = false;
		}
		else{
			if (file.isFile()){
				flag = deleteFile(s);
			}
			else{
				flag = deleteDirectory(s);
			}
		}
		return flag;
	}

	public static boolean deleteDirectory(String s){
		File file;
		boolean flag = false;
		String s1 = File.separator;
		if (!s.endsWith(s1)){
			StringBuilder stringbuilder = (new StringBuilder()).append(s);
			String s2 = File.separator;
			s = stringbuilder.append(s2).toString();
		}
		
		file = new File(s);
		if (file.exists() && file.isDirectory()){
			int i;
			File afile[];
			int j;
			i = 1;
			afile = file.listFiles();
			int k = afile.length;
			
			for(j = 0; j < k; j++){
				if(afile[j].isFile()){
					flag = deleteFile(afile[j].getAbsolutePath());
				}
				else{
					deleteDirectory(afile[j].getAbsolutePath());
					flag = file.delete();
				}
			}
		}else{
			 flag = false;
		}
		return flag;

	}

	public static boolean deleteFile(String s){
		File file = new File(s);
		boolean flag;
		if (file.exists() && file.isFile()){
			file.delete();			
			flag = true;
		}
		else
			flag = false;
		return flag;
	}

	public static String getMIMEType(File file){
		String s = file.getName();
		int i = s.lastIndexOf(".") + 1;
		int j = s.length();
		String s1 = s.substring(i, j).toLowerCase();
		String s2;
		if (s1.equals("m4a") || s1.equals("mp3") || s1.equals("mid") || s1.equals("xmf") || s1.equals("ogg") || s1.equals("wav"))
			s2 = "audio";
		else
		if (s1.equals("3gp") || s1.equals("mp4"))
			s2 = "video";
		else
		if (s1.equals("jpg") || s1.equals("gif") || s1.equals("png") || s1.equals("jpeg") || s1.equals("bmp"))
			s2 = "image";
		else
		if (s1.equals("apk"))
			s2 = "application/vnd.android.package-archive";
		else
			s2 = "*";
		if (!s1.equals("apk"))
			s2 = (new StringBuilder()).append(s2).append("/*").toString();
		return s2;
	}

	public static String getMIMEType(String s){
		return getMIMEType(new File(s));
	}
}
