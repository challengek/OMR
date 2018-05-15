package com.example.omr.tools;

import java.util.ArrayList;

import com.example.omr.struct.Vrun;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;

public class MusicLineInfo {

	private static ArrayList<Integer> staffPositArray;
	private static int staffW;
	
	public static Bitmap getImageAfterDelLine(Bitmap bmp) {
		staffW = countLineWidth(setVruns(bmp));// 获取谱线线宽
		staffPositArray = getLinePosit(bmp);// 获取谱线位置
		Bitmap result = delLine(staffPositArray, bmp);
		staffPositArray = setStaffW(staffPositArray); // 设置单行像素谱线位置
		return result;
	}
	
	private static ArrayList<Integer> setStaffW(ArrayList<Integer> array) {
		ArrayList<Integer> delArray = new ArrayList<Integer>();
		for(int n = 1; n < array.size(); n++) {
			if(array.get(n) - 1 == array.get(n - 1)) {
				delArray.add(array.get(n - 1));
			}
		}
		array.removeAll(delArray);
		return array;
	}
	
	public static ArrayList<Integer> getStaffPositArray() {
		return staffPositArray;
	}
	
	public static int getStaffW() {
		return staffW;
	}
	
	public static int getStaffSpace() {
		return staffPositArray.get(1) - staffPositArray.get(0) - staffW;
	}

	/**
	 * 删除谱线(采用直线追踪法删除谱线)
	 * @param array
	 * @param bmp
	 * @return
	 */
	private static Bitmap delLine(ArrayList<Integer> array, Bitmap bmp) {
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		
		for(Integer i : array) {
			for(int j = 0; j < width; j++) {
				if(pixels[width * (i - 1) + j] != Color.rgb(0, 0, 0) 
						|| pixels[width * (i + 1) + j] != Color.rgb(0, 0, 0)) {
					pixels[width * i + j] = Color.rgb(255, 255, 255);
				}
			}
		}
		Bitmap result = Bitmap
				.createBitmap(width, height, Config.RGB_565);
		result.setPixels(pixels, 0, width, 0, 0, width, height);
		return result;
	}
	
	
	/**
	 * 谱线定位(采用水平投影求出峰值即为谱线的位置)
	 * @param bmp
	 * @return
	 */
	private static ArrayList<Integer> getLinePosit(Bitmap bmp) {
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		int[] projection = new int[height];
		for(int j = 0; j < height; j++) {
			for(int i = 0; i < width; i++) {
				if(bmp.getPixel(i, j) == Color.rgb(0, 0, 0)) {
					projection[j]++;
				}
			}
		}
		
		ArrayList<Integer> array = new ArrayList<Integer>();
		
		for(int n = 0; n < projection.length; n++) {
			if(projection[n] > width * 3 / 5) {
				array.add(n);
			}
		}
		
		return array;
	}
	
	/**
	 * 计算谱线线宽
	 * @param runs
	 * @return
	 */
	private static int countLineWidth(ArrayList<Vrun> runs) {
		ArrayList<ArrayList<Integer>> counts = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> newCount = null;
		int bh = 0;
		int maxFrequency = 0;
		int maxFrequencyBh = 0;
		boolean flag = false;
		for(Vrun run : runs) {
			bh = run.getEndRow() - run.getBegRow() + 1;
			for(ArrayList<Integer> count : counts) {
				if(count.get(0) == bh) {
					count.set(1, count.get(1) + 1);
					flag = true;
					break;
				}
			}
			if(flag == false) {
				newCount = new ArrayList<Integer>();
				newCount.add(bh);
				newCount.add(1);
				counts.add(newCount);
			}
			flag = false;
		}
		
		for(ArrayList<Integer> count : counts) {
			if(count.get(1) > maxFrequency) {
				maxFrequency = count.get(1);
				maxFrequencyBh = count.get(0);
			}
		}
		
		return maxFrequencyBh;
	}
	
	/**
	 * 垂直游程编码
	 * @param bmp
	 * @return
	 */
	private static ArrayList<Vrun> setVruns(Bitmap bmp) {
		int bmpWidth = bmp.getWidth();
		int bmpHeight = bmp.getHeight();
		ArrayList<Vrun> runs = new ArrayList<Vrun>();
		for(int i = 0; i < bmpWidth; i++) {
			Vrun run = null;
			if(bmp.getPixel(i, 0) == Color.rgb(0, 0, 0)) {
				run = new Vrun();
				run.setCol(i);
				run.setBegRow(0);
				run.setEndRow(0);
			}
			for(int j = 1; j < bmpHeight; j++) {
				if(bmp.getPixel(i, j) == Color.rgb(0, 0, 0) 
						&& bmp.getPixel(i, j - 1) == Color.rgb(0, 0, 0)) {
					run.setEndRow(j);
					if(j == bmpHeight - 1 && run != null) {
						runs.add(run);
					}
				} else if(bmp.getPixel(i, j) == Color.rgb(0, 0, 0) 
						&& bmp.getPixel(i, j - 1) == Color.rgb(255, 255, 255)) {
					run = new Vrun();
					run.setCol(i);
					run.setBegRow(j);
					run.setEndRow(j);
				} else if(bmp.getPixel(i, j) == Color.rgb(255, 255, 255) 
						&& bmp.getPixel(i, j - 1) == Color.rgb(0, 0, 0)) {
					runs.add(run);
				}
			}
		}
		return runs;
	}
	
}



















