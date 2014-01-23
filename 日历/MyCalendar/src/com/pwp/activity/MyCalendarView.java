package com.pwp.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.pwp.borderText.BorderText;
import com.pwp.dao.ScheduleDAO;

/**
 * 
 * @author lilin
 * @date 2013-2-5 下午5:12:40
 * @annotation 主界面
 */
public class MyCalendarView extends Activity implements OnGestureListener,
		OnClickListener {

	private ViewFlipper mViewFlipper = null;
	private GestureDetector gestureDetector = null;

	private MyCalendarAdp mAdp = null;

	private GridView mGridView = null;

	private BorderText topTextView = null;

	private static int jumpMonth = 0; // 每次滑动，增加或减去一个月,默认为0（即显示当前月）
	private static int jumpYear = 0; // 滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;

	private String curDateStr = "";

	private ScheduleDAO dao = null;

	Button backTodayButton;
	Button selectDataButton;

	@SuppressLint("SimpleDateFormat")
	public MyCalendarView() {

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
		curDateStr = sdf.format(date); // 当期日期
		year_c = Integer.parseInt(curDateStr.split("-")[0]);
		month_c = Integer.parseInt(curDateStr.split("-")[1]);
		day_c = Integer.parseInt(curDateStr.split("-")[2]);

		dao = new ScheduleDAO(this);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mycalendarview);

		backTodayButton = (Button) findViewById(R.id.backtodaybutton);
		selectDataButton = (Button) findViewById(R.id.selectdatabutton);

		backTodayButton.setOnClickListener((OnClickListener) this);
		selectDataButton.setOnClickListener((OnClickListener) this);

		gestureDetector = new GestureDetector(this);
		mViewFlipper = (ViewFlipper) findViewById(R.id.flipper);
		mViewFlipper.removeAllViews();

		mAdp = new MyCalendarAdp(this, getResources(), jumpMonth,
				jumpYear, year_c, month_c, day_c);

		addGridView();

		mGridView.setAdapter(mAdp);
		mViewFlipper.addView(mGridView, 0);

		topTextView = (BorderText) findViewById(R.id.toptext);
		addTopTextView(topTextView);

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		int gvFlag = 0; // 每次添加gridview到viewflipper中时给的标记

		// 左滑动
		if (e1.getX() - e2.getX() > 120) {

			addGridView(); // 添加一个gridView
			jumpMonth++; // 下一个月

			mAdp = new MyCalendarAdp(this, getResources(), jumpMonth,
					jumpYear, year_c, month_c, day_c);
			mGridView.setAdapter(mAdp);

			addTopTextView(topTextView);

			gvFlag++;

			mViewFlipper.addView(mGridView, gvFlag);

			mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_left_in));
			mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_left_out));

			mViewFlipper.showNext();
			mViewFlipper.removeViewAt(0);
			return true;
		}
		// 右滑动
		else if (e1.getX() - e2.getX() < -120) {

			addGridView(); // 添加一个gridView
			jumpMonth--; // 上一个月

			mAdp = new MyCalendarAdp(this, getResources(), jumpMonth,
					jumpYear, year_c, month_c, day_c);
			mGridView.setAdapter(mAdp);

			gvFlag++;

			addTopTextView(topTextView);

			mViewFlipper.addView(mGridView, gvFlag);

			mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_right_in));
			mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_right_out));

			mViewFlipper.showPrevious();
			mViewFlipper.removeViewAt(0);
			return true;
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, Menu.FIRST, Menu.FIRST, "今天");
		menu.add(0, Menu.FIRST + 1, Menu.FIRST + 1, "跳转");
		menu.add(0, Menu.FIRST + 2, Menu.FIRST + 2, "日程");
		menu.add(0, Menu.FIRST + 3, Menu.FIRST + 3, "日期转换");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST:
			gotoToday();
			break;
		case Menu.FIRST + 1:
			selectDate();

			break;
		case Menu.FIRST + 2:
			Intent intent = new Intent();
			intent.setClass(MyCalendarView.this, ScheduleAllView.class);
			startActivity(intent);
			break;
		case Menu.FIRST + 3:
			Intent intent1 = new Intent();
			intent1.setClass(MyCalendarView.this, MyCalendarConvertView.class);
			intent1.putExtra("date", new int[] { year_c, month_c, day_c });
			startActivity(intent1);
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	//
	private void selectDate() {
		new DatePickerDialog(this, new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// 1901-1-1 ----> 2049-12-31
				if (year < 1901 || year > 2049) {
					// 不在查询范围内
					new AlertDialog.Builder(MyCalendarView.this)
							.setTitle("错误日期")
							.setMessage("跳转日期范围(1901/1/1-2049/12/31)")
							.setPositiveButton("确认", null).show();
				} else {
					int gvFlag = 0;
					addGridView(); // 添加一个gridView
					mAdp = new MyCalendarAdp(MyCalendarView.this,
							getResources(), year, monthOfYear + 1, dayOfMonth);
					mGridView.setAdapter(mAdp);
					addTopTextView(topTextView);
					gvFlag++;
					mViewFlipper.addView(mGridView, gvFlag);
					if (year == year_c && monthOfYear + 1 == month_c) {
						// nothing to do
					}
					if ((year == year_c && monthOfYear + 1 > month_c)
							|| year > year_c) {
						mViewFlipper.setInAnimation(AnimationUtils
								.loadAnimation(MyCalendarView.this,
										R.anim.push_left_in));
						mViewFlipper.setOutAnimation(AnimationUtils
								.loadAnimation(MyCalendarView.this,
										R.anim.push_left_out));
						mViewFlipper.showNext();
					} else {
						mViewFlipper.setInAnimation(AnimationUtils
								.loadAnimation(MyCalendarView.this,
										R.anim.push_right_in));
						mViewFlipper.setOutAnimation(AnimationUtils
								.loadAnimation(MyCalendarView.this,
										R.anim.push_right_out));
						mViewFlipper.showPrevious();
					}
					mViewFlipper.removeViewAt(0);
					// 跳转之后将跳转之后的日期设置为当期日期
					year_c = year;
					month_c = monthOfYear + 1;
					day_c = dayOfMonth;
					jumpMonth = 0;
					jumpYear = 0;
				}
			}
		}, year_c, month_c - 1, day_c).show();

	}

	// 跳转到今天
	private void gotoToday() {

		int xMonth = jumpMonth;
		int xYear = jumpYear;
		int gvFlag = 0;

		jumpMonth = 0;
		jumpYear = 0;

		addGridView(); // 添加一个gridView

		year_c = Integer.parseInt(curDateStr.split("-")[0]);
		month_c = Integer.parseInt(curDateStr.split("-")[1]);
		day_c = Integer.parseInt(curDateStr.split("-")[2]);

		mAdp = new MyCalendarAdp(this, getResources(), jumpMonth,
				jumpYear, year_c, month_c, day_c);

		mGridView.setAdapter(mAdp);

		addTopTextView(topTextView);

		gvFlag++;

		mViewFlipper.addView(mGridView, gvFlag);

		if (xMonth == 0 && xYear == 0) {
			// nothing to do
		} else if ((xYear == 0 && xMonth > 0) || xYear > 0) {
			mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_left_in));
			mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_left_out));
			mViewFlipper.showNext();
		} else {
			mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_right_in));
			mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_right_out));
			mViewFlipper.showPrevious();
		}
		mViewFlipper.removeViewAt(0);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		return gestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {

		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {

		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {

		return false;
	}

	// 添加头部的年份 闰哪月等信息
	public void addTopTextView(TextView textView) {
		StringBuffer textDate = new StringBuffer();

		Drawable bg = getResources().getDrawable(R.drawable.top_day);
		textView.setBackgroundDrawable(bg);

		// 2013年2月 蛇年（癸巳年）
		// 2012年2月 闰四月龙年（壬辰年）

		// 2013年2月
		textDate.append(mAdp.getShowYear()).append("年")
				.append(mAdp.getShowMonth()).append("月").append("\t");

		// 显示闰月
		if (!mAdp.getLeapMonth().equals("") && mAdp.getLeapMonth() != null) {
			textDate.append("闰").append(mAdp.getLeapMonth()).append("月")
					.append("\t");
		}

		// 蛇年（癸巳年）
		textDate.append(mAdp.getAnimalsYear()).append("年").append("(")
				.append(mAdp.getCyclical()).append("年)");

		textView.setText(textDate);
		textView.setTextSize(18);
		textView.setTextColor(Color.BLACK);
		textView.setTypeface(Typeface.DEFAULT_BOLD);
	}

	// 添加gridview
	private void addGridView() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		// 取得屏幕的宽度和高度
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int Width = display.getWidth();
		int Height = display.getHeight();

		mGridView = new GridView(this);
		mGridView.setNumColumns(7);// 7列
		mGridView.setColumnWidth(46);// 每项的宽度

		if (Width == 480 && Height == 800) {
			mGridView.setColumnWidth(69);
		}

		mGridView.setGravity(Gravity.CENTER_VERTICAL);

		isShowGridViewBorder(mGridView, false);// 是否显示网格边框

		mGridView.setBackgroundResource(R.drawable.mycalendar_gridview_bg);

		mGridView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {// 将gridview中的触摸事件回传给gestureDetector

				return gestureDetector.onTouchEvent(event);
			}
		});

		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				// Toast.makeText(MyCalendarView.this, position % 7 + "",
				// Toast.LENGTH_SHORT).show();

				int startPosition = mAdp.getStartPositon();// 当月第一天的位置
				int endPosition = mAdp.getEndPosition();// 当月最后一天的位置
				Log.i("andli", "点击位置=" + position);
				Log.i("andli", "当月第一天，最后一天的位置[" + startPosition + ","
						+ endPosition + "]");

				// 本月范围内方可添加日程
				if (startPosition <= position && position <= endPosition) {
					String scheduleDay = mAdp.getDateByClickItem(position)
							.split("\\.")[0]; // 这一天的阳历
					// String scheduleLunarDay =
					// mAdp.getDateByClickItem(position).split("\\.")[1];
					// //这一天的阴历
					String scheduleYear = mAdp.getShowYear();
					String scheduleMonth = mAdp.getShowMonth();

					// 通过日期查询这一天是否被标记，如果标记了日程就查询出这天的所有日程信息
					String[] scheduleIDs = dao.getScheduleByTagDate(
							Integer.parseInt(scheduleYear),
							Integer.parseInt(scheduleMonth),
							Integer.parseInt(scheduleDay));
					if (scheduleIDs != null && scheduleIDs.length > 0) {
						// 跳转到显示这一天的所有日程信息界面
						Intent intent = new Intent();
						intent.setClass(MyCalendarView.this,
								ScheduleInfoView.class);
						intent.putExtra("scheduleID", scheduleIDs);
						startActivity(intent);

					}
					// 直接跳转到需要添加日程的界面
					else {
						ArrayList<String> scheduleDate = new ArrayList<String>();
						scheduleDate.add(scheduleYear);
						scheduleDate.add(scheduleMonth);
						scheduleDate.add(scheduleDay);
						scheduleDate.add(getWeek(position));

						Intent intent = new Intent();
						intent.putStringArrayListExtra("scheduleDate",
								scheduleDate);
						intent.setClass(MyCalendarView.this,
								ScheduleAddView.class);
						startActivity(intent);
					}
				}
				// else {
				// Toast.makeText(MyCalendarView.this, "非当月，无法新增日程",
				// Toast.LENGTH_SHORT).show();
				// }
			}

		});
		mGridView.setLayoutParams(params);

	}

	// 是否显示GridView的边框
	private void isShowGridViewBorder(GridView mGridView2, boolean b) {
		if (b) {
			mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT)); // 去除gridView边框
			mGridView.setVerticalSpacing(1);
			mGridView.setHorizontalSpacing(1);
		}

	}

	private String getWeek(int position) {
		String week = "";
		// 得到这一天是星期几
		switch (position % 7) {
		case 0:
			week = "星期日";
			break;
		case 1:
			week = "星期一";
			break;
		case 2:
			week = "星期二";
			break;
		case 3:
			week = "星期三";
			break;
		case 4:
			week = "星期四";
			break;
		case 5:
			week = "星期五";
			break;
		case 6:
			week = "星期六";
			break;
		}
		return week;

	}

	@Override
	public void onClick(View v) {
		if (v == backTodayButton) {
			gotoToday();
		} else if (v == selectDataButton) {
			selectDate();
		}

	}

}