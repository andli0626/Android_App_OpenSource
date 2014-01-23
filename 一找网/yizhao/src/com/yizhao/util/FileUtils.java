package com.yizhao.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.yizhao.core.Const;



import android.os.Environment;
import android.util.Log;

public class FileUtils {
	
	private String SDPATH;
	
	public String getSDPATH(){
		return SDPATH;
	}
	
	public FileUtils(){
		//得到当前外部存储设备的目录( /SDCARD )
		File sdCard = Environment.getExternalStorageDirectory(); 
		SDPATH = sdCard.getAbsolutePath() + "/";
		Log.d(Const.TAG, "FileUtils.SDPATH="+SDPATH);
	}
	
	/**
	 * 在SD卡上创建文件
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public File createSDFile(String fileName) throws IOException{
		File file = new File(fileName);
		//断点续传要放开此处，否则创建的都是新文件
		//if(!file.exists()){
			file.createNewFile();
		//}
		return file;
	}
	
	/**
	 * 在SD卡上创建目录
	 * @param dirName
	 * @return
	 */
	public File createSDDir(String dirName){
		
		File dir = null;
		
		if(dirName == null){
			dirName = "";
		}
		
		dir = new File(SDPATH + dirName);
		dir.mkdir();
		
		return dir;
	}
	
	/**
	 * 判断SD卡上的文件是否存在
	 * @param fileName
	 * @return
	 */
	public File isFileExist(String fileName){
		
		File file = new File(SDPATH + fileName);
		if(file.exists()){
			return file;
		}else{
			return null;
		}
		
	}
	
	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 * @param path 创建目录
	 * @param fileName 创建的文件名
	 * @param input 输入流
	 * @return
	 */
	public File writeFile2SDFromInput(String path,String fileName,InputStream input){
		File file = null;
		FileOutputStream output = null;
		try {
			File dir = createSDDir(path);
			file = createSDFile(dir.getPath()+"/"+fileName);
			output = new FileOutputStream(file);
			byte[] buffer = new byte[Const.BYTE_SIZE_INT];
			do{
				int numread = input.read(buffer);  
				if (numread <= 0) {   
                    break;   
                }
				output.write(buffer, 0, numread);   
			} while (true);
			input.close();
		} 
		catch (Exception e) {
			file = null;
			e.printStackTrace();
		}
		finally{
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	
	
	
//	/**
//	 * 读取SD卡某个文件内容，仅限TXT
//	 * @param path SD卡上创建的目录
//	 * @param fileName 文件名
//	 * @return
//	 */
//	public String readFromSD(String path,String fileName){
//		
//		StringBuffer sb = new StringBuffer();
//		
//		File f = new File(SDPATH + path + fileName);//这是对应文件路径
//		
//		InputStream in = null;
//		
//		try {
//			in = new BufferedInputStream(new FileInputStream(f));
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//			return "";
//		}
//		
//		BufferedReader br = null;
//		
//		try {
//			br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
//		} catch (UnsupportedEncodingException e1) {
//			e1.printStackTrace();
//			return "";
//		}
//		
//		String tmp;
//		
//		try {
//			while((tmp=br.readLine())!=null){
//				sb.append(tmp);
//			}
//			br.close();
//			in.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//			return "";
//		}
//		return sb.toString();
//	}
	
	   
    /**
     * 删除当前下载临时文件
     */
    public void delFile(String localPath){
        File myFile = new File(localPath);   
        if (myFile.exists()) {
            myFile.delete();   
        }   
    } 
    
//    /**
//     * 获取文件类型
//     * @param f
//     * @return
//     */
//    public static String getMIMEType(File f){
//        String type = "";   
//        String fName = f.getName();
//        String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();   
//        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")   
//                || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {   
//            type = "audio";   
//        } else if (end.equals("3gp") || end.equals("mp4")) {
//            type = "video";   
//        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {   
//            type = "image";   
//        } else if (end.equals("apk")) {   
//            type = "application/vnd.android.package-archive";   
//        } else {   
//            type = "*";   
//        }   
//        if (end.equals("apk")){
//        	
//        } else {   
//            type += "/*";   
//        }   
//        return type;   
//    }
	
	/**
	 * 根据URL得到输入流
	 * @param urlStr
	 * @return
	 */
	public InputStream getInputStreamFromURL(String urlStr) {
		HttpURLConnection urlConn = null;
		InputStream inputStream = null;
		try {
			URL url = new URL(urlStr);
			urlConn = (HttpURLConnection)url.openConnection();
			inputStream = urlConn.getInputStream();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inputStream;
	}
	
	/**
	 * 根据URL下载文件,前提是这个文件当中的内容是文本,函数的返回值就是文本当中的内容
	 * 1.创建一个URL对象
	 * 2.通过URL对象,创建一个HttpURLConnection对象
	 * 3.得到InputStream
	 * 4.从InputStream当中读取数据
	 * @param urlStr
	 * @return
	 */
	public String downloadTxT(String urlStr){
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader buffer = null;
		try {
			URL url = new URL(urlStr);
			HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
			buffer = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			while( (line = buffer.readLine()) != null){
				sb.append(line);
			}
			
		} 
		catch (Exception e) {
			Log.e(Const.TAG, "FileUtils.downloadTxT|Exception",e);
		}
		finally{
			try {
				buffer.close();
			} catch (IOException e) {
				Log.e(Const.TAG, "FileUtils.downloadTxT|IOException",e);
			}
		}
		return sb.toString();
	}
	
	// 复制文件    
	public void copyFile(File sourceFile,File targetFile){
		try{
			FileInputStream input = new FileInputStream(sourceFile); // 新建文件输入流并对它进行缓冲    
		
	        BufferedInputStream inBuff=new BufferedInputStream(input);   
	  
	        // 新建文件输出流并对它进行缓冲    
	        FileOutputStream output = new FileOutputStream(targetFile);   
	        BufferedOutputStream outBuff=new BufferedOutputStream(output);   
	           
	        // 缓冲数组    
	        byte[] b = new byte[Const.BYTE_SIZE_INT];   
	        int len;   
	        while ((len =inBuff.read(b)) != -1) {   
	            outBuff.write(b, 0, len);   
	        }   
	        // 刷新此缓冲的输出流    
	        outBuff.flush();   
	           
	        //关闭流    
	        inBuff.close();   
	        outBuff.close();   
	        output.close();   
	        input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	} 
	
	//获取本地文件大小    
	public long readFileSize(String filename){
		
		long size = 0;
		
		File file = new File(SDPATH+filename);
		
		if(file.exists()){
			size = file.length();
		}
		return size;
		
	} 

}