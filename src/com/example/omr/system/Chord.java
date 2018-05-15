package com.example.omr.system;

import java.util.LinkedList;

/**
 * 音符
 *
 */
public class Chord {

	private boolean virtualStem;// 标记音符有无符干
	private boolean stemUp;// 标记符干方向(true:向上 false:向下)
	private int naugdots;// 记录附点数
	private int nFlags;// 记录符尾个数
	private int stemX, stemY, stemEnd;// 记录符干位置
	private LinkedList<Note> notes;// 符头链表
	
	public Chord(boolean virtualStem, boolean stemUp, int naugdots, int nFlags, int stemX, int stemY, int stemEnd,
			LinkedList<Note> notes) {
		super();
		this.virtualStem = virtualStem;
		this.stemUp = stemUp;
		this.naugdots = naugdots;
		this.nFlags = nFlags;
		this.stemX = stemX;
		this.stemY = stemY;
		this.stemEnd = stemEnd;
		this.notes = notes;
	}
	public boolean isVirtualStem() {
		return virtualStem;
	}
	public void setVirtualStem(boolean virtualStem) {
		this.virtualStem = virtualStem;
	}
	public boolean isStemUp() {
		return stemUp;
	}
	public void setStemUp(boolean stemUp) {
		this.stemUp = stemUp;
	}
	public int getNaugdots() {
		return naugdots;
	}
	public void setNaugdots(int naugdots) {
		this.naugdots = naugdots;
	}
	public int getnFlags() {
		return nFlags;
	}
	public void setnFlags(int nFlags) {
		this.nFlags = nFlags;
	}
	public int getStemX() {
		return stemX;
	}
	public void setStemX(int stemX) {
		this.stemX = stemX;
	}
	public int getStemY() {
		return stemY;
	}
	public void setStemY(int stemY) {
		this.stemY = stemY;
	}
	public int getStemEnd() {
		return stemEnd;
	}
	public void setStemEnd(int stemEnd) {
		this.stemEnd = stemEnd;
	}
	public LinkedList<Note> getNotes() {
		return notes;
	}
	public void setNotes(LinkedList<Note> notes) {
		this.notes = notes;
	}
}
