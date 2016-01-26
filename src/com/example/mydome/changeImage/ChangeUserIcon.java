package com.example.mydome.changeImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mydome.R;
import com.example.mydome.handwriting.HandwritingActivity;

public class ChangeUserIcon extends Activity implements OnClickListener {
	private final int REQUEST_CODE_PHOTE = 0; // 访问相册的请求码
	private final int REQUEST_CODE_CAMERA = 1;// 访问相机的请求码
	private final int REQUEST_CODE_CROP = 2;// 调用系统裁剪的请求码
	private final String IMAGE_FILE_NAME = "MyDome";// 文件夹的名称（保存相机拍摄的图片）
	private ImageView mImageView;
	private Button mBtnPhoto;
	private Button mBtnCamera;
	private File fielPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_user_icon);
		initView();
	}

	private void initView() {
		mImageView = (ImageView) findViewById(R.id.change_user_icon_iv);
		mBtnPhoto = (Button) findViewById(R.id.change_user_icon_btn_photo);
		mBtnCamera = (Button) findViewById(R.id.change_user_icon_btn_camera);
		mBtnPhoto.setOnClickListener(this);
		mBtnCamera.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 用户没有进行有效的设置操作，返回
		if (resultCode == RESULT_CANCELED) {
			Toast.makeText(ChangeUserIcon.this, "操作取消", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		switch (requestCode) {
		case REQUEST_CODE_PHOTE:
			System.out.println(data.getData());
			startPictureCrop(data.getData());
			break;
		case REQUEST_CODE_CAMERA:
			/*
			 * 如果图片保存在：data/data/com.mx.browser/files”下，即系统内存下，会出现拍完照“拍照”---》“确定”
			 * 后， 没有数据返回，是因为files目录对于文件的大小有限制
			 */
			File tempFile = new File(Environment.getExternalStorageDirectory(),
					IMAGE_FILE_NAME);
			// fielPath在点击事件那里已经赋值的，但这里还是为空？？？？？？？？？？
			System.out.println("fielPath---->" + fielPath);
			startPictureCrop(Uri.fromFile(new File(tempFile, "head.png")));
			break;

		case REQUEST_CODE_CROP:
			setImageToHeadView(data);

			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.change_user_icon_btn_photo:
			Intent intentFromPhoto = new Intent();
			// 设置跳转到相册的意图
			intentFromPhoto.setAction(Intent.ACTION_PICK);
			// 设置数据和类型，如果要选择指定格式的图片则可以：image/jpeg 、 image/png等的类型
			intentFromPhoto.setDataAndType(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			startActivityForResult(intentFromPhoto, REQUEST_CODE_PHOTE);
			break;

		case R.id.change_user_icon_btn_camera:
			Intent intentFromCamera = new Intent();
			// 设置启动相机的意图
			intentFromCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
			File file = new File(Environment.getExternalStorageDirectory(),
					IMAGE_FILE_NAME);
			if (!file.exists()) {
				file.mkdirs();
			}
			fielPath = new File(file, "head.png");
			System.out.println("fielPath---->" + fielPath);
			// 指定调用相机拍照后的照片存储的路径
			intentFromCamera.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(fielPath));
			startActivityForResult(intentFromCamera, REQUEST_CODE_CAMERA);
			break;

		default:
			break;
		}

	}

	// 把得到的Bitmap转换成指定格式的图片
	public void saveBitmapToPNG(Bitmap bitmap, File photo) throws IOException {
		Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(newBitmap);
		canvas.drawColor(Color.WHITE);
		canvas.drawBitmap(bitmap, 0, 0, null);
		OutputStream stream = new FileOutputStream(photo);
		newBitmap.compress(Bitmap.CompressFormat.PNG, 80, stream);
		stream.close();
	}

	// 取得图片的保存位置
	public File getAlbumStorageDir(String albumName) {
		// Get the directory for the user's public pictures directory.
		// 保存图片的文件夹
		File file = new File(new File(
				Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME),
				albumName);
		if (!file.mkdirs()) {
			Log.e("SignaturePad", "文件目录没有被创建");
		}
		return file;
	}

	// 保存图片并通知媒体库有内容更新
	public boolean addSignatureToGallery(Bitmap signature) {
		boolean result = false;
		try {
			// String.format("Signature_%d.jpg",
			// System.currentTimeMillis()),%d代表后面的一个参数
			File photo = new File(getAlbumStorageDir("HeadImage"),
					String.format("head_icon_%d.png",
							System.currentTimeMillis()));
			saveBitmapToPNG(signature, photo);
			Intent mediaScanIntent = new Intent(
					Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			Uri contentUri = Uri.fromFile(photo);
			mediaScanIntent.setData(contentUri);
			ChangeUserIcon.this.sendBroadcast(mediaScanIntent);
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 提取保存裁剪之后的图片数据,设置给ImageView
	private void setImageToHeadView(Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			mImageView.setImageBitmap(photo);
			addSignatureToGallery(photo);
		}

	}

	// 开始裁剪
	private void startPictureCrop(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		System.out.println("uri----->" + uri);
		intent.setDataAndType(uri, "image/*");
		// crop=true是设置在开启的Intent中设置显示的VIEW可裁剪,即设置可裁剪
		intent.putExtra("crop", true);
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高,这里设置为像素300*300
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		// 启动裁剪页
		startActivityForResult(intent, REQUEST_CODE_CROP);
	}

}
