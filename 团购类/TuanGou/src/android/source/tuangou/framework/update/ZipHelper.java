package android.source.tuangou.framework.update;

import android.util.Log;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipHelper
{

	public ZipHelper()
	{
	}

	public static void unZipFolder(String s, String s1)
		throws Exception
	{
		int i = Log.v("XZip", "UnZipFolder(String, String)");
		FileInputStream fileinputstream = new FileInputStream(s);
		ZipInputStream zipinputstream = new ZipInputStream(fileinputstream);
		do
		{
			ZipEntry zipentry = zipinputstream.getNextEntry();
			if (zipentry != null)
			{
				String s2 = zipentry.getName();
				if (zipentry.isDirectory())
				{
					int j = s2.length() - 1;
					String s3 = s2.substring(0, j);
					StringBuilder stringbuilder = (new StringBuilder()).append(s1);
					String s4 = File.separator;
					String s5 = stringbuilder.append(s4).append(s3).toString();
					boolean flag = (new File(s5)).mkdirs();
				} else
				{
					StringBuilder stringbuilder1 = (new StringBuilder()).append(s1);
					String s6 = File.separator;
					String s7 = stringbuilder1.append(s6).append(s2).toString();
					File file = new File(s7);
					boolean flag1 = file.createNewFile();
					FileOutputStream fileoutputstream = new FileOutputStream(file);
					byte abyte0[] = new byte[1024];
					do
					{
						int k = zipinputstream.read(abyte0);
						if (k == -1)
							break;
						fileoutputstream.write(abyte0, 0, k);
						fileoutputstream.flush();
					} while (true);
					fileoutputstream.close();
				}
			} else
			{
				zipinputstream.close();
				return;
			}
		} while (true);
	}
}
