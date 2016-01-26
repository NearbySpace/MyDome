package com.example.mydome.listView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mydome.R;
import com.example.mydome.adapter.ContactListAdapter;
import com.example.mydome.bean.ContactsInfo;
import com.example.mydome.widget.CharacterParser;
import com.example.mydome.widget.SideBar;
import com.example.mydome.widget.SideBar.OnTouchingLetterChangedListener;

public class LetterSortListView extends Activity {
	private String[] name={};
	private SideBar mSideBar;
	private ListView mListView;
	private TextView mTextView;
	
	private ContactListAdapter adapter;
	private CharacterParser characterParser; 
	private List<ContactsInfo> SourceDateList;
	private PinyinComparator pinyinComparator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listview_letter_sort);
		name=getString(R.string.address_book).split(",");
		initView();
	}

	private void initView() {
		//实例化汉字转拼音类  
        characterParser = CharacterParser.getInstance();  
        pinyinComparator = new PinyinComparator(); 
        
		mSideBar=(SideBar) findViewById(R.id.letter_sort_sidebar);
		mListView=(ListView) findViewById(R.id.letter_sort_listview);
		mTextView=(TextView) findViewById(R.id.letter_sort_tv);
		mSideBar.setTextView(mTextView);
		
		//设置右侧触摸监听  
        mSideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {  
              
            @Override  
            public void onTouchingLetterChanged(String s) {  
                //该字母首次出现的位置  
                int position = adapter.getPositionForSection(s.charAt(0));  
                if(position != -1){  
                    mListView.setSelection(position);  
                }  
                  
            }  
        });  
        
        SourceDateList = filledData(name);
        // 根据a-z进行排序源数据  
        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new ContactListAdapter(this, SourceDateList);  
        mListView.setAdapter(adapter);  
	}
	
	
	/**
	 * 为ListView填充数据
	 * @param date
	 * @return
	 */
	private List<ContactsInfo> filledData(String [] date){  
        List<ContactsInfo> mSortList = new ArrayList<ContactsInfo>();  
          
        for(int i=0; i<date.length; i++){  
        	ContactsInfo contactsInfo = new ContactsInfo();  
        	contactsInfo.setName(date[i]);  
            //汉字转换成拼音  
            String pinyin = characterParser.getSelling(date[i]);  
            String sortString = pinyin.substring(0, 1).toUpperCase();  
              
            // 正则表达式，判断首字母是否是英文字母  
            if(sortString.matches("[A-Z]")){  
            	contactsInfo.setSortLetters(sortString.toUpperCase());  
            }else{  
            	contactsInfo.setSortLetters("#");  
            }  
              
            mSortList.add(contactsInfo);  
        }  
        return mSortList;  
          
    }  
	
}
