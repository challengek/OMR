package com.example.omr.struct;

import java.util.ArrayList;

/**
 * 游程类
 *
 */
public class StructRun {

	private int col; //扫描线列坐标
	private int begRow; //起始行坐标
	private int endRow; //终止行坐标
	private ArrayList<StructRun> parents; //父游程列表
	private ArrayList<StructRun> children; //子游程列表
	private StructSection pSection; //该游程所属的图段对象
	private int tag; //访问标记
	
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public int getBegRow() {
		return begRow;
	}
	public void setBegRow(int i) {
		this.begRow = i;
	}
	public int getEndRow() {
		return endRow;
	}
	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}
	public ArrayList<StructRun> getParents() {
		return parents;
	}
	public void setParents(ArrayList<StructRun> parents) {
		this.parents = parents;
	}
	public ArrayList<StructRun> getChildren() {
		return children;
	}
	public void setChildren(ArrayList<StructRun> children) {
		this.children = children;
	}
	public StructSection getpSection() {
		return pSection;
	}
	public void setpSection(StructSection pSection) {
		this.pSection = pSection;
	}
	public int getTag() {
		return tag;
	}
	public void setTag(int tag) {
		this.tag = tag;
	}
}
