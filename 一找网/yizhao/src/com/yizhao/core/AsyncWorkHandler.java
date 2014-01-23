package com.yizhao.core;

import java.util.Map;
import android.content.AsyncQueryHandler;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

public class AsyncWorkHandler extends Handler implements AsyncWorkIF{   
           
    private static Looper sLooper = null;   
    
    private WorkerHandler mWorkerHanler ;   
       
    protected final class WorkerArgs{
        Handler handler;   
        Map<String,String> map;
    }
       
    public AsyncWorkHandler(){
        synchronized (AsyncQueryHandler.class) {
            if (sLooper == null) {
            	HandlerThread thread = new HandlerThread("AsyncWorkHandler");   
                thread.start();   
                sLooper = thread.getLooper();   
            }   
        }
        mWorkerHanler = new WorkerHandler(sLooper);   
    }
  
    /**
     * 获取结果并发送至消息
     * @param bean
     */
    public void doWork(Map<String,String> map){
        Message msg = mWorkerHanler.obtainMessage();
        WorkerArgs workArgs = new WorkerArgs();   
        workArgs.handler = this;
        workArgs.map = map;
        msg.obj = workArgs; 
        mWorkerHanler.sendMessage(msg);
    }   
       
    /**
     * 得到消息，需要重写
     */
    @Override  
    public void handleMessage(Message msg){}
    
    
    protected class WorkerHandler extends Handler {
    	
        public WorkerHandler(Looper looper) {   
            super(looper);   
        }
        
        @Override  
        public void handleMessage(Message msg) { 
        	
            WorkerArgs args = (WorkerArgs) msg.obj;   
           
            Map<String,String> quest_map = args.map;
            
            Object obj = excute(quest_map);//调用excute方法执行某个操作
               
            Message result = args.handler.obtainMessage();   
            
            result.obj = obj;
               
            result.sendToTarget();//发送消息至目标   
        }   
           
    }
    
    /**  
     * 实际执行方法
     */ 
    @Override
	public Object excute(Map<String, String> map) {
		return null;
	}
    
}  
