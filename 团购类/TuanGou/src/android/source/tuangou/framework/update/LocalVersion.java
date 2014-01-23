package android.source.tuangou.framework.update;

import android.source.tuangou.framework.util.LogUtil;

import java.io.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class LocalVersion
{

	class XmlHandler extends DefaultHandler{

		public boolean badFile;
		final LocalVersion this$0;

		public void startElement(String s, String s1, String s2, Attributes attributes){
			if (attributes != null){
				if ("version".equalsIgnoreCase(s1)){
					String s3 = attributes.getValue("client");
					clientVersion = s3;
					
					String s4 = attributes.getValue("web-file");
					webFileVersion = s4;
					if (clientVersion == null || webFileVersion == null)
						badFile = true;
				}
				return;
			}else{
				badFile = true;
			}
			return;
		}

		private XmlHandler(){
			super();
			this$0 = LocalVersion.this;
			badFile = false;
		}
	}


	public String clientVersion;
	public String webFileVersion;

	public LocalVersion(){
		
	}

	public boolean loadFromFile(String s)
	{
		boolean flag = false;
		try {
			FileInputStream fileinputstream = new FileInputStream(s);
			SAXParser saxparser = SAXParserFactory.newInstance().newSAXParser();
			XmlHandler xmlhandler = new XmlHandler();
			saxparser.parse(fileinputstream, xmlhandler);
			fileinputstream.close();
			flag = xmlhandler.badFile;
			if (!flag)
				flag = true;
			else
				flag = false;
		} catch (FileNotFoundException fe) {
			LogUtil.e(fe, "Local config.xml not found.");
			// TODO: handle exception
		}catch(Exception e){
			e.printStackTrace();
		}
		return flag;
	}
}
