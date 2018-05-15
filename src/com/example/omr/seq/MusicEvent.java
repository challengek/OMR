package com.example.omr.seq;

public class MusicEvent {

	private int posX;// 位置
	private int pitch;// 音高
	private int durlen;// 时值
	
	public MusicEvent(int posX, int pitch, int durlen) {
		super();
		this.posX = posX;
		this.pitch = pitch;
		this.durlen = durlen;
	}
	public int getPosX() {
		return posX;
	}
	public void setPosX(int posX) {
		this.posX = posX;
	}
	public int getPitch() {
		return pitch;
	}
	public void setPitch(int pitch) {
		this.pitch = pitch;
	}
	public int getDurlen() {
		return durlen;
	}
	public void setDurlen(int durlen) {
		this.durlen = durlen;
	}
}
