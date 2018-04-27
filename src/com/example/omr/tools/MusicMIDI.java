package com.example.omr.tools;

import java.io.File;
import java.io.IOException;

import android.media.MediaPlayer;

public class MusicMIDI {

	public static void play(MediaPlayer player, File musicFile) {
		try {
			player.reset(); 
			player.setDataSource(musicFile.getAbsolutePath()); //重新设置要播放的音频
			player.prepare(); //预加载音频
			player.start(); //开始播放
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
	}
	
	public static void stop(MediaPlayer player) {
		if(player.isPlaying()) {
			player.stop();
		}
	}
	
}
