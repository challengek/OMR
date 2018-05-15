package com.example.omr.system;

import java.util.LinkedList;

/**
 * 谱表
 *
 */
public class MusicSystem {

	private int top, left;// 上左边界
	private int width, height;// 尺寸
	private LinkedList<Stave> staves;// 五线谱链表
	
	public MusicSystem(int top, int left, int width, int height, LinkedList<Stave> staves) {
		super();
		this.top = top;
		this.left = left;
		this.width = width;
		this.height = height;
		this.staves = staves;
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
	public LinkedList<Stave> getStaves() {
		return staves;
	}
	public void setStaves(LinkedList<Stave> staves) {
		this.staves = staves;
	}
}
