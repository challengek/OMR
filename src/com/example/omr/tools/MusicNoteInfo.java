package com.example.omr.tools;

import java.util.ArrayList;

import com.example.omr.struct.Hrun;
import com.example.omr.struct.Vrun;

import android.graphics.Bitmap;
import android.graphics.Color;

public class MusicNoteInfo {
	
	private static ArrayList<Integer> staffPositArray;
	private static int staffW;
	private static int staffSpace;
	
	private static ArrayList<Vrun> vruns;
	private static ArrayList<Hrun> hruns;
	
	private static void setAttribute() {
		staffPositArray = MusicLineInfo.getStaffPositArray();
		staffW = MusicLineInfo.getStaffW();
		staffSpace = MusicLineInfo.getStaffSpace();
	}

	// 测试
	public static ArrayList<Bitmap> get(Bitmap bmp) {
		setAttribute();
//		vruns = setVrunsAfterDelLine(bmp);
//		hruns = setHrunsAfterDelLine(bmp);
//		getSolidHead();
		ArrayList<Bitmap> bmps = cuttingBmp(bmp);
		bmps = getSingleSymbol(bmps.get(0));
		return bmps;
	}
	
	/**
	 * 获取单个的符号
	 * @param bmp
	 * @return
	 */
	private static ArrayList<Bitmap> getSingleSymbol(Bitmap bmp) {
		if(bmp == null) {
			return null;
		}
		int width = bmp.getWidth();
		int height = bmp .getHeight();
		
		ArrayList<Bitmap> bmps = new ArrayList<Bitmap>();
		Bitmap b = null;
		int l, r;
		boolean flag;
		int count;
		for(int i = 0; i < width; i++) {
			flag = true;
			for(int j = 0; j < height; j++) {
				if(bmp.getPixel(i, j) == Color.rgb(0, 0, 0)) {
					l = i;
					while(flag) {
						i++;
						count = 0;
						for(j = 0; j < height; j++) {
							if(bmp.getPixel(i, j) == Color.rgb(0, 0, 0)) {
								count++;
							}
						}
						if(count == 0) {
							if(count > staffW * 1 || i - l >= staffW * 1) {
								r = i;
								b = Bitmap.createBitmap(bmp, l, 0, r - l + 1, height);
								bmps.add(b);
							}
							flag = false;
						}
					}
				}
			}
		}
		
		return bmps;
	}
	
