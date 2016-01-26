package com.example.mydome.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.example.mydome.R;

import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class FileOperation extends Activity implements OnClickListener {
	private final String TAG = "FileOperation";
	private String path;
	private String sdRootPath;
	private TextView tv_file;
	private TextView tv_in_or_out_Storage;
	private TextView tv_mkdir;
	private TextView tv_delete;
	private TextView tv_directory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file);
		initView();
		// initData();
		// showFileList(path);
		stringTest();
	}

	private void initView() {
		tv_file = (TextView) findViewById(R.id.file_tv_file);
		tv_in_or_out_Storage = (TextView) findViewById(R.id.file_tv_in_or_out_Storage);
		tv_mkdir = (TextView) findViewById(R.id.file_tv_mkdir);
		tv_delete = (TextView) findViewById(R.id.file_tv_delete);
		tv_directory = (TextView) findViewById(R.id.file_tv_directory);
		findViewById(R.id.file_btn_delete).setOnClickListener(this);
		findViewById(R.id.file_btn_directory).setOnClickListener(this);
		findViewById(R.id.file_btn_in_or_out_Storage).setOnClickListener(this);
		findViewById(R.id.file_btn_mkdir).setOnClickListener(this);
	}

	private void initData() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			sdRootPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath();// 取得根目录的绝对路径
			File file = new File(sdRootPath + "/MyDome");// 在SD卡上创建应用目录
			if (!file.exists()) {
				file.mkdir();// 如果没有该目录就创建该目录
			}
			Log.i(TAG, "绝对路径：" + file.getAbsolutePath() + "-------getPath:"
					+ file.getPath());
			// 使用mkdir创建一个目录，它的上一级目录必须存在。
			// 故创建/mnt/sdcard/MyDome/Download目录必须要先创建MyDome目录
			// 使用mkdirs可以创建多级目录，即没有上级目录也可以。
			File file2 = new File(file.getPath() + "/Download/");
			if (!file2.exists()) {
				file2.mkdir();
			}
			path = file2.getAbsolutePath();
			File file3 = new File(file2.getPath() + "/test/");
			if (!file3.exists()) {
				file3.mkdir();
			}
		}
	}

	/**
	 * 显示文件夹内的全部文件
	 * 
	 * @param path
	 *            文件夹路径
	 */
	private void showFileList(String path) {
		Vector<String> vecFile = new Vector<String>();
		File file = new File(path);
		File[] subFile = file.listFiles();

		for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
			Log.i(TAG, "历遍" + subFile[iFileLength].getPath());
			// 判断是否为文件夹
			if (!subFile[iFileLength].isDirectory()) {
				// 取得文件名
				String filename = subFile[iFileLength].getName();
				Log.i(TAG, "历遍" + subFile[iFileLength].getName());
				// 判断是否为MP4结尾
				if (filename.trim().toLowerCase().endsWith(".mp4")) {
					vecFile.add(filename);
				}
			}
		}
	}

	private void stringTest() {
		String str = "hjk/jhkdgh/njgfk/kdbm/huang.apk";
		Log.i(TAG, "长度:" + str.length());
		int start = str.lastIndexOf("/");
		int end = str.lastIndexOf(".");
		Log.i(TAG, "start:" + start + "---->end:" + end);
		str.substring(start, end);// 截取的结果是：/huang。即包含开始位置字符，不包含end的位置的字符
		Log.i(TAG, "截断:" + str.substring(start, end));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.file_btn_delete:

			break;
		case R.id.file_btn_directory:
			showFileList();

			break;
		case R.id.file_btn_in_or_out_Storage:
			showInOrOutStoragePath();
			break;
		case R.id.file_btn_mkdir:

			break;

		default:
			break;
		}

	}

	private void showInOrOutStoragePath() {
		StringBuilder log = new StringBuilder();
		String inPath = getInnerSDCardPath();
		log.append("内置SD卡路径：" + inPath + "\r\n");

		List<String> extPaths = getExtSDCardPath();
		for (String path : extPaths) {
			log.append("外置SD卡路径：" + path + "\r\n");
		}
		tv_in_or_out_Storage.setText(log.toString());
		System.out.println(log.toString());

	}

	public String getInnerSDCardPath() {
		return Environment.getExternalStorageDirectory().getPath();
	}

	public List<String> getExtSDCardPath() {
		List<String> lResult = new ArrayList<String>();
		try {
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec("mount");
			InputStream is = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
//				System.out.println(line);
				//外置存储的挂载信息:/mnt/media_rw/extSdCard /storage/extSdCard sdcardfs rw,seclabel,nosuid,nodev,relatime,uid=1023,gid=1023,derive=unified 0 0
				//对于不同的手机厂商，写的挂载信息有可能不同，故使用"extSdCard"来判定不一定正确，需根据挂载信息来写				
				if (line.contains("extSdCard")) {
					System.out.println(line);
					String[] arr = line.split(" ");
					String path = arr[1];
					File file = new File(path);
					if (file.isDirectory()) {
						lResult.add(path);
					}
				}
			}
			isr.close();
		} catch (Exception e) {
		}
		return lResult;
	}

	private void showFileList() {
		tv_directory.setText("getFilesDir = " + getFilesDir()
				+ "\ngetExternalFilesDir = " + getExternalFilesDir("MyImage")
				+ "\ngetDownloadCacheDirectory = "
				+ Environment.getDownloadCacheDirectory()
				+ "\ngetDataDirectory = " + Environment.getDataDirectory()
				+ "\ngetExternalStorageDirectory = "
				+ Environment.getExternalStorageDirectory());

	}

}
