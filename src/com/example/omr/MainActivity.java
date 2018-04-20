package com.example.omr;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.example.omr.tools.ImgPretreatment;
import com.example.omr.tools.MusicRecognition;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private static final int PHOTO_CAPTURE = 0x11;// 拍照
	private static final int PHOTO_RESULT = 0x12;// 结果
	
	private static String IMG_PATH = getSDPath() + java.io.File.separator + "omrdir";
	
	private static ImageButton btnCamera;
	private static ImageButton btnPicture;
	private static ImageButton btnPlay;
	private static ImageButton btnPause;
	private static ImageView imgSelected;
	private static ImageView imgTreated;
	
	private static TextView tvResult;

	private static Bitmap bitmapSelected;
	private static Bitmap bitmapTreated;
	
	// 该handler用于处理修改结果的任务
	public static Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			tvResult.setText((String) msg.obj);
			super.handleMessage(msg);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 绑定按键
		btnCamera = (ImageButton) findViewById(R.id.btn_camera);
		btnPicture = (ImageButton) findViewById(R.id.btn_picture);
		btnPlay = (ImageButton) findViewById(R.id.btn_play);
		btnPause = (ImageButton) findViewById(R.id.btn_pause);
		imgSelected = (ImageView) findViewById(R.id.img_selected);
		imgTreated = (ImageView) findViewById(R.id.img_treated);
		
		tvResult = (TextView) findViewById(R.id.tv_result);
		
		// 绑定监听器
		btnCamera.setOnClickListener(new OnClick());
		btnPicture.setOnClickListener(new OnClick());
		btnPlay.setOnClickListener(new OnClick());
		btnPause.setOnClickListener(new OnClick());
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// TODO Auto-generated method stub
		if (resultCode == Activity.RESULT_CANCELED)
			return;

		if (requestCode == PHOTO_CAPTURE) {
			startPhotoCrop(Uri.fromFile(new File(IMG_PATH, "temp.jpg")));
		}
		
		if (requestCode == PHOTO_RESULT) {
			bitmapSelected = decodeUriAsBitmap(Uri.fromFile(new File(IMG_PATH, "temp_cropped.jpg")));
			showPicture(imgSelected, bitmapSelected);
			bitmapTreated = ImgPretreatment.doPretreatment(bitmapSelected);
			showPicture(imgTreated, bitmapTreated);
			
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					Message msg = new Message();
//					String str = MusicRecognition.getSomeone(bitmapTreated);
//					msg.obj = str;
//					myHandler.sendMessage(msg);
//				}
//
//			}).start();
		}
		
	}

	/**
	 * 绑定响应事件
	 *
	 */
	class OnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = null;
			switch(v.getId()) {
			case R.id.btn_camera: 
				intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(IMG_PATH, "temp.jpg")));
				startActivityForResult(intent, PHOTO_CAPTURE);
				break;
			case R.id.btn_picture: 
				intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/*");
				intent.putExtra("crop", "true");
				intent.putExtra("scale", true);
				intent.putExtra("return-data", false);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(IMG_PATH, "temp_cropped.jpg")));
				intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
				intent.putExtra("noFaceDetection", true);
				startActivityForResult(intent, PHOTO_RESULT);
				break;
			case R.id.btn_play: 
				intent = new Intent(MainActivity.this, TestActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_pause: 
				intent = new Intent(MainActivity.this, TestActivity.class);
				startActivity(intent);
				break;
			default : startActivity(intent);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * 获取sd卡的路径
	 * 
	 * @return 路径的字符串
	 */
	private static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取外存目录
		}
		return sdDir.toString();
	}
	
	/**
	 * 调用系统图片编辑进行裁剪
	 */
	private void startPhotoCrop(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(IMG_PATH, "temp_cropped.jpg")));
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, PHOTO_RESULT);
	}
	
	/**
	 * 根据URI获取位图
	 * @param uri
	 * @return 对应的位图
	 */
	private Bitmap decodeUriAsBitmap(Uri uri) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}
	
	/**
	 *  将图片显示在view中
	 * @param iv
	 * @param bmp
	 */
	private static void showPicture(ImageView iv, Bitmap bmp){
		iv.setImageBitmap(bmp);
	}

}
