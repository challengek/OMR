package com.example.omr.system;

import java.util.LinkedList;

/**
 * 五线谱
 *
 */
public class Stave {

	private int top, left;// 上左边界
	private int width, height;// 尺寸
	private LinkedList<Measure> measures;// 小节链表
	
	public Stave(int top, int left, int width, int height, LinkedList<Measure> measures) {
		super();
		this.top = top;
		this.left = left;
		this.width = width;
		this.height = height;
		this.measures = measures;
	}
	public int getTop() {
		return top;
	}
	public void setTop(int top) {
		this.top = top;
	}
	public int getLeft() {
		return left;
	}
	public void setLeft(int left) {
		this.left = left;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public LinkedList<Measure> getMeasures() {
		return measures;
	}
	public void setMeasures(LinkedList<Measure> measures) {
		this.measures = measures;
	}
}
