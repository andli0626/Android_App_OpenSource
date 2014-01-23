package android.source.tuangou;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.source.tuangou.framework.util.StringUtil;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import java.util.UUID;

/*
 * 
 * 
 * ActivityGroup--可用于activity之间切换
 *    ActivityGroup里面有个非常重要的成员，它是负责subActivity管理的——LocalActivityManager，
 * 可以通过getLocalActivityManager()来获的
 * 
 * */
public class ActivityContainer extends ActivityGroup{

	//ViewFlipper对象
	ViewFlipper container;
	
	//动画
	Animation leftIn;
	Animation leftOut;
	Animation rightIn;
	Animation rightOut;
	
	//activity的堆栈list
	ActivityStack stack;

	public ActivityContainer(){
		
	}

	private void openStack(ActivityStack activitystack){
		setStack(activitystack);
		HeaderWebActivity headerwebactivity = activitystack.getTop();
		if (headerwebactivity != null){
			container.setInAnimation(null);
			container.setOutAnimation(null);
			container.removeAllViews();
			View view = headerwebactivity.getWindow().getDecorView();
			showView(view, 2, "");
		}
	}

	//显示view
	private void showView(View view){
		showView(view, 0, null);
	}

	//显示view
	private void showView(View view, int i, String s){
		//添加view
		container.addView(view);
		
		android.widget.FrameLayout.LayoutParams layoutparams = new android.widget.
			FrameLayout.LayoutParams(-1, -1);
		view.setLayoutParams(layoutparams);
		//显示下一个
		container.showNext();
		
		((HeaderWebActivity)view.getContext()).triggerJavascript(i, s);
	}

	public void back(String s){
		if (stack.size() != 1){
			ViewFlipper viewflipper = container;
			Animation animation = rightIn;
			viewflipper.setInAnimation(animation);
			ViewFlipper viewflipper1 = container;
			Animation animation1 = rightOut;
			viewflipper1.setOutAnimation(animation1);
			container.removeAllViews();
			HeaderWebActivity headerwebactivity = stack.pop();
			View view = stack.getTop().getWindow().getDecorView();
			showView(view, 1, s);
			LocalActivityManager localactivitymanager = getLocalActivityManager();
			String s1 = headerwebactivity.getId();
			Window window = localactivitymanager.destroyActivity(s1, true);
		}
	}

	//返回到最顶层，如果是最后一个返回false
	public boolean backToStackBottom(){
		boolean flag;
		//判断是否已经是最顶层
		if (stack.size() <= 1){
			flag = false;
		} else{
			//返回到最顶层的activty
			container.setInAnimation(rightIn);
			container.setOutAnimation(rightOut);
			container.removeAllViews();

			HeaderWebActivity headerwebactivity = stack.pop();
			View view = stack.popToBottom().getWindow().getDecorView();
			showView(view, 1, null);
		
			LocalActivityManager localactivitymanager = getLocalActivityManager();
			String s = headerwebactivity.getId();
			Window window = localactivitymanager.destroyActivity(s, true);
			flag = true;
		}
		return flag;
	}

	//构造函数
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		
		//动画
		rightIn = AnimationUtils.loadAnimation(this, R.anim.right_in);
		rightOut = AnimationUtils.loadAnimation(this, R.anim.right_out);
		leftIn = AnimationUtils.loadAnimation(this, R.anim.left_in);
		leftOut = AnimationUtils.loadAnimation(this, R.anim.left_out);

		
		//创建ViewFlipper对象
		android.widget.FrameLayout.LayoutParams layoutparams = new android.widget.
			FrameLayout.LayoutParams(-1, -1);
		container = new ViewFlipper(this);
		container.setLayoutParams(layoutparams);
		setContentView(container);

		if (getIntent().getExtras() == null);
	}

	//打开stack或初始化
	public void openStackOrInit(ActivityStack activitystack, String s, String s1){
		//设置当前的activitystack
		setStack(activitystack);
		//销毁所有的subActivity
		getLocalActivityManager().removeAllActivities();

		//判断activitystack是否为0
		if (activitystack.size() == 0 && !StringUtil.isEmpty(s).booleanValue()){
			System.out.println("openUrl");
			openUrl(s, s1, true);
		}
		else{
			System.out.println("openStack");
			openStack(activitystack);
		}
	}

	public void openUrl(String s, String s1){
		openUrl(s, s1, false);
	}

	//打开Url
	public void openUrl(String s, String s1, boolean flag){
		Bundle bundle = new Bundle();
		bundle.putString("url", s);
		bundle.putString("ui_title", s1);
		openWebActivity(bundle, flag);
	}

	//打开WebActivity
	public void openWebActivity(Bundle bundle){
		//调用具体打开webActivity的方法
		openWebActivity(bundle, false);
	}

	//打开webActivity
	public void openWebActivity(Bundle bundle, boolean flag){
		container.setInAnimation(leftIn);
		container.setOutAnimation(leftOut);
		bundle.putBoolean("isFirst", flag);
		
		Intent intent = new Intent(this, HeaderWebActivity.class);
		Intent intent1 = intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		Intent intent2 = intent.putExtras(bundle);
		String s = UUID.randomUUID().toString();
		Window window = getLocalActivityManager().startActivity(s, intent);

		View view = window.getDecorView();
		
		//保存到stack堆栈中
		if (bundle != null){
			String s1 = bundle.getString("not_in_stack");
			if (!"true".equalsIgnoreCase(s1))
			{
				HeaderWebActivity headerwebactivity = (HeaderWebActivity)window.getContext();
				headerwebactivity.setId(s);
				
				//保存到stack堆栈中
				stack.push(headerwebactivity);
			}
		}
		showView(view);
	}

	//设置
	public void setStack(ActivityStack activitystack){
		stack = activitystack;
	}
}
