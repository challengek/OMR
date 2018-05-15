package com.example.omr.system;

/**
 * 谱号
 *
 */
public class Clef {
	
	public static final String TREBLE = "Treble";// 高音谱号
	public static final String BASS = "Bass";// 低音谱号

	private String shape;// 谱号类型(高音谱号:Treble 低音谱号:Bass)
	private int posX, posY;// 中心位置坐标
	
	public Clef(String shape, int posX, int posY) {
		super();
		this.shape = shape;
		this.posX = posX;
		this.posY = posY;
	}
	public String getShape() {
		return shape;
	}
	public void setShape(String shape) {
		this.shape = shape;
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
