package com.example.mydome.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper {
	private static String database_name = "chat";
	private final static int VERSION=1;
	private static SQLHelper instance;
	
	public static SQLHelper getInstance(Context context){
		
		if(instance == null){
			instance=new SQLHelper(context); 
		}
		return instance;
	}

	public SQLHelper(Context context) {
		
		super(context,database_name, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
