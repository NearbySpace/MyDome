package com.example.mydome.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mydome.R;
import com.example.mydome.IM.ExpressionUtil;
import com.example.mydome.bean.ChatInfo;

public class ChatContentAdapter extends BaseAdapter {
	private List<ChatInfo> list;
	private ChatInfo info;
	private Context context;
	private Holder holder;
	private String zhengze = "f_static_0[0-9]{2}|f_static_10[0-7]";

	public ChatContentAdapter(Context context, List<ChatInfo> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new Holder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.chat_lv_item, null);
			holder.iv_left = (ImageView) convertView
					.findViewById(R.id.chat_lv_item_icon_head_from);
			holder.iv_right = (ImageView) convertView
					.findViewById(R.id.chat_lv_item_icon_head_to);
			holder.tv_left = (TextView) convertView
					.findViewById(R.id.chat_lv_item_text_from);
			holder.tv_right = (TextView) convertView
					.findViewById(R.id.chat_lv_item_text_to);
			holder.tv_time = (TextView) convertView
					.findViewById(R.id.chat_lv_item_time);
			holder.ll_left = (LinearLayout) convertView
					.findViewById(R.id.chat_lv_item_ll_from);
			holder.ll_right = (LinearLayout) convertView
					.findViewById(R.id.chat_lv_item_ll_to);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		info = list.get(position);
		if ("1".equals("" + info.getFromOrTo())) {
			holder.ll_right.setVisibility(View.GONE);
			holder.ll_left.setVisibility(View.VISIBLE);
			holder.tv_left.setText(ExpressionUtil.getExpressionString(context,
					info.getContent(), zhengze));

		} else {
			holder.ll_right.setVisibility(View.VISIBLE);
			holder.ll_left.setVisibility(View.GONE);
			holder.tv_right.setText(ExpressionUtil.getExpressionString(context,
					info.getContent(), zhengze));
		}
		holder.tv_time.setText(info.getTime());
		return convertView;
	}

	class Holder {
		public LinearLayout ll_right;
		public LinearLayout ll_left;
		public ImageView iv_right;
		public ImageView iv_left;
		public TextView tv_right;
		public TextView tv_left;
		public TextView tv_time;
	}

}
