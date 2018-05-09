package com.example.omr.struct;

import java.util.HashMap;
import java.util.Map;

public class Vrun {

	private int col; //扫描线列坐标
	private int begRow; //起始行坐标
	private int endRow; //终止行坐标
	private Map<Integer, Hrun> hruns; //垂直游程的每个点所属水平游程列表
	
	public Vrun() {
		super();
		this.hruns = new HashMap<Integer, Hrun>();
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public int getBegRow() {
		return begRow;
	}
	public void setBegRow(int begRow) {
		this.begRow = begRow;
	}
	public int getEndRow() {
		return endRow;
	}
	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}
	public Map<Integer, Hrun> getHruns() {
		return hruns;
	}
	public void setHruns(Map<Integer, Hrun> hruns) {
		this.hruns = hruns;
	}
}
