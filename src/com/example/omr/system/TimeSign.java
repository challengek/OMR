package com.example.omr.system;

/**
 * 拍号
 *
 */
public class TimeSign {

	private int top,bottom;// 上下数字
	private int posX, posY;// 中心位置坐标
	
	public TimeSign(int top, int bottom, int posX, int posY) {
		super();
		this.top = top;
		this.bottom = bottom;
		this.posX = posX;
		this.posY = posY;
	}
	public int getTop() {
		return top;
	}
	public void setTop(int top) {
		this.top = top;
	}
	public int getBottom() {
		return bottom;
	}
	public void setBottom(int bottom) {
		this.bottom = bottom;
	}
	public int getPosX() {
		return posX;
	}
	public void setPosX(int posX) {
		this.posX = posX;
	}
	public int getPosY() {
		return posY;
	}
	public void setPosY(int posY) {
		this.posY = posY;
	}
}
