package com.example.omr.system;

/**
 * 小节线
 *
 */
public class BarLine {
	
	public static final String SINGLE = "Single";
	public static final String DOUBLE = "Double";
	public static final String LEFTREPEAT = "Leftrepeat";
	public static final String RIGHTREPEAT = "Rightrepeat";
	public static final String BACKTOBACKREPEAT = "Backtobackrepeat";
	
	private String type;// 小节线类型
	private int leftX, rightX;// 左右边界
	
	public BarLine(String type, int leftX, int rightX) {
		super();
		this.type = type;
		this.leftX = leftX;
		this.rightX = rightX;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getLeftX() {
		return leftX;
	}
	public void setLeftX(int leftX) {
		this.leftX = leftX;
	}
	public int getRightX() {
		return rightX;
	}
	public void setRightX(int rightX) {
		this.rightX = rightX;
	}
}
