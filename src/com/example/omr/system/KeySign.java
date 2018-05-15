package com.example.omr.system;

/**
 * 调号
 *
 */
public class KeySign {

	private int key;// 调号值(-7~7)
	private int posX, posY;// 中心位置坐标
	
	public KeySign(int key, int posX, int posY) {
		super();
		this.key = key;
		this.posX = posX;
		this.posY = posY;
	}
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
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
