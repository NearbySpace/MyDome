package com.example.mydome.db;

import java.util.ArrayList;
import java.util.List;

import com.example.mydome.bean.ChatInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SQLUtil {
	private final String TAG="SQLUtil";
	private static SQLUtil instance;

	public static SQLUtil getInstance(){
		if(instance == null){
			instance = new SQLUtil();
		}
		return instance;
	}
	
	public void inster(Context context, ChatInfo info){
		SQLiteDatabase db=SQLHelper.getInstance(context).getWritableDatabase();
		String tableName = "chat_user"+info.getId();
		String sql="create table if not exists "+tableName+"(_id  integer primary key autoincrement,"
				+ "oneself text, other_person text, content text, time text, from_or_to integer);";
		db.execSQL(sql);
		ContentValues values = new ContentValues();
		if(info.getFromOrTo()==1){
			values.put("oneself", info.getName_who());
		}else{
			values.put("other_person", info.getName_who());
		}
		values.put("content", info.getContent());
		values.put("time", info.getTime());
		values.put("from_or_to", info.getFromOrTo()); 
		db.insert(tableName, null, values);
		Log.i(TAG, "数据插入成功");
		db.close();
	}
	
	public List<ChatInfo> getChatRecord(Context context,String id,int startPosition,int munber){
		SQLiteDatabase db=SQLHelper.getInstance(context).getReadableDatabase();
		String tableName = "chat_user"+id;
		List<ChatInfo> list = new ArrayList<ChatInfo>();
		if(tabIsExist(context, tableName)){
			String sql = "select * from "+tableName+" order by _id desc limit ? , ?";
			Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(startPosition),String.valueOf(munber)});
			int position = 0;//因为使用了倒序的查法，故内容要从0的位置插入，记录显示才是顺序的
			while(cursor.moveToNext()){
				ChatInfo info = new ChatInfo();
				int from_or_to = cursor.getInt(cursor.getColumnIndex("from_or_to"));
				info.setFromOrTo(from_or_to);
				if(from_or_to == 1){
					info.setName_who(cursor.getString(cursor.getColumnIndex("oneself")));
				}else{
					info.setName_who(cursor.getString(cursor.getColumnIndex("other_person")));
				}
				info.setContent(cursor.getString(cursor.getColumnIndex("content")));
				info.setTime(cursor.getString(cursor.getColumnIndex("time")));
				list.add(position, info);
			}
		}
		
		return list;
	}
	
	public boolean tabIsExist(Context context,String tabName){
        boolean result = false;
        if(tabName == null){
                return false;
        }
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
                db = SQLHelper.getInstance(context).getReadableDatabase();
                String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"+tabName+"';";
                cursor = db.rawQuery(sql, null);
                if(cursor.moveToNext()){
                        int count = cursor.getInt(0);
                        if(count>0){
                                result = true;
                        }
                }
                 
        } catch (Exception e) {
                // TODO: handle exception
        }                
        return result;
    }
}
