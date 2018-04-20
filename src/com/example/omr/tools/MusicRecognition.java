package com.example.omr.tools;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;

public class MusicRecognition {
	
	private static Bitmap img;
	private static int imgWidth;
	private static int imgHeight;
	private static int[] imgPixels;
	
	private static final int STRIP_WIDTH = 32;

	private static void setImgInfo(Bitmap bmp) {
		img = bmp;
		imgWidth = img.getWidth();
		imgHeight = img.getHeight();
		imgPixels = new int[imgWidth * imgHeight];
		img.getPixels(imgPixels, 0, imgWidth, 0, 0, imgWidth, imgHeight);
	}
	
	/**
	 * 测试
	 * @param bmp
	 * @return
	 */
	public static String getSomeone(Bitmap bmp) {
		setImgInfo(bmp);
		String str = "";
		return str;
	}

	/**
	 * 对图像进行倾斜矫正
	 * @param bitmap
	 * @return
	 */
	private static int[] tiltCorrection(Bitmap image) {
		// TODO:
		setImgInfo(image);
		int[] parms = calculateRange();
		int k = imgWidth / STRIP_WIDTH;
		// offset 存储各个区域相对于左端区域的相对偏移量
		int [] offset = new int[k];
		for(int os : offset) {
			os = 0;
		}
		
		// 获取各个区域的水平投影直方图
		int [][] Pk = new int[k][];
		for(int n = 0; n < k; n++) {
			int[] P = new int[imgHeight];
			for(int j = 0; j < imgHeight; j++) {
				for(int i = n * STRIP_WIDTH; i < (n + 1) * STRIP_WIDTH; i++) {
					if(img.getPixel(i, j) == Color.rgb(255, 255, 255)) {
						P[j]++;
					}
				}
			}
			Pk[n] = P;
		}
		
		int[] C = new int[2 * parms[1]];
		int[] P_0 = new int[imgHeight];
		// 计算各个区域的水平投影队列相对于基准的交叉相关性
		for(int n = 1; n < k; n++) {
			// 计算校正基准P'0(j)
			for(int j = 0; j < imgHeight; j++) {
				for(int p = 1; p <= n; p++) {
					P_0[j] += Pk[p-1][j + offset[p-1]];
				}
			}
			
			for(int l = 0; l > -parms[1] && l < parms[1]; l++) {
				for(int j = parms[1]; j < imgHeight - parms[1]; j++) {
					C[l] = Pk[n][j + l + offset[n - 1]] * P_0[j];
				}
			}
			
			int max_l = 0;
			int max = 0;
			for(int m = 0; m < C.length; m++) {
				if(C[m] > max) {
					max = C[m];
					max_l = m - parms[1];
				}
			}
			offset[n] = offset[n - 1] + max_l;
		}
		
		return offset;
	}
	
	/**
     * 图像旋转
     * @param img 原图
     * @param alpha  旋转角度，可正可负
     * @return 旋转后的图片
     */
	private static Bitmap rotateBitmap(Bitmap bmp, float alpha) {
        if (bmp == null) {
            return null;
        }
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(alpha);
        // 围绕原地进行旋转
        Bitmap newBM = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, false);
        if (newBM.equals(bmp)) {
            return newBM;
        }
        bmp.recycle();
        return newBM;
    }
	
	/**
	 * 预计算线宽和间距的平均值
	 */
	private static int[] calculateRange() {
		ArrayList<ArrayList<Integer>> arrs = runLengthCoding();
		int count_0 = 0;
		int number_0 = 0;
		int count_1 = 0;
		int number_1 = 0;
		for(int i = 1; i < arrs.size(); i++) {
			if(arrs.get(i).get(0) == 0) {
				count_0++;
				number_0 += arrs.get(i).get(1);
			} else if(arrs.get(i).get(0) == 1) {
				count_1++;
				number_1 += arrs.get(i).get(1);
			}
		}
		int[] parms = new int[2];
		parms[0] = number_0 / count_0;
		parms[1] = number_1 / count_1;
		return parms;
	}
	
	// 对图像水平方向最中间垂线最为进行垂直游程编码
	private static ArrayList<ArrayList<Integer>> runLengthCoding() {
		// TODO:
		ArrayList<ArrayList<Integer>> arrs = new ArrayList<ArrayList<Integer>>();
		int i = imgWidth / 2;
		int count = 1;
		int num = 0;
		for(int j = 1; j < imgHeight; j++) {
			if(img.getPixel(i, j) != img.getPixel(i, j - 1)) {
				if(img.getPixel(i, j) == Color.rgb(255, 255, 255)) {
					ArrayList<Integer> arr = new ArrayList<Integer>();
					arr.add(0, 0);
					arr.add(1, count);
					arrs.add(arr);
					count = 1;
				}
				if(img.getPixel(i, j) == Color.rgb(0, 0, 0)) {
					ArrayList<Integer> arr = new ArrayList<Integer>();
					arr.add(0, 1);
					arr.add(1, count);
					arrs.add(arr);
					count = 1;
				}
			} else {
				count++;
			}
		}
		
		return arrs;
	}
}



















