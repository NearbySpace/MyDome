package com.example.mydome.handwriting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.example.mydome.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class HandwritingActivity extends Activity implements OnClickListener{
	private SignaturePad mSignaturePad;//自定义的一张画板类
	private Button bt_save;
	private Button bt_clear;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activtty_handwriting);
		initView();
	}
	private void initView() {
		mSignaturePad=(SignaturePad) findViewById(R.id.Signature_Pad);
		bt_save=(Button) findViewById(R.id.bt_save);
		bt_clear=(Button) findViewById(R.id.bt_clear);
		bt_save.setOnClickListener(this);
		bt_clear.setOnClickListener(this);
		//对画板内容的监听
		mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener(){

			@Override
			public void onSigned() {//当画板上有内容时，保存和清除按钮可用
				bt_save.setEnabled(true);
				bt_clear.setEnabled(true);
			}

			@Override
			public void onClear() {//当画板上没有内容时，保存和清除按钮不可用
				bt_save.setEnabled(false);
				bt_clear.setEnabled(false);
			}
			
		});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_save:
			//getSignatureBitmap，将画板的内容作为一个位图返回
			Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
			if(addSignatureToGallery(signatureBitmap)){
				Toast.makeText(HandwritingActivity.this, "已保存到本地", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(HandwritingActivity.this, "保存到本地失败", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.bt_clear:
			mSignaturePad.clear();//清空画板上的内容
			break;
		default:
			break;
		}
		
	}
	
	//取得图片的保存位置
	public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
		//getExternalStoragePublicDirectory取得公共目录，第一个参数是该公共文件夹保存的文件的类型，
		//第二个参数为在该公共文件夹下创建一个名为：albumName的文件夹。这里是取得android存放图片的目录
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "文件目录没有被创建");
        }
        return file;
    }
	
	//把得到的Bitmap转换成指定格式的图片
	public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }
	
	//保存图片并通知媒体库有内容更新
	public boolean addSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {
        	//String.format("Signature_%d.jpg", System.currentTimeMillis()),%d代表后面的一个参数
            File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo);
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(photo);
            mediaScanIntent.setData(contentUri);
            HandwritingActivity.this.sendBroadcast(mediaScanIntent);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
	
}
