package android.source.tuangou.framework.update;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.source.tuangou.framework.Config;
import android.source.tuangou.framework.util.StringUtil;


public class RemoteVersion
{

	class XmlHandler extends DefaultHandler{

		final RemoteVersion this$0;

		public void startDocument(){
			clientVersionInfoList.clear();
		}

		//sax对xml的解析
		public void startElement(String s, String s1, String s2, Attributes attributes){
			ClientVersionInfo clientversioninfo = null;
			try {
				if (!"version".equalsIgnoreCase(s1)) {
					if (!"client".equalsIgnoreCase(s1)) {
						return;
					} else {
						RemoteVersion remoteversion1 = RemoteVersion.this;
						clientversioninfo = remoteversion1.new ClientVersionInfo();
						
						int i = Integer.valueOf(
								attributes.getValue("version-code")).intValue();
						clientversioninfo.versionCode = i;
						
						String s4 = StringUtil.getValueOrDefault(
								attributes.getValue("client-version"), "");
						clientversioninfo.clientVersion = s4;
						
						String s5 = StringUtil.getValueOrDefault(
								attributes.getValue("file"), "");
						clientversioninfo.fileName = s5;

						String s6 = StringUtil.getValueOrDefault(
								attributes.getValue("current-web-version"), "");
						clientversioninfo.currentWebVersion = s6;
						
						String s7 = StringUtil.getValueOrDefault(
								attributes.getValue("current-web-file"), "");
						clientversioninfo.currentWebFile = s7;
					
						boolean flag = clientVersionInfoList.add(clientversioninfo);
					}
				} else {
					RemoteVersion remoteversion = RemoteVersion.this;
					String s3 = StringUtil.getValueOrDefault(
							attributes.getValue("current-stable-client"), "");
					remoteversion.currentStableClientVersion = s3;
				}
			} catch (Exception e) {
				clientversioninfo.versionCode = 0;
				// TODO: handle exception
			}

		}

		//构造函数
		private XmlHandler(){
			super();
			this$0 = RemoteVersion.this;
		}
	}

	//客户端版本信息类
	public class ClientVersionInfo{

		public String clientVersion;
		public String currentWebFile;
		public String currentWebVersion;
		public String fileName;
		final RemoteVersion this$0;
		public int versionCode;

		public ClientVersionInfo(){
			super();
			this$0 = RemoteVersion.this;
		}
	}


	private List clientVersionInfoList;
	public String currentStableClientVersion;

	public RemoteVersion(){
		ArrayList arraylist = new ArrayList();
		clientVersionInfoList = arraylist;
	}

	public ClientVersionInfo getClientVersionInfo(String s){
		ClientVersionInfo clientversioninfo = null;
		if (StringUtil.isEmpty(s).booleanValue())
			return clientversioninfo;
		Iterator iterator = clientVersionInfoList.iterator();
		String s1;
		do{
			if (!iterator.hasNext())
				break;
			clientversioninfo = (ClientVersionInfo)iterator.next();
			s1 = clientversioninfo.clientVersion;
		} while (!s.equalsIgnoreCase(s1));
		return clientversioninfo;

	}

	public ClientVersionInfo getCurrentClientVersionInfo()
	{
		String s = Config.CLIENT_VERSION;
		return getClientVersionInfo(s);
	}

	public ClientVersionInfo getCurrentStableClientVersionInfo()
	{
		String s = currentStableClientVersion;
		return getClientVersionInfo(s);
	}

	public String getCurrentWebVersion()
	{
		ClientVersionInfo clientversioninfo = getCurrentClientVersionInfo();
		String s;
		if (clientversioninfo == null)
			s = null;
		else
			s = clientversioninfo.currentWebVersion;
		return s;
	}

	public boolean loadFromUrl(String s){
		System.out.println("loadFromUrl s = "+s);
		boolean flag = false;
		HttpURLConnection httpurlconnection = null;
		try {
			httpurlconnection = (HttpURLConnection) (new URL(s))
					.openConnection();
			httpurlconnection.setConnectTimeout(5000);
			if (httpurlconnection.getResponseCode() != 200){
				return false;
			}
			
			InputStream inputstream = httpurlconnection.getInputStream();
			SAXParser saxparser = SAXParserFactory.newInstance().newSAXParser();
			XmlHandler xmlhandler = new XmlHandler();
			saxparser.parse(inputstream, xmlhandler);
			
			inputstream.close();
			flag = true;
			if (httpurlconnection != null){
				httpurlconnection.disconnect();
			}
			
		} catch (MalformedURLException me) {
			// TODO: handle exception
			me.printStackTrace();
			if (httpurlconnection != null)
				httpurlconnection.disconnect();
		}catch (IOException ie) {
			// TODO: handle exception
			ie.printStackTrace();
			if (httpurlconnection != null)
				httpurlconnection.disconnect();
		}catch (ParserConfigurationException pe) {
			// TODO: handle exception
			pe.printStackTrace();
			if (httpurlconnection != null)
				httpurlconnection.disconnect();
		}catch (SAXException se) {
			// TODO: handle exception
			se.printStackTrace();
			if (httpurlconnection != null)
				httpurlconnection.disconnect();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			if (httpurlconnection != null)
				httpurlconnection.disconnect();
		}
		return flag;
	
	}

}
