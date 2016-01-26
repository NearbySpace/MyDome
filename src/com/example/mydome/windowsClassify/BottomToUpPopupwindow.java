package com.example.mydome.windowsClassify;

import com.example.mydome.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

public class BottomToUpPopupwindow extends PopupWindow {
	 private Button btn_take_photo, btn_pick_photo, btn_cancel;
	    private View mMenuView;
	 
	    public BottomToUpPopupwindow(Activity context,OnClickListener itemsOnClick) {
	        super(context);
	        LayoutInflater inflater = (LayoutInflater) context
	                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        mMenuView = inflater.inflate(R.layout.buttom_to_up_popupwindow, null);
	        btn_take_photo = (Button) mMenuView.findViewById(R.id.btn_take_photo);
	        btn_pick_photo = (Button) mMenuView.findViewById(R.id.btn_pick_photo);
	        btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
	        //取消按钮
	        btn_cancel.setOnClickListener(new OnClickListener() {
	 
	            public void onClick(View v) {
	                //销毁弹出框
	                dismiss();
	            }
	        });
	        //设置按钮监听
	        btn_pick_photo.setOnClickListener(itemsOnClick);
	        btn_take_photo.setOnClickListener(itemsOnClick);
	        //设置BottomToUpPopupwindow的View
	        this.setContentView(mMenuView);
	        //设置BottomToUpPopupwindow弹出窗体的宽
	        this.setWidth(LayoutParams.MATCH_PARENT);
	        //设置BottomToUpPopupwindow弹出窗体的高
	        this.setHeight(LayoutParams.WRAP_CONTENT);
	        //设置BottomToUpPopupwindow弹出窗体可点击
	        this.setFocusable(true);
	        //设置BottomToUpPopupwindow弹出窗体动画效果
	        this.setAnimationStyle(R.style.mystyle);
	        //实例化一个ColorDrawable颜色为半透明
	        ColorDrawable dw = new ColorDrawable(0xbffffff);
	        //设置BottomToUpPopupwindow弹出窗体的背景
	        this.setBackgroundDrawable(dw);
	        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
	        mMenuView.setOnTouchListener(new OnTouchListener() {
	             
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					return false;
					
				}
	        });
	 
	    }
}
