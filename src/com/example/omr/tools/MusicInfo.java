package com.example.omr.tools;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;

public class MusicInfo {

	
	/**
	 * 测试
	 */
	public static String getData(Bitmap bmp) {
//		int num = getLineWidth(setRunMap(bmp));
//		String str = "计算完成... 结果:" + num;
		String str = "";
		getLinePosit(bmp);
		return str;
	}
	
	public static Bitmap getBitmap(Bitmap bmp) {
		Bitmap result = delLine(getLinePosit(bmp), bmp);
		return result;
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
	
	//
	
//	private static int getLineWidth(ArrayList<StructRun> runs) {
//		ArrayList<ArrayList<Integer>> counts = new ArrayList<ArrayList<Integer>>();
//		ArrayList<Integer> newCount = null;
//		int bh = 0;
//		int maxFrequency = 0;
//		int maxFrequencyBh = 0;
//		boolean flag = false;
//		for(StructRun run : runs) {
//			bh = run.getEndRow() - run.getBegRow() + 1;
//			for(ArrayList<Integer> count : counts) {
//				if(count.get(0) == bh) {
//					count.set(1, count.get(1) + 1);
//					flag = true;
//					break;
//				}
//			}
//			if(flag == false) {
//				newCount = new ArrayList<Integer>();
//				newCount.add(bh);
//				newCount.add(1);
//				counts.add(newCount);
//			}
//			flag = false;
//		}
//		
//		for(ArrayList<Integer> count : counts) {
//			if(count.get(1) > maxFrequency) {
//				maxFrequency = count.get(1);
//				maxFrequencyBh = count.get(0);
//			}
//		}
//		
//		return maxFrequencyBh;
//	}
	
//	/**
//	 * 建立游程邻接图
//	 * @param bmp
//	 * @return
//	 */
//	private static ArrayList<StructRun> setRunMap(Bitmap bmp) {
//		int bmpWidth = bmp.getWidth();
//		int bmpHeight = bmp.getHeight();
//		ArrayList<StructRun> runs = new ArrayList<StructRun>();
//		for(int i = 0; i < bmpWidth; i++) {
//			StructRun run = null;
//			if(bmp.getPixel(i, 0) == Color.rgb(0, 0, 0)) {
//				run = new StructRun();
//				run.setCol(i);
//				run.setBegRow(0);
//				run.setEndRow(0);
//				run.setChildren(new ArrayList<StructRun>());
//				run.setParents(new ArrayList<StructRun>());
//				run.setTag(0);
//			}
//			for(int j = 1; j < bmpHeight; j++) {
//				if(bmp.getPixel(i, j) == Color.rgb(0, 0, 0) 
//						&& bmp.getPixel(i, j - 1) == Color.rgb(0, 0, 0)) {
//					run.setEndRow(j);
//				} else if(bmp.getPixel(i, j) == Color.rgb(0, 0, 0) 
//						&& bmp.getPixel(i, j - 1) == Color.rgb(255, 255, 255)) {
//					run = new StructRun();
//					run.setCol(i);
//					run.setBegRow(j);
//					run.setEndRow(j);
//					run.setChildren(new ArrayList<StructRun>());
//					run.setParents(new ArrayList<StructRun>());
//					run.setTag(0);
//				} else if(bmp.getPixel(i, j) == Color.rgb(255, 255, 255) 
//						&& bmp.getPixel(i, j - 1) == Color.rgb(0, 0, 0)) {
//					for(StructRun sr : runs) {
//						if(run.getCol() == sr.getCol() + 1 
//								&& !(run.getBegRow() > sr.getEndRow())
//										&& !(run.getEndRow() < sr.getBegRow())) {
//							run.getParents().add(sr);
//							sr.getChildren().add(run);
//						}
//					}
//					runs.add(run);
//				}
//			}
//		}
////		// TODO:
////		// 以下部分用于测试
////		ArrayList<StructRun> r = new ArrayList<StructRun>();
////		for(StructRun sr : runs) {
////			if(sr.getTag() != 0) {
////				r.add(sr);
////			}
////		}
//		return runs;
//	}
	
//	/**
//	 * 建立图段邻接图
//	 * @param runs
//	 * @return
//	 */
//	private ArrayList<StructSection> setSectionMap(ArrayList<StructRun> runs) {
//		StructSection section = null;
//		for(StructRun run : runs) {
//			if(run.getTag() == 0) {
//				section = depthFirst(run, null,null);
//				if(section.getParents().size() == 0 && section.getChildren().size() == 0) {
//					section.setType(0);
//				} else if(section.getParents().size() >=2 || section.getChildren().size() >= 2) {
//					section.setType(2);
//				} else {
//					section.setType(1);
//				}
//			}
//		}
//		
//		return null;
//	}
	
//	/**
//	 * @param run 当前游程
//	 * @param section 当前图段
//	 * @param lastSection 上一次生成的图段
//	 * @return
//	 */
//	private StructSection depthFirst(StructRun run, StructSection section, StructSection lastSection) {
//		run.setTag(1);
//		if(section == null) {
//			section = new StructSection();
//			section.setRuns(new ArrayList<StructRun>());
//			section.setParents(new ArrayList<StructSection>());
//			section.setChildren(new ArrayList<StructSection>());
//			sections.add(section);
//			if(lastSection != null) {
//				// TODO:
//			}
//		}
//		section.getRuns().add(run);
//		run.setpSection(section);
//		// 深度优先搜索子游程
//		for(StructRun son : run.getChildren()) {
//			if(son.getTag() == 1) {
//				// TODO:
//			} else {
//				if(run.getChildren().size() == 1 && son.getParents().size() < 2) {
//					depthFirst(son, section, null);
//				} else {
//					depthFirst(son, null, run.getpSection());
//				}
//			}
//		}
//		// 深度优先搜索父游程 
//		for(StructRun father : run.getParents()) {
//			if(father.getTag() == 1) {
//				// TODO:
//			} else {
//				if(run.getChildren().size() == 1 && father.getChildren().size() < 2) {
//					depthFirst(father, section, null);
//				} else {
//					depthFirst(father, null, run.getpSection());
//				}
//			}
//		}
//		
//		return section;
//	}
	
//	/**
//	 * 将二值化的图像转换为二值数组
//	 * @param bmp
//	 * @return
//	 */
//	private static byte[][] setPixelsToDuoValue(Bitmap bmp) {
//		byte[][] duoValue;
//		int bmpWidth = bmp.getWidth();
//		int bmpHeight = bmp.getHeight();
//		duoValue = new byte[bmpWidth][bmpHeight];
//		int num;
//		for(int j = 0; j < bmpHeight; j++) {
//			for(int i = 0; i < bmpWidth; i++) {
//				num = bmp.getPixel(i, j);
//				if(num == Color.rgb(255, 255, 255)) {
//					duoValue[i][j] = 0;
//				} else if(num == Color.rgb(0, 0, 0)) {
//					duoValue[i][j] = 1;
//				}
//			}
//		}
//		return duoValue;
//	}
	
}



















