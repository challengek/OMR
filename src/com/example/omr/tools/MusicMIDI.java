package com.example.omr.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

import com.example.omr.seq.MusicEvent;
import com.example.omr.system.Chord;
import com.example.omr.system.Clef;
import com.example.omr.system.KeySign;
import com.example.omr.system.Measure;
import com.example.omr.system.MusicSystem;
import com.example.omr.system.Stave;
import com.example.omr.system.TimeSign;

import android.media.MediaPlayer;

public class MusicMIDI {
	
	private static final int TREBLE[] = {5, 0, 7, 2, 9, 4, 11};
	private static final int BASS[] = {0, -2, -3, -5, -7, -9, -10};
	
	private static final int SHARP[] = {5, 0, 7, 2, 9, 4, 11};
	private static final int FLAT[] = {11, 4, 9, 2, 7, 0, 5};
	
	public static void generateEventSeq(MusicSystem ms, String dirPath) {
		LinkedList<MusicEvent> events = new LinkedList<MusicEvent>();
		for(Stave stave : ms.getStaves()) {
			for(Measure measure : stave.getMeasures()) {
				Clef clef = measure.getClef();
				KeySign keySign = measure.getKeySign();
				TimeSign timeSign = measure.getTimeSign();
				for(Chord chord : measure.getChords()) {
					int posX = 0;
					int pos = chord.getNotes().get(0).getPos();
					int pitch = 0;
					if(Clef.TREBLE.equals(clef.getShape())) {
						if(pos >= 0) {
							pitch = 71 - (pos / 7) * 12 + TREBLE[pos % 7];
						} else {
							pitch = 71 - (pos / 7) * 12 - TREBLE[-pos % 7];
						}
					} else if(Clef.BASS.equals(clef.getShape())) {
						if(pos >= 0) {
							pitch = 50 -(pos / 7) * 12 + BASS[pos % 7];
						} else {
							pitch = 50 -(pos / 7) * 12 - BASS[-pos % 7];
						}
					}
					if(keySign.getKey() > 0) {
						for(int i = 0; i < keySign.getKey(); i++) {
							if((pitch - SHARP[i]) % 12 == 0) {
								pitch++;
							}
						}
					} else if(keySign.getKey() < 0) {
						for(int i = 0; i < -keySign.getKey(); i++) {
							if((pitch - FLAT[i]) % 12 == 0) {
								pitch--;
							}
						}
					}
					int durlen = 128;
					int nFlags = chord.getnFlags();
					durlen = durlen >> nFlags;
					MusicEvent event = new MusicEvent(posX, pitch, durlen);
					events.addLast(event);
				}
			}
		}
		generateMIDI(events, dirPath);
	}
	
	private static void generateMIDI(LinkedList<MusicEvent> events, String dirPath) {
		// TODO:
		File file = new File(dirPath, "temp.mid");
		if(file.exists()) {
			file.delete();
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file, true);
			int[] MThd = {0x4D, 0x54, 0x68, 0x64, 0x00, 0x00, 0x00, 0x06, 0x00, 0x00, 0x00, 0x01, 0x01, 0xE0};
			byte[] bMThd = new byte[14];
			for(int i = 0; i < MThd.length; i++) {
				bMThd[i] = (byte) (MThd[i] & 0xFF);
			}
			for(int i = 0; i < bMThd.length; i++) {
				fos.write(bMThd[i]);
			}
			
			// 默认使用通道1
			byte noteOn = (byte) 0x90;
			byte noteOff = (byte) 0x80;
			
			int[] MTrk = {0x4D, 0x54, 0x72, 0x6B};
			byte[] bMTrk = new byte[4];
			for(int i = 0; i < MTrk.length; i++) {
				bMTrk[i] = (byte) (MTrk[i] & 0xFF);
			}
			for(int i = 0; i < bMTrk.length; i++) {
				fos.write(bMTrk[i]);
			}
			
			int length = events.size() * 8;
			byte[] bLength = new byte[4];
			bLength[0] = (byte) (length >> 24 & 0xFF);
			bLength[1] = (byte) (length >> 16 & 0xFF);
			bLength[2] = (byte) (length >> 8 & 0xFF);
			bLength[3] = (byte) (length & 0xFF);
			for(int i = 0; i < bLength.length; i++) {
				fos.write(bLength[i]);
			}
			
			for(MusicEvent me : events) {
				byte onTime = 0;
				byte offTime = (byte) me.getDurlen();
				byte note = (byte) me.getPitch();
				byte speed = 127;
				fos.write(onTime);
				fos.write(noteOn);
				fos.write(note);
				fos.write(speed);
				fos.write(offTime);
				fos.write(noteOff);
				fos.write(note);
				fos.write(speed);
			}
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		return;
	}

	/**
	 * @param player
	 * @param musicFile
	 */
	public static void play(MediaPlayer player, File musicFile) {
		try {
			player.reset(); 
			player.setDataSource(musicFile.getAbsolutePath()); //重新设置要播放的音频
			player.prepare(); //预加载音频
			player.start(); //开始播放
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param player
	 */
	public static void stop(MediaPlayer player) {
		if(player.isPlaying()) {
			player.stop();
		}
	}
	
}