	/**
	 * 根据无限位置切割图片
	 * @param bmp
	 * @return
	 */
	private static ArrayList<Bitmap> cuttingBmp(Bitmap bmp) {
		if(bmp == null) {
			return null;
		}
		int width = bmp.getWidth();
		int height = bmp .getHeight();
		
		ArrayList<Bitmap> bmps = new ArrayList<Bitmap>();
		Bitmap b = null;
		int count = staffPositArray.size() / 5;
		for(int i = 0; i < count; i++) {
			int x = 0;
			int y = staffPositArray.get(i * 5) - staffSpace * 4;
//			// 判断顶部是否足够4个间
//			if(y < 0) {
//				y = 0;
//			}
			int w = width;
			int h = staffPositArray.get(i * 5 + 4) + staffSpace * 4 - y; // 截取高度为五线上加四间到下加四间
//			// 判断底部是否足够4个间
//			int min = height - 1 - y;
//			if(min < h) {
//				h = min;
//			}
			b = Bitmap.createBitmap(bmp, x, y, width, h, null, false);
			bmps.add(b);
		}
		
		return bmps;
	}
	
//	private static void getSolidHead() {
//		ArrayList<Vrun> vrs = new ArrayList<Vrun>();
//		ArrayList<Hrun> hrs = new ArrayList<Hrun>();
//		for(Vrun run : vruns) {
//			int length = run.getEndRow() - run.getBegRow() + 1;
//			if(length >= staffW * 2) {
//				vrs.add(run);
//			}
//		}
//		for(Hrun run : hruns) {
//			int length = run.getLength();
//			if(length >= staffW * 3) {
//				hrs.add(run);
//			}
//		}
//		
//		for(Vrun run : vrs) {
//			
//		}
//		return;
//	}
//	
//	/**
//	 * 获取符干基元
//	 * @param runs
//	 * @return
//	 */
//	private static ArrayList<Vrun> getStem() {
//		ArrayList<Vrun> candRuns = new ArrayList<Vrun>();
//		ArrayList<Vrun> stemRuns = new ArrayList<Vrun>();
//		for(Vrun run : vruns) {
//			int length = run.getEndRow() - run.getBegRow() + 1;
//			if(length > 2 * staffSpace && length < 8 * staffSpace) {
//				candRuns.add(run);
//			}
//		}
//		for(Vrun run : candRuns) {
//			int flag = 0;
//			for(int r = run.getBegRow(); r <= run.getEndRow(); r++) {
//				Hrun hrun = run.getHruns().get(r);
//				if(hrun.getLength() < 3 * staffW) {
//					int left = hrun.getLeft();
//					int right = hrun.getRight();
//					int col = run.getCol();
//					int alpha = staffW * 3 / 2 + 1;
//					if(Math.abs(col -  left) < alpha 
//							&& Math.abs(col - right) < alpha 
//							&& Math.abs(left / 2 + right / 2 -col) < alpha) {
//						flag++;
//					} else {
//						flag = 0;
//					}
//				} else {
//					flag = 0;
//				}
//				if(flag == staffSpace) {
//					stemRuns.add(run);
//					break;
//				}
//			}
//		}
//		// TODO: 最后作连通域判断，同一个连通域对应一个符干单元
//		
//		return stemRuns;
//	}
//	
//	/**
//	 * 删除 谱线后进行水平游程编码
//	 * @param bmp
//	 * @return
//	 */
//	private static ArrayList<Hrun> setHrunsAfterDelLine(Bitmap bmp) {
//		int bmpWidth = bmp.getWidth();
//		int bmpHeight = bmp.getHeight();
//		ArrayList<Hrun> hruns = new ArrayList<Hrun>();
//		for(int j = 0; j < bmpHeight; j++) {
//			Hrun hrun = null;
//			if(bmp.getPixel(0, j) == Color.rgb(0, 0, 0)) {
//				hrun = new Hrun();
//				hrun.setRow(j);
//				hrun.setLeft(0);
//				hrun.setRight(0);
//			}
//			for(int i = 1; i < bmpWidth; i++) {
//				if(bmp.getPixel(i, j) == Color.rgb(0, 0, 0) 
//						&& bmp.getPixel(i - 1, j) == Color.rgb(0, 0, 0)) {
//					hrun.setRight(i);
//				} else if(bmp.getPixel(i, j) == Color.rgb(0, 0, 0) 
//						&& bmp.getPixel(i - 1, j) == Color.rgb(255, 255, 255)) {
//					hrun = new Hrun();
//					hrun.setRow(j);
//					hrun.setLeft(i);
//					hrun.setRight(i);
//				} else if(bmp.getPixel(i, j) == Color.rgb(255, 255, 255) 
//						&& bmp.getPixel(i - 1, j) == Color.rgb(0, 0, 0)) {
//					hrun.countLength();
//					hruns.add(hrun);
//				}
//			}
//		}
//		return hruns;
//	}
//	
//	/**
//	 * 删除 谱线后重新进行垂直游程编码
//	 * 并且每个垂直游程上的点作水平游程编码
//	 * @param bmp 删除谱线后的图像
//	 * @return
//	 */
//	private static ArrayList<Vrun> setVrunsAfterDelLine(Bitmap bmp) {
//		int bmpWidth = bmp.getWidth();
//		int bmpHeight = bmp.getHeight();
//		ArrayList<Vrun> runs = new ArrayList<Vrun>();
//		for(int i = 0; i < bmpWidth; i++) {
//			Vrun run = null;
//			Hrun hrun = null;
//			if(bmp.getPixel(i, 0) == Color.rgb(0, 0, 0)) {
//				run = new Vrun();
//				run.setCol(i);
//				run.setBegRow(0);
//				run.setEndRow(0);
//				// 
//				hrun = new Hrun();
//				hrun.setRow(0);
//				hrun.setLeft(i);
//				hrun.setRight(i);
//				int col = i;
//				while(--col >=0 && bmp.getPixel(col, 0) == Color.rgb(0, 0, 0)) {
//					hrun.setLeft(col);
//				}
//				col = i;
//				while(++col < bmpWidth && bmp.getPixel(col, 0) == Color.rgb(0, 0, 0)) {
//					hrun.setRight(col);
//				}
//				hrun.countLength();
//				run.getHruns().put(0, hrun);
//			}
//			for(int j = 1; j < bmpHeight; j++) {
//				if(bmp.getPixel(i, j) == Color.rgb(0, 0, 0) 
//						&& bmp.getPixel(i, j - 1) == Color.rgb(0, 0, 0)) {
//					run.setEndRow(j);
//					// 
//					hrun = new Hrun();
//					hrun.setRow(j);
//					hrun.setLeft(i);
//					hrun.setRight(i);
//					int col = i;
//					while(--col >=0 && bmp.getPixel(col, j) == Color.rgb(0, 0, 0)) {
//						hrun.setLeft(col);
//					}
//					col = i;
//					while(++col < bmpWidth && bmp.getPixel(col, j) == Color.rgb(0, 0, 0)) {
//						hrun.setRight(col);
//					}
//					hrun.countLength();
//					run.getHruns().put(j, hrun);
//				} else if(bmp.getPixel(i, j) == Color.rgb(0, 0, 0) 
//						&& bmp.getPixel(i, j - 1) == Color.rgb(255, 255, 255)) {
//					run = new Vrun();
//					run.setCol(i);
//					run.setBegRow(j);
//					run.setEndRow(j);
//					// 
//					hrun = new Hrun();
//					hrun.setRow(j);
//					hrun.setLeft(i);
//					hrun.setRight(i);
//					int col = i;
//					while(--col >=0 && bmp.getPixel(col, j) == Color.rgb(0, 0, 0)) {
//						hrun.setLeft(col);
//					}
//					col = i;
//					while(++col < bmpWidth && bmp.getPixel(col, j) == Color.rgb(0, 0, 0)) {
//						hrun.setRight(col);
//					}
//					hrun.countLength();
//					run.getHruns().put(j, hrun);
//				} else if(bmp.getPixel(i, j) == Color.rgb(255, 255, 255) 
//						&& bmp.getPixel(i, j - 1) == Color.rgb(0, 0, 0)) {
//					runs.add(run);
//				}
//			}
//		}
//		return runs;
//	}
	
}









