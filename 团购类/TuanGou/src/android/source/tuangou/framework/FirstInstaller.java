package android.source.tuangou.framework;

import android.content.res.AssetManager;
import android.source.tuangou.framework.file.FileHelper;
import android.source.tuangou.framework.update.LocalVersion;
import android.source.tuangou.framework.update.VersionManager;
import android.source.tuangou.framework.util.LogUtil;

import java.io.*;
import java.util.Iterator;
import java.util.List;

/*
 * 第一次安装处理类
 * */
public class FirstInstaller{

	static AssetManager am;
	static String appFilesPath;

	public FirstInstaller()
	{
	}

	private static boolean check(){
		boolean flag = false;
		
		if (Config.ASSETS_FOLDERS_TO_COPY.size() <= 0){
			return flag;
		} else{
			Iterator iterator = Config.ASSETS_FOLDERS_TO_COPY.iterator();
			
			while(true){
				if (!iterator.hasNext()) {
					return flag;
				} else{
					String s;
					s = (String)iterator.next();
					
					StringBuilder stringbuilder = new StringBuilder();
					String s1 = appFilesPath;
					String s2 = stringbuilder.append(s1).append(s).toString();
					
					if ((new File(s2)).exists()){
						String s3 = Config.UPDATE_FILE_FOLDER;
						
						if (!s.equalsIgnoreCase(s3)) {
							continue;
						}else{
							String s4;
							String s5;
							if (VersionManager.getLocalVersion() == null){
								break; 
							}
							s4 = VersionManager.getLocalVersion().webFileVersion;
							s5 = Config.CLIENT_VERSION;
							
							if (s4.equalsIgnoreCase(s5)) {
								continue;
							}else{
								flag = true;
							}
						}
					} else{
						flag = true;
					}
					
				}
			}
		}
		return flag;
		

	}

	/*
	 * 拷贝Assets文件夹下目录到sd卡中
	 * */
	public static void checkAndCopyAssetsFolders(){
		StringBuilder stringbuilder = new StringBuilder();
		String s = Application.getAppFilesPath();
		appFilesPath = stringbuilder.append(s).append("/").toString();
		
		if (check()){
			am = Application.getInstance().getAssets();
			Iterator iterator = Config.ASSETS_FOLDERS_TO_COPY.iterator();
			
			while(iterator.hasNext()){
				copyAssetFolderIfNotExists((String)iterator.next());	
			}
		}else{
		}
	}

	private static void copyAssetFolderIfNotExists(String s){
		try {
			Object obj;
			StringBuilder stringbuilder = new StringBuilder();
			String s1 = appFilesPath;
			String s2 = stringbuilder.append(s1).append(s).toString();
			
			obj = new File(s2);
			boolean flag;
			if (((File) (obj)).exists()){
				flag = FileHelper.delete(((File) (obj)).getAbsolutePath());
			}
			
			String as1[];
			int i;
			int j;
			boolean flag1 = ((File) (obj)).mkdir();
			String as[] = am.list(s);
			
			StringBuilder stringbuilder1 = new StringBuilder();
			String s3 = appFilesPath;
			obj = stringbuilder1.append(s3).append(s).append("/").toString();
			as1 = as;
			i = as1.length;
			for(j = 0; j < i; j++){
				InputStream inputstream;
				FileOutputStream fileoutputstream;
				String s4 = as1[j];
				File file = new File(((String) (obj)), s4);
				AssetManager assetmanager = am;
				String s5 = (new StringBuilder()).append(s).append("/")
						.append(s4).toString();
				inputstream = assetmanager.open(s5);
				fileoutputstream = new FileOutputStream(file);
				byte abyte0[] = new byte[4096];
				do {
					int k = inputstream.read(abyte0);
					if (k <= 0)
						break;
					fileoutputstream.write(abyte0, 0, k);
				} while (true);
				inputstream.close();
				fileoutputstream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

	}
}
