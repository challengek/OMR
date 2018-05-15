package com.example.omr;

import java.io.File;
import java.io.FileNotFoundException;

import com.example.omr.system.MusicSystem;
import com.example.omr.tools.ImgPretreatment;
import com.example.omr.tools.MusicLineInfo;
import com.example.omr.tools.MusicMIDI;
import com.example.omr.tools.MusicSymbolInfo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
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
	
	private static final int PHOTO_CAPTURE = 0x11; // 拍照
	private static final int PHOTO_RESULT = 0x12; // 结果
	private static final int CHANGE_SELECTED_IMAGE = 0x21;// 刷新预处理的图片
	private static final int CHANGE_TREATED_IMAGE = 0x22;// 刷新预处理的图片
	private static final int CHANGE_DELETED_IMAGE = 0x23;// 刷新删除谱线后的图片
	private static final int MUSIC_PLAYING = 0x31;// 音乐播放
	private static final int MUSIC_STOPED = 0x32;// 音乐停止
	private static final int MUSIC_OVER = 0x33;// 音乐播放完成
	private static final int COMPLETION = 0x41;// 音乐播放完成
	
	private static String DIR_PATH = getSDPath() + java.io.File.separator + "omrdir";
	
	private static ImageButton btnCamera;
	private static ImageButton btnPicture;
	private static ImageButton btnPlay;
	private static ImageButton btnPause;
	private static ImageView imgSelected;
	private static ImageView imgTreated;
	private static ImageView imgDeleted;
	
	private static TextView tvResult;
	private static TextView dataView;

	private static Bitmap bitmapSelected;
	private static Bitmap bitmapTreated;
	private static Bitmap bitmapDeletedLine;
	
	private MediaPlayer player; // MediaPlayer对象
	private File musicFile;
	
	private Thread thread = null;
	
	// 该handler用于处理修改结果的任务
	public static Handler stateHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == MUSIC_PLAYING) {
				tvResult.setText("播放中...\n");
			} else if(msg.what == MUSIC_STOPED) {
				tvResult.setText("已停止!\n");
			} else if(msg.what == MUSIC_OVER) {
				tvResult.setText("音乐播放完毕!\n");
			} else if(msg.what == CHANGE_SELECTED_IMAGE) {
				tvResult.setText("已获取到图片!\n正在预处理图片...");
			} else if(msg.what == CHANGE_TREATED_IMAGE) {
				tvResult.setText("图片预处理完成!\n正在删除谱线...");
			} else if(msg.what == CHANGE_DELETED_IMAGE) {
				tvResult.setText("乐谱谱线已删除!\n正在识别音符...");
			} else if(msg.what == COMPLETION) {
				tvResult.setText("乐谱识别完成!\n");
			}
			super.handleMessage(msg);
		}

	};
	
	public static Handler dataHandler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			dataView.setText("计算完成!" + msg.obj);
			super.handleMessage(msg);
		}
	};
	
	public static Handler imageHandler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == CHANGE_TREATED_IMAGE) {
				showPicture(imgTreated, bitmapTreated);
			} else if(msg.what == CHANGE_DELETED_IMAGE) {
				showPicture(imgDeleted, bitmapDeletedLine);
			}
			
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
		imgDeleted = (ImageView) findViewById(R.id.img_deleted);
		
		tvResult = (TextView) findViewById(R.id.tv_result);
		dataView = (TextView) findViewById(R.id.data_view);
		
		// 绑定监听器
		btnCamera.setOnClickListener(new OnClick());
		btnPicture.setOnClickListener(new OnClick());
		btnPlay.setOnClickListener(new OnClick());
		btnPause.setOnClickListener(new OnClick());
		btnPlay.setEnabled(false);
		btnPause.setEnabled(false);
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// TODO Auto-generated method stub
		if (resultCode == Activity.RESULT_CANCELED)
			return;

		if (requestCode == PHOTO_CAPTURE) {
			startPhotoCrop(Uri.fromFile(new File(DIR_PATH, "temp.jpg")));
		}
		
		if (requestCode == PHOTO_RESULT) {
			bitmapSelected = decodeUriAsBitmap(Uri.fromFile(new File(DIR_PATH, "temp_cropped.jpg")));
			showPicture(imgSelected, bitmapSelected);
			
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					Message stateMSG;
					Message imageMSG;
					Message dataMSG;
					
					stateMSG = new Message();
					stateMSG.what = CHANGE_SELECTED_IMAGE;
					stateHandler.sendMessage(stateMSG);
					
					imageMSG = new Message();
					stateMSG = new Message();
					bitmapTreated = ImgPretreatment.doPretreatment(bitmapSelected);
					imageMSG.what = CHANGE_TREATED_IMAGE;
					stateMSG.what = CHANGE_TREATED_IMAGE;
					imageHandler.sendMessage(imageMSG);
					stateHandler.sendMessage(stateMSG);
					
					imageMSG = new Message();
					stateMSG = new Message();
					bitmapDeletedLine = MusicLineInfo.getImageAfterDelLine(bitmapTreated);
					imageMSG.what = CHANGE_DELETED_IMAGE;
					stateMSG.what = CHANGE_DELETED_IMAGE;
					imageHandler.sendMessage(imageMSG);
					stateHandler.sendMessage(stateMSG);
					
					// 测试数据区
					dataMSG = new Message();
					String str = "";
					str = "\n线宽:" + MusicLineInfo.getStaffW() + "\n间宽:" + MusicLineInfo.getStaffSpace();
					dataMSG.obj = str;
					dataHandler.sendMessage(dataMSG);
					
					MusicSystem ms = MusicSymbolInfo.rebuildTabs(bitmapDeletedLine);
					MusicMIDI.generateEventSeq(ms, DIR_PATH);
					
					stateMSG = new Message();
					stateMSG.what = COMPLETION;
					stateHandler.sendMessage(stateMSG);
					
				}

			});
			thread.start();
			btnPlay.setEnabled(true);
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
			Message stateMSG = new Message();
		    String str;
			switch(v.getId()) {
			case R.id.btn_camera: 
				btnPlay.setEnabled(false);
			    btnPause.setEnabled(false);
				intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(DIR_PATH, "temp.jpg")));
				startActivityForResult(intent, PHOTO_CAPTURE);
				break;
			case R.id.btn_picture: 
				btnPlay.setEnabled(false);
			    btnPause.setEnabled(false);
				intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/*");
				intent.putExtra("crop", "true");
				intent.putExtra("scale", true);
				intent.putExtra("return-data", false);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(DIR_PATH, "temp_cropped.jpg")));
				intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
				intent.putExtra("noFaceDetection", true);
				startActivityForResult(intent, PHOTO_RESULT);
				break;
			case R.id.btn_play: 
				musicFile = new File(DIR_PATH, "temp.mid"); //获取要播放的文件
			    if(musicFile.exists()){ 
			      player = MediaPlayer.create(MainActivity.this, Uri.parse(musicFile.getAbsolutePath())); //创建MediaPlayer
			      // 绑定音乐播放完成事件
			      player.setOnCompletionListener(new OnCompletionListener() {
						
						@Override
						public void onCompletion(MediaPlayer mp) {
							btnPlay.setEnabled(true);
							btnPause.setEnabled(false);
							Message stateMSG;
							
							stateMSG = new Message();
							stateMSG.what = MUSIC_OVER;
							stateHandler.sendMessage(stateMSG);

						}
					});
			    }
			    btnPlay.setEnabled(false);
			    btnPause.setEnabled(true);
			    MusicMIDI.play(player, musicFile);
			    stateMSG.what = MUSIC_PLAYING;
			    stateHandler.sendMessage(stateMSG);
				break;
			case R.id.btn_pause: 
				btnPlay.setEnabled(true);
			    btnPause.setEnabled(false);
				MusicMIDI.stop(player);
			    stateMSG.what = MUSIC_STOPED;
			    stateHandler.sendMessage(stateMSG);
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
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(player.isPlaying()) {
			player.stop();
		}
		player.release();
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
			sdDir = Environment.getExternalStorageDirectory(); // 获取外存目录
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
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(DIR_PATH, "temp_cropped.jpg")));
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
