package android.source.tuangou;

import java.util.ArrayList;
import java.util.List;

/*
 * ActivityStack类
 * */
public class ActivityStack{

	//List堆栈保存HeaderWebActivity对象
	List stack;

	//构造函数
	public ActivityStack(){
		stack = new ArrayList();
	}

	//获取stack最顶部的actviity
	public HeaderWebActivity getTop(){
		int i = stack.size() - 1;
		return (HeaderWebActivity)stack.get(i);
	}

	//stack堆栈进行出堆，遵守先进先出的原则
	public HeaderWebActivity pop(){
		HeaderWebActivity headerwebactivity;
		if (stack.size() == 0){
			headerwebactivity = null;
		} else{
			HeaderWebActivity headerwebactivity1 = getTop();
			List list = stack;
			int i = stack.size() - 1;
			Object obj = list.remove(i);
			headerwebactivity = headerwebactivity1;
		}
		return headerwebactivity;
	}

	//stack堆栈，pop出栈，只保留顶部
	public HeaderWebActivity popToBottom(){
		HeaderWebActivity headerwebactivity;
		if (stack.size() == 0)
			headerwebactivity = null;
		else
		if (stack.size() == 1)
		{
			headerwebactivity = (HeaderWebActivity)stack.get(0);
		} else
		{
			for (int i = stack.size() - 1; i > 0; i--)
			{
				Object obj = stack.remove(i);
			}

			headerwebactivity = (HeaderWebActivity)stack.get(0);
		}
		return headerwebactivity;
	}

	//stack进堆栈
	public void push(HeaderWebActivity headerwebactivity){
		boolean flag = stack.add(headerwebactivity);
	}

	//获取堆栈的大小
	public int size(){
		return stack.size();
	}
}
