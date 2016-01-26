package com.example.mydome.animator;

import android.animation.ObjectAnimator;
import android.animation.PointFEvaluator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.mydome.R;

public class AnimatorClassify extends Activity {
	private ListView mListVeiw;
	private View view;
	private ImageView iv;
	private ImageView iv2;
	private ImageView iv3;
	private PopupWindow mPopupWindow;
	private ObjectAnimator mObjectAnimator;
	private String[] title = { "Property Animation" };
	private String[] content = { "故名思议就是通过动画的方式改变对象的属性了" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initPropertyPopupWindow();//初始化属性动画的PopupWindow
		mListVeiw = (ListView) findViewById(R.id.main_lv);
		mListVeiw.setAdapter(new MyAdapter());
		mListVeiw.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					mPopupWindow.showAsDropDown(view);
					break;

				default:
					break;
				}
			}
		});
	}

	private void initPropertyPopupWindow() {
		initViewProperty();
		mPopupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, true);
		mPopupWindow.setTouchable(true);// 取的焦点
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		mPopupWindow.setBackgroundDrawable(dw);
		// 设置popwindow如果点击外面区域，便关闭。
		mPopupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.i("mengdd", "onTouch : ");
				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});
	}

	private void initViewProperty() {
		view = LayoutInflater.from(this).inflate(
				R.layout.popupwindow_show_animator, null);
		final Button btn1 = (Button) view.findViewById(R.id.animator_btn1);
		Button btn2 = (Button) view.findViewById(R.id.animator_btn2);
		Button btn3 = (Button) view.findViewById(R.id.animator_btn3);
		iv = (ImageView) view.findViewById(R.id.animator_iv);
		iv2 = (ImageView) view.findViewById(R.id.animator_iv2);
		iv3 = (ImageView) view.findViewById(R.id.animator_iv3);
		btn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 绕X轴旋转360度
				ObjectAnimator.ofFloat(iv, "rotationX", 0.0F, 360.0F)
						.setDuration(500).start();
			}
		});
		btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ObjectAnimator anim = ObjectAnimator//
						.ofFloat(iv, "zhy", 1.0F, 0.0F)//
						.setDuration(500);//
				anim.start();
				anim.addUpdateListener(new AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						float cVal = (Float) animation.getAnimatedValue();
						iv.setAlpha(cVal);
						iv.setScaleX(cVal);
						iv.setScaleY(cVal);
					}
				});
			}
		});
		btn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PointF startPointF = new PointF(0, 0);
				PointF endPointF = new PointF(view.getWidth() - iv2.getHeight(),
						view.getHeight() - btn1.getHeight() - iv2.getHeight());
				/*使用ValueAnimator，只是根据value进行某种动作需要加监听器，监听值的变化 
				 * 做相应的处理，故需要添加一个监听器可才可以实现动画效果。而ObjectAnimator是直接改属性，
				 * 不需要添加监听器*/
				ValueAnimator animator = ValueAnimator.ofObject(
						new TypeEvaluator<PointF>() {
							// 自己定义算法，算出自己想要的数据
							@Override
							public PointF evaluate(float fraction,
									PointF startValue, PointF endValue) {
								PointF pointF = new PointF();
								pointF.x = fraction
										* (endValue.x - startValue.x);
								pointF.y = fraction
										* (endValue.y - startValue.y);
								return pointF;
							}
						}, startPointF, endPointF);
				// 使用系统默认的算法
				// ValueAnimator animator=ValueAnimator.ofObject(new
				// PointFEvaluator(), startPointF,endPointF);
				animator.setDuration(3000);
				// 设置补间器，可以控制动画的变化速率。系统内置多种样式，BounceInterpolator具有回弹效果
				animator.setInterpolator(new BounceInterpolator());
				animator.start();
				animator.addUpdateListener(new AnimatorUpdateListener() {

					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						PointF point = (PointF) animation.getAnimatedValue();
						iv2.setY(point.y);
						iv3.setX(point.x);
						iv3.setY(point.y);
					}
				});
			}
		});
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return title.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return title[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout ll = new LinearLayout(AnimatorClassify.this);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			ll.setPadding(17, 7, 0, 7);
			TextView tv_title = new TextView(AnimatorClassify.this);
			TextView tv_content = new TextView(AnimatorClassify.this);
			tv_title.setTextSize(16);
			tv_content.setTextSize(12);
			tv_title.setText(title[position]);
			tv_content.setText(content[position]);
			ll.addView(tv_title);
			ll.addView(tv_content);
			return ll;
		}

	}
}
