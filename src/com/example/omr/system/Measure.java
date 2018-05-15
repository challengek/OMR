package com.example.omr.system;

import java.util.LinkedList;

/**
 * 乐谱小节
 *
 */
public class Measure {

	private Clef clef;// 谱号
	private KeySign keySign;// 调号
	private TimeSign timeSign;// 拍号
	private LinkedList<Chord> chords;// 音符链表
	private BarLine barLine;// 小节线
	
	public Measure(Clef clef, KeySign keySign, TimeSign timeSign, LinkedList<Chord> chords, BarLine barLine) {
		super();
		this.clef = clef;
		this.keySign = keySign;
		this.timeSign = timeSign;
		this.chords = chords;
		this.barLine = barLine;
	}
	public Clef getClef() {
		return clef;
	}
	public void setClef(Clef clef) {
		this.clef = clef;
	}
	public KeySign getKeySign() {
		return keySign;
	}
	public void setKeySign(KeySign keySign) {
		this.keySign = keySign;
	}
	public TimeSign getTimeSign() {
		return timeSign;
	}
	public void setTimeSign(TimeSign timeSign) {
		this.timeSign = timeSign;
	}
	public LinkedList<Chord> getChords() {
		return chords;
	}
	public void setChords(LinkedList<Chord> chords) {
		this.chords = chords;
	}
	public BarLine getBarLine() {
		return barLine;
	}
	public void setBarLine(BarLine barLine) {
		this.barLine = barLine;
	}
}
