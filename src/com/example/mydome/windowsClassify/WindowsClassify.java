package com.example.mydome.windowsClassify;

import com.example.mydome.R;
import com.example.mydome.adapter.MyAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class WindowsClassify extends Activity {
	private String[] title = { "对话框", "PopupWindow", "自定义弹出窗体", "底部弹出窗体" };
	private ListView lv;
	private LayoutInflater layoutInflater;
	private Dialog dialog;
	private WindowManager wm;
	private BottomToUpPopupwindow mButtonToUpPopupwindow;
	private View view;// 是位置1和2的窗体内容
	private WindowManager.LayoutParams params;
	private boolean exist = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common);
		initView();
	}

	private void initView() {
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		lv = (ListView) findViewById(R.id.common_lv);
		layoutInflater = LayoutInflater.from(WindowsClassify.this);
		MyAdapter adapter = new MyAdapter(this, title, null);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new MyOnItemClickListener());
	}

	class MyOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			switch (position) {
			case 0:
				// 对话框
				AlertDialog.Builder builder = new AlertDialog.Builder(
						WindowsClassify.this);
				View v = layoutInflater.inflate(R.layout.custom_dialog, null);
				builder.setView(v);
				dialog = builder.create();
				dialog.show();
				TextView tv1 = (TextView) v
						.findViewById(R.id.cuetom_dialog_tv_exit_radio);
				TextView tv2 = (TextView) v
						.findViewById(R.id.cuetom_dialog_tv_exit_account);
				tv1.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 退出应用的操作
						dialog.dismiss();
					}
				});
				tv2.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 退出当前账号的操作
						dialog.dismiss();
					}
				});
				break;
			case 1:
				// 弹出窗体
				showPopupWindow(view);
				break;
			case 2:
				// 自定义弹出窗体，可拖动
				if (!exist)
					myToast();
				break;
			case 3:
				mButtonToUpPopupwindow = new BottomToUpPopupwindow(
						WindowsClassify.this, itemsOnClick);
				mButtonToUpPopupwindow.showAtLocation(WindowsClassify.this
						.findViewById(R.id.common_lv),
						Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
				break;

			default:
				break;
			}

		}

	}

	private OnClickListener itemsOnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mButtonToUpPopupwindow.dismiss();
			switch (v.getId()) {
			case R.id.btn_take_photo:
				break;
			case R.id.btn_pick_photo:
				break;
			default:
				break;
			}

		}

	};

	private void showPopupWindow(View view) {

		// 一个自定义的布局，作为显示的内容
		View contentView = LayoutInflater.from(WindowsClassify.this).inflate(
				R.layout.activity_change_user_icon, null);
		// 设置按钮的点击事件
		Button button = (Button) contentView
				.findViewById(R.id.change_user_icon_btn_photo);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(WindowsClassify.this, "button is pressed",
						Toast.LENGTH_SHORT).show();
			}
		});

		final PopupWindow popupWindow = new PopupWindow(contentView,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		// 设置popwindow出现和消失动画
		// popupWindow.setAnimationStyle(R.style.PopMenuAnimation);
		// 设定 PopupWindow 取的焦点，创建出来的 PopupWindow 默认无焦点
		// popupWindow.setFocusable(true);
		popupWindow.setTouchable(true);// 取的焦点

		// 设置popwindow如果点击外面区域，便关闭。
		popupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.i("mengdd", "onTouch : ");
				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});
		// 也可用popupWindow.setOutsideTouchable(true)来代替

		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		popupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.backgroundcolor2));

		// 设置好参数之后再show
		popupWindow.showAsDropDown(view);
		// 如果需要拉动窗体，可以先定位，再更新数据。
		// showAsDropDown(View anchor)：相对某个控件的位置（正左下方），无偏移
		// showAsDropDown(View anchor, int xoff, int yoff)：相对某个控件的位置，有偏移
		// showAtLocation(View parent, int gravity, int x, int y)：
		// 相对于父控件的位置（例如正中央Gravity.CENTER，下方Gravity.BOTTOM等），可以设置偏移或无偏移
		//parent是要能获取到window唯一标示的（也就是只要能获取到window 标示，view是什么控件都可以），
		//应该是标示这个pw添加到哪个window里面，对控制pw出现位置没有影响,故parent可以是window里的任何一个View
		// 更新位置信息可用：popupWindow.update();

		// 关闭 PopupWindow
		// pop.dismiss()

	}

	private void myToast() {
		// 取出用户保存的修改数据，设置背景
		view = View.inflate(getApplicationContext(),
				R.layout.activity_change_user_icon, null);
		// TextView tv = (TextView) view.findViewById(R.id.tv_toast_address);
		Button btn = (Button) view
				.findViewById(R.id.change_user_icon_btn_photo);
		btn.setText("取消窗体");
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 移除view之前应该先判断view是否为空
				if (view != null) {
					wm.removeView(view);
					exist = false;
				}

			}
		});
		view.setBackgroundResource(R.drawable.backgroundcolor1);
		// 双击的实现,完成双击复位
		view.setOnClickListener(new OnClickListener() {
			int first_time;

			@Override
			public void onClick(View v) {
				if (first_time > 0) {
					int second_time = (int) SystemClock.uptimeMillis();
					int dtime = second_time - first_time;
					if (dtime < 400) {
						params.x = 50;
						params.y = 50;
						wm.updateViewLayout(view, params);
					} else {
						first_time = 0;
					}
				}
				first_time = (int) SystemClock.uptimeMillis();

			}
		});
		// 给view对象设置一个触摸的监听器
		view.setOnTouchListener(new OnTouchListener() {
			int startX;
			int startY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					Log.i("TAG", "X=" + startX + ",Y=" + startY);

					break;
				case MotionEvent.ACTION_MOVE:
					int newX = (int) event.getRawX();
					int newY = (int) event.getRawY();
					Log.i("TAG", "newX=" + newX + ",newY=" + newY);
					int dx = newX - startX;
					int dy = newY - startY;
					params.x += dx;
					params.y += dy;
					wm.updateViewLayout(view, params);
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;

				case MotionEvent.ACTION_UP:
					// 放开的瞬间，保存当前位置
					// Editor editor = sp.edit();
					// editor.putInt("toast_x", params.x);
					// editor.putInt("toast_y", params.y);
					// editor.commit();

					break;

				default:
					break;
				}
				return false;
			}
		});
		params = new WindowManager.LayoutParams();
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		// 设置窗体的位置
		// params.gravity=Gravity.TOP+Gravity.LEFT;
		// 如果出现的位置要和上次一样，需要用SharedPreferences来保存上一次的位置，再恢复
		// params.x = sp.getInt("toast_x", 0);
		// params.y = sp.getInt("toast_y", 0);
		params.x = 50;// 默认显示位置
		params.y = 50;
		// 设置显示的模式
		params.format = PixelFormat.TRANSLUCENT;
		// android系统里面具有电话优先级的一种窗体类型
		// params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
		params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;// 系统提示
		// 记得添加权限SYSTEM_ALERT_WINDOW。
		wm.addView(view, params);
		exist = true;
	}

}
