package com.example.omr.system;

public class Note {

	private String shape;// 记录符头形状(实心或空心)和休止符类型
	private int pos;// 记录符头的谱线位置
	private String accid;// 记录变音记号类型(升音记号、降音记号、还原记号)
	private int dis;// 记录变音记号位置
	
	public Note(String shape, int pos, String accid, int dis) {
		super();
		this.shape = shape;
		this.pos = pos;
		this.accid = accid;
		this.dis = dis;
	}
	public String getShape() {
		return shape;
	}
	public void setShape(String shape) {
		this.shape = shape;
	}
	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
	public String getAccid() {
		return accid;
	}
	public void setAccid(String accid) {
		this.accid = accid;
	}
	public int getDis() {
		return dis;
	}
	public void setDis(int dis) {
		this.dis = dis;
	}
}
