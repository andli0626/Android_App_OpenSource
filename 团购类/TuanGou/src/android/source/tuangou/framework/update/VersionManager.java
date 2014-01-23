package android.source.tuangou.framework.update;

import android.source.tuangou.framework.Application;
import android.source.tuangou.framework.Config;
import android.source.tuangou.framework.util.LogUtil;

import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class VersionManager
{

	private static LocalVersion localVersion;
	private static RemoteVersion remoteVersion;

	public VersionManager(){
	}

	//检查客户端是否需要更新
	public static boolean checkClientUpdate(){
		boolean flag = false;
		try {
			remoteVersion = getRemoteVersion();
			if (remoteVersion == null){
				return flag;
			}
			
			String loclVersionClient = Config.CLIENT_VERSION;
			String remoteVersionClient = remoteVersion.getCurrentStableClientVersionInfo().clientVersion;
			
			System.out.println("loclVersionClient = "+loclVersionClient);
			System.out.println("remoteVersionClient = "+remoteVersionClient);
			
			
			flag = compareVersion(loclVersionClient, remoteVersionClient);

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return flag;
		
	}

	public static boolean checkWebFileUpdate()
	{
		boolean flag = false;
		try {
			localVersion = null;
			RemoteVersion remoteversion = getRemoteVersion();
			LocalVersion localversion = getLocalVersion();
			if (remoteVersion == null || localVersion == null)
				return flag;
			boolean flag1;
			String s = localVersion.webFileVersion;
			String s1 = remoteVersion.getCurrentWebVersion();
			flag1 = compareVersion(s, s1);
			flag = flag1;
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return flag;
	}

	private static boolean compareVersion(String s, String s1)
		throws Exception{
		
		boolean flag = false;
		try {
			if (s1 != null && s != null) {
				String as[] = s.split("\\.");
				String as1[] = s1.split("\\.");
				int i = as.length;
				int j = as1.length;
				if (i != j)
					throw new Exception("Version file format error.");
				int k = 0;
				do {
					int l = as.length;
					if (k >= l)
						break;
					int i1 = Integer.valueOf(as[k]).intValue();
					if (Integer.valueOf(as1[k]).intValue() > i1) {
						flag = true;
						continue; /* Loop/switch isn't completed */
					}
					k++;
				} while (true);
			} else {
				flag = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
		return flag;
		
	}

	public static String getFullVersion(){
		StringBuilder stringbuilder = new StringBuilder();
		String s = Config.CLIENT_VERSION;
		StringBuilder stringbuilder1 = stringbuilder.append(s).append(".");
		String s1 = getLocalVersion().webFileVersion;
		return stringbuilder1.append(s1).toString();
	}

	public static LocalVersion getLocalVersion()
	{
		if (localVersion == null){
			StringBuilder stringbuilder = new StringBuilder();
			String s = Application.getAppFilesPath();
			StringBuilder stringbuilder1 = stringbuilder.append(s).append("/");
			String s1 = Config.UPDATE_FILE_FOLDER;
			String s2 = stringbuilder1.append(s1).append("/").append("ver.xml").toString();
			LocalVersion localversion = new LocalVersion();
			if (localversion.loadFromFile(s2))
				localVersion = localversion;
			
			if(localVersion == null){
			}else{
				
			}
		}
		return localVersion;
	}

	//获取remote版本号
	public static RemoteVersion getRemoteVersion(){
		if (remoteVersion == null){
			RemoteVersion remoteversion = new RemoteVersion();
			String s = Config.REMOTE_VERSION_URL;
			if (remoteversion.loadFromUrl(s))
				remoteVersion = remoteversion;
		}
		return remoteVersion;
	}

	private static String getStringFromNode(Node node)
		throws IOException
	{
		StringBuilder stringbuilder = new StringBuilder();
		if (node.getNodeType() != 3){
			NodeList nodelist;
			int k;
			StringBuilder stringbuilder5;
			if (node.getNodeType() != 9)
			{
				StringBuffer stringbuffer = new StringBuffer();
				int i = 0;
				do
				{
					int j = node.getAttributes().getLength();
					if (i >= j)
						break;
					StringBuffer stringbuffer1 = stringbuffer.append(" ");
					String s1 = node.getAttributes().item(i).getNodeName();
					StringBuffer stringbuffer2 = stringbuffer1.append(s1).append("=\"");
					String s2 = node.getAttributes().item(i).getNodeValue();
					StringBuffer stringbuffer3 = stringbuffer2.append(s2).append("\" ");
					i++;
				} while (true);
				StringBuilder stringbuilder2 = stringbuilder.append("<");
				String s3 = node.getNodeName();
				StringBuilder stringbuilder3 = stringbuilder2.append(s3).append(" ").append(stringbuffer).append(">");
			} else
			{
				stringbuilder5 = stringbuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			}
			nodelist = node.getChildNodes();
			k = 0;
			for (int l = nodelist.getLength(); k < l; k++)
			{
				String s4 = getStringFromNode(nodelist.item(k));
				StringBuilder stringbuilder4 = stringbuilder.append(s4);
			}

			if (node.getNodeType() != 9)
			{
				StringBuilder stringbuilder6 = stringbuilder.append("</");
				String s5 = node.getNodeName();
				StringBuilder stringbuilder7 = stringbuilder6.append(s5).append(">");
			}
			return stringbuilder.toString();
		}else{
			String s = node.getNodeValue();
			StringBuilder stringbuilder1 = stringbuilder.append(s);
			return stringbuilder1.toString();
		}
		
	}

	public static void updateLocalWebFileVersion(){
		String s2;
		try {
			StringBuilder stringbuilder = new StringBuilder();
			String s = Application.getAppFilesPath();
			StringBuilder stringbuilder1 = stringbuilder.append(s).append("/");
			String s1 = Config.UPDATE_FILE_FOLDER;
			s2 = stringbuilder1.append(s1).append("/").append("ver.xml")
					.toString();
			DocumentBuilder documentbuilder = DocumentBuilderFactory
					.newInstance().newDocumentBuilder();
			File file = new File(s2);
			FileInputStream fileinputstream = new FileInputStream(file);
			Document document = documentbuilder.parse(fileinputstream);
			fileinputstream.close();
			Element element = (Element) document.getDocumentElement()
					.getElementsByTagName("version").item(0);
			String s3 = Config.CLIENT_VERSION;
			element.setAttribute("client", s3);
			String s4 = getRemoteVersion().getCurrentWebVersion();
			element.setAttribute("web-file", s4);
			String s5 = getStringFromNode(document.getDocumentElement());
			File file1 = new File(s2);
			FileOutputStream fileoutputstream = new FileOutputStream(file1);
			OutputStreamWriter outputstreamwriter = new OutputStreamWriter(
					fileoutputstream);
			outputstreamwriter.write(s5);
			outputstreamwriter.close();
			fileoutputstream.close();
			localVersion = null;
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
}
