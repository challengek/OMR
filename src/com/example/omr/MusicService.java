package com.example.omr;

import java.io.File;
import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener{

	private static String MUSIC_PATH = getSDPath() + java.io.File.separator + "omrdir";
	
	private MediaPlayer player;
	private static Uri URI;
	
	private final IBinder binder = new MusicBinder();
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// 初始化时就创建一个MediaPlayer进行资源链接
        player = new MediaPlayer();
        try {
			player.setDataSource(MUSIC_PATH + "temp.mid");
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        player.setOnCompletionListener(this);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return binder;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		stopSelf();
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(!player.isPlaying()) {
			player.start();
		}
		return START_STICKY;
	}
	
	public void onDestroy() {
		super.onDestroy();
		if(player.isPlaying()) {
			player.stop();
		}
		player.release();
	}
	
	/**
	 * 定义Binder对象与Activity交互
	 *
	 */
	class MusicBinder extends Binder {
		MusicService getService() {
			return MusicService.this;
		}
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

}
