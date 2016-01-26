package com.example.mydome.adapter;

import java.util.List;

import com.example.mydome.R;
import com.example.mydome.bean.ContactsInfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class ContactListAdapter extends BaseAdapter implements SectionIndexer {
    private List<ContactsInfo> list = null;
    private Context mContext;

    public ContactListAdapter(Context mContext, List<ContactsInfo> list) {
        this.mContext = mContext;
        this.list = list;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * 
     * @param list
     */
    public void updateListView(List<ContactsInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        final ContactsInfo mContent = list.get(position);

        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.address_book,
                    null);
            viewHolder.nameTextview = (TextView) view.findViewById(R.id.address_book_name);
            viewHolder.tvHeader = (TextView) view.findViewById(R.id.address_book_letter);
            viewHolder.phoneNumber = (TextView) view
                    .findViewById(R.id.address_book_phone_number);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        // 根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);

        // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            viewHolder.tvHeader.setVisibility(View.VISIBLE);
            viewHolder.tvHeader.setText(mContent.getSortLetters());
        } else {
            viewHolder.tvHeader.setVisibility(View.GONE);
        }
        viewHolder.nameTextview.setText(this.list.get(position).getName());
        // 设置用户头像
        // String imgUrl
        // =list.get(position).getUserImage();//默认图片地址TEST服务器是hint型记得让后台改成url
//        if (list.get(position).getUserImage() != null
//                && NetUtil.isNetAvailable(mContext)) {
//            if(list.get(position).getUserStatus().equals("OffLine")){
//                
//            }
//            Picasso.with(mContext)
//                    .load("http://downloads.easemob.com/downloads/57.png")
//                    .placeholder(R.drawable.default_avatar)
//                    .into(viewHolder.avatar);
//        } else {
//            Picasso.with(mContext).load(R.drawable.default_avatar)
//                    .into(viewHolder.avatar);
//        }
        // UserUtils.setUserAvatar(getContext(), username, holder.avatar);
//        if (viewHolder.unreadMsgView != null)
//            viewHolder.unreadMsgView.setVisibility(View.INVISIBLE);
//        String unreadMsg = "" + list.get(position).getUnReadMessage();
//        LogHelper.e("是否有未读消息:" + unreadMsg);
//        viewHolder.unreadMsgView.setText(unreadMsg);
        return view;

    }

    final static class ViewHolder {
        TextView phoneNumber;
        TextView nameTextview;
        TextView tvHeader;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 提取英文的首字母，非英文字母用#代替。
     * 
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}