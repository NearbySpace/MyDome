package com.example.mydome.listView;

import java.util.Comparator;

import com.example.mydome.bean.ContactsInfo;

/**
 * @author huangjojo
 * PinyinComparator接口用来对ListView中的数据根据A-Z进行排序，前面两个if判断主要是将不是以汉字开头的数据放在后面
 *
 */
public class PinyinComparator implements Comparator<ContactsInfo>{

	@Override
	public int compare(ContactsInfo lhs, ContactsInfo rhs) {
		//这里主要是用来对ListView里面的数据根据ABCDEFG...来排序  
        if (rhs.getSortLetters().equals("#")) {  
            return -1;  
        } else if (lhs.getSortLetters().equals("#")) {  
            return 1;  
        } else {  
            return lhs.getSortLetters().compareTo(rhs.getSortLetters());  
        }  
	}
}
