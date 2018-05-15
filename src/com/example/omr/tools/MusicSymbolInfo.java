package com.example.omr.tools;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import com.example.omr.struct.Hrun;
import com.example.omr.struct.Vrun;
import com.example.omr.system.BarLine;
import com.example.omr.system.Chord;
import com.example.omr.system.Clef;
import com.example.omr.system.KeySign;
import com.example.omr.system.Measure;
import com.example.omr.system.MusicSystem;
import com.example.omr.system.Note;
import com.example.omr.system.Stave;
import com.example.omr.system.TimeSign;

import android.graphics.Bitmap;
import android.graphics.Color;

public class MusicSymbolInfo {
	
	private static ArrayList<Integer> staffPositArray;
	private static int staffW;
	private static int staffSpace;
	
//	private static ArrayList<Vrun> vruns;
//	private static ArrayList<Hrun> hruns;
	
	private static void setAttribute() {
		staffPositArray = MusicLineInfo.getStaffPositArray();
		staffW = MusicLineInfo.getStaffW();
		staffSpace = MusicLineInfo.getStaffSpace();
	}

	// 测试
	public static ArrayList<Bitmap> get(Bitmap bmp) {
		rebuildTabs(bmp);
		
		ArrayList<Bitmap> bmps = cuttingBmp(bmp);
		bmps = getSingleSymbol(bmps.get(0));
		analysisChord(setVruns(bmps.get(0)));
		
		return bmps;
	}
	
	/**
	 * 建立谱表
	 * @param bmp
	 * @return
	 */
	public static MusicSystem rebuildTabs(Bitmap bmp) {
		setAttribute();
		LinkedList<Stave> staves = new LinkedList<Stave>(); // 五线谱链表
		
		ArrayList<Bitmap> bmps = cuttingBmp(bmp);
		ArrayList<Bitmap> singleSymbols = null;// 存储单个符号
		int clefCount = 0;
		for(Bitmap b : bmps) {
			clefCount++;
			if(clefCount % 2 == 1) {
				LinkedList<Measure> measures = new LinkedList<Measure>(); // 小节链表
				LinkedList<Chord> chords = new LinkedList<Chord>(); // 音符链表
				singleSymbols = getSingleSymbol(b);
				boolean flag = false;
				int i = 0;
				for(Bitmap symbol : singleSymbols) {
					ArrayList<Vrun> vruns = setVruns(symbol);
					if(flag) {
						i++;
						if(i > 2) {
							String isBarLine = analysisBarLine(symbol, vruns);
							if(isBarLine == null) {
								ArrayList<Chord> cs = analysisChord(vruns);
								chords.addAll(cs);
							} else {
								BarLine barLine = new BarLine(isBarLine, 0, 0);
								Clef clef = null;
								if(clefCount % 2 == 1) {
									clef = new Clef(Clef.TREBLE, 0, 0);
								} else {
									clef = new Clef(Clef.BASS, 0, 0);
								}
								KeySign keySign = new KeySign(0, 0, 0);
								TimeSign timeSign = new TimeSign(4, 4, 0, 0);
								Measure measure = new Measure(clef, keySign, timeSign, chords, barLine);
								measures.add(measure);
								chords = new LinkedList<Chord>();
							}
						}
					} else if(!flag) {
						if(analysisBarLine(symbol, vruns) != null) {
							flag = true;
						}
					}
					
				}
				if(chords.size() != 0) {
					BarLine barLine = new BarLine(BarLine.SINGLE, 0, 0);
					Clef clef = null;
					if(clefCount % 2 == 1) {
						clef = new Clef(Clef.TREBLE, 0, 0);
					} else {
						clef = new Clef(Clef.BASS, 0, 0);
					}
					KeySign keySign = new KeySign(0, 0, 0);
					TimeSign timeSign = new TimeSign(4, 4, 0, 0);
					Measure measure = new Measure(clef, keySign, timeSign, chords, barLine);
					measures.add(measure);
				}
				
				int w = b.getWidth();
				int h = b.getHeight();
				Stave stave = new Stave(0, 0, w, h, measures);
				staves.add(stave);
			}
		}
		// 返回值为谱表
		MusicSystem ms = new MusicSystem(0, 0, bmp.getWidth(), bmp.getHeight(), staves);
		ArrayList<Chord> cc = new ArrayList<Chord>();
		cc.addAll(ms.getStaves().get(0).getMeasures().get(0).getChords());
		return ms;
	}
	
	private static void analysisSymbol(ArrayList<Bitmap> bmps) {
	}
	
	/**
	 * 计算符头的谱线位置
	 * @param posit
	 * @return
	 */
	private static int getPos(int posit) {
		int pos0 = staffSpace * 6 + staffW * 2 + 1;
		int c = (staffSpace + 1) / 4;
		if(posit > pos0 - c && posit < pos0 + c) {
			return 0;
		} else if(posit >= pos0 + c && posit <= pos0 + c * 3) {
			return 1;	
		}else if(posit <= pos0 - c && posit >= pos0 - c * 3) {
			return -1;	
		} else if(posit >= pos0 + c * 3 && posit <= pos0 + c * 5) {
			return 2;	
		} else if(posit <= pos0 - c * 3 && posit >= pos0 - c * 5) {
			return -2;	
		} else if(posit >= pos0 + c * 5 && posit <= pos0 + c * 7) {
			return 3;	
		} else if(posit <= pos0 - c * 5 && posit >= pos0 - c * 7) {
			return -3;	
		} else if(posit >= pos0 + c * 7 && posit <= pos0 + c * 9) {
			return 4;	
		} else if(posit <= pos0 - c * 7 && posit >= pos0 - c * 9) {
			return -4;	
		} else if(posit >= pos0 + c * 9 && posit <= pos0 + c * 11) {
			return 5;	
		} else if(posit <= pos0 - c * 9 && posit >= pos0 - c * 11) {
			return -5;	
		} else if(posit >= pos0 + c * 11 && posit <= pos0 + c * 13) {
			return 6;	
		} else if(posit <= pos0 - c * 11 && posit >= pos0 - c * 13) {
			return -6;	
		} else if(posit >= pos0 + c * 13 && posit <= pos0 + c * 15) {
			return 7;	
		} else if(posit <= pos0 - c * 13 && posit >= pos0 - c * 15) {
			return -7;	
		} else {
			return 0;
		}
	}
	
	/**
	 * 识别符头
	 * @param vruns
	 * @return
	 */
	private static int discoverNote(ArrayList<Vrun> vruns) {
		int h = 0;
		int w = 0;
		for(Vrun v : vruns) {
			h = v.getEndRow() - v.getBegRow();
			if(h >= staffSpace - 1) {
				Map<Integer, Hrun> hruns = v.getHruns();
				for(Map.Entry<Integer, Hrun> entry : hruns.entrySet()) {
					w = entry.getValue().getLength();
					if(w > staffSpace) {
						return (v.getBegRow() + v.getEndRow()) / 2;
					}
				}
			}
		}
		return -1;
	}
	
	/**
	 * 分析乐符
	 * @param bmp
	 * @return
	 */
	private static ArrayList<Chord> analysisChord(ArrayList<Vrun> vruns) {
		ArrayList<Chord> chords = new ArrayList<Chord>();
		ArrayList<Vrun> delStems = new ArrayList<Vrun>();
		ArrayList<Vrun> stems = getStem(vruns);
		for(int i = 1; i < stems.size(); i++) {
			if(stems.get(i).getCol() == stems.get(i - 1).getCol() + 1 
					|| stems.get(i).getCol() == stems.get(i - 1).getCol() - 1) {
				delStems.add(stems.get(i));
			}
		}
		stems.removeAll(delStems);
		
		Chord chord = null;
		if(stems.size() == 0) {
			int posit = discoverNote(vruns);
			if(posit != -1) {
				boolean virtualStem = true;// 标记音符有无符干
				boolean stemUp = false;// 标记符干方向(true:向上 false:向下)
				int naugdots = 0;// 记录附点数
				int nFlags = 0;// 记录符尾个数
				int stemX = 0, stemY = 0, stemEnd = 0;// 记录符干位置
				LinkedList<Note> notes = new LinkedList<Note>();// 符头链表
				
				Note note = null;
				String shape = "s";// 记录符头形状(实心或空心)和休止符类型
				int pos = getPos(posit);// 记录符头的谱线位置
				String accid = null;// 记录变音记号类型(升音记号、降音记号、还原记号)
				int dis = 0;// 记录变音记号位置
				note = new Note(shape, pos, accid, dis);
				notes.addLast(note);
				
				chord = new Chord(virtualStem, stemUp, naugdots, nFlags, stemX, stemY, stemEnd, notes);
				chords.add(chord);
			}
		} else {
			ArrayList<Vrun> vs = new ArrayList<Vrun>();
			for(Vrun stem : stems) {
				vs.clear();
				for(Vrun run : vruns) {
					if(run.getCol() >= stem.getCol() - staffSpace * 2 
							&& run.getCol() <= stem.getCol() + staffSpace * 2) {
						vs.add(run);
					}
				}
				vs.removeAll(stems);
				vs.removeAll(delStems);
				int posit = discoverNote(vs);
				if(posit != -1) {
					boolean virtualStem = false;// 标记音符有无符干
					boolean stemUp;// 标记符干方向(true:向上 false:向下)
					if(posit > (stem.getBegRow() + stem.getEndRow()) / 2) {
						stemUp = false;
					} else {
						stemUp = true;
					}
					int naugdots = 0;// 记录附点数
					int nFlags = 0;// 记录符尾个数
					for(Vrun v : vruns) {
						if(v.getCol() == stem.getCol() - (staffSpace + 1) / 2 
								|| v.getCol() == stem.getCol() + (staffSpace + 1) / 2) {
							if(v.getEndRow() - v.getBegRow() < staffSpace / 2 + staffW
									&& v.getEndRow() - v.getBegRow() > staffW) {
								nFlags++;
							}
						}
					}
					int stemX = stem.getCol();
					int stemY = stem.getBegRow();
					int stemEnd = stem.getEndRow();// 记录符干位置
					LinkedList<Note> notes = new LinkedList<Note>();// 符头链表
					
					Note note = null;
					String shape = "s";// 记录符头形状(实心或空心)和休止符类型
					int pos = getPos(posit);// 记录符头的谱线位置
					String accid = null;// 记录变音记号类型(升音记号、降音记号、还原记号)
					int dis = 0;// 记录变音记号位置
					note = new Note(shape, pos, accid, dis);
					notes.addLast(note);
					
					chord = new Chord(virtualStem, stemUp, naugdots, nFlags, stemX, stemY, stemEnd, notes);
					chords.add(chord);
				}
			}
		}
		return chords;
	}
	
	/**
	 * 分析小节线
	 * @param vruns
	 * @return
	 */
	private static String analysisBarLine(Bitmap bmp, ArrayList<Vrun> vruns) {
		if(bmp.getWidth() <= staffW * 3) {
			for(Vrun run : vruns) {
				int length = run.getEndRow() - run.getBegRow();
				if(length > staffSpace * 4) {
					return BarLine.SINGLE;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * 分析谱号
	 * @param bmp
	 * @return
	 */
	private static String analysisClef(Bitmap bmp, ArrayList<Vrun> vruns) {
		return null;
	}
	
	/**
	 * 分析调号
	 * @param bmp
	 * @return
	 */
	private static String analysisKeySign(Bitmap bmp, ArrayList<Vrun> vruns) {
		return null;
	}
	
	/**
	 * 分析拍号
	 * @param bmp
	 * @return
	 */
	private static String analysisTimeSign(Bitmap bmp, ArrayList<Vrun> vruns) {
		return null;
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
							if(count > staffW * 2 || i - l >= staffW * 1) {
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
		try {
			for(int i = 0; i < count; i++) {
				int x = 0;
				int y = staffPositArray.get(i * 5) - staffSpace * 4;
//				// 判断顶部是否足够4个间
//				if(y < 0) {
//					y = 0;
//				}
				int w = width;
				int h = staffPositArray.get(i * 5 + 4) + staffSpace * 4 - y; // 截取高度为五线上加四间到下加四间
//				// 判断底部是否足够4个间
//				int min = height - 1 - y;
//				if(min < h) {
//					h = min;
//				}
				b = Bitmap.createBitmap(bmp, x, y, width, h, null, false);
				bmps.add(b);
			}
		} catch(Exception e) {
			
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
	
	/**
	 * 获取符干基元
	 * @param runs
	 * @return
	 */
	private static ArrayList<Vrun> getStem(ArrayList<Vrun> vruns) {
		ArrayList<Vrun> candRuns = new ArrayList<Vrun>();
		ArrayList<Vrun> stemRuns = new ArrayList<Vrun>();
		for(Vrun run : vruns) {
			int length = run.getEndRow() - run.getBegRow() + 1;
			if(length > 2 * staffSpace && length < 8 * staffSpace) {
				candRuns.add(run);
			}
		}
		for(Vrun run : candRuns) {
			int flag = 0;
			for(int r = run.getBegRow(); r <= run.getEndRow(); r++) {
				Hrun hrun = run.getHruns().get(r);
				if(hrun.getLength() < 3 * staffW) {
					int left = hrun.getLeft();
					int right = hrun.getRight();
					int col = run.getCol();
					int alpha = staffW * 3 / 2 + 1;
					if(Math.abs(col -  left) < alpha 
							&& Math.abs(col - right) < alpha 
							&& Math.abs(left / 2 + right / 2 -col) < alpha) {
						flag++;
					} else {
						flag = 0;
					}
				} else {
					flag = 0;
				}
				if(flag == staffSpace) {
					stemRuns.add(run);
					break;
				}
			}
		}
		
		return stemRuns;
	}

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
	
	/**
	 * 对截取的单个符号作垂直游程编码
	 * 并且每个垂直游程上的点作水平游程编码
	 * @param bmp 删除谱线后截取的单个图像
	 * @return
	 */
	private static ArrayList<Vrun> setVruns(Bitmap bmp) {
		int bmpWidth = bmp.getWidth();
		int bmpHeight = bmp.getHeight();
		ArrayList<Vrun> vruns = new ArrayList<Vrun>();
		for(int i = 0; i < bmpWidth; i++) {
			Vrun run = null;
			Hrun hrun = null;
			if(bmp.getPixel(i, 0) == Color.rgb(0, 0, 0)) {
				run = new Vrun();
				run.setCol(i);
				run.setBegRow(0);
				run.setEndRow(0);
				// 
				hrun = new Hrun();
				hrun.setRow(0);
				hrun.setLeft(i);
				hrun.setRight(i);
				int col = i;
				while(--col >=0 && bmp.getPixel(col, 0) == Color.rgb(0, 0, 0)) {
					hrun.setLeft(col);
				}
				col = i;
				while(++col < bmpWidth && bmp.getPixel(col, 0) == Color.rgb(0, 0, 0)) {
					hrun.setRight(col);
				}
				hrun.countLength();
				run.getHruns().put(0, hrun);
			}
			for(int j = 1; j < bmpHeight; j++) {
				if(bmp.getPixel(i, j) == Color.rgb(0, 0, 0) 
						&& bmp.getPixel(i, j - 1) == Color.rgb(0, 0, 0)) {
					run.setEndRow(j);
					// 
					hrun = new Hrun();
					hrun.setRow(j);
					hrun.setLeft(i);
					hrun.setRight(i);
					int col = i;
					while(--col >=0 && bmp.getPixel(col, j) == Color.rgb(0, 0, 0)) {
						hrun.setLeft(col);
					}
					col = i;
					while(++col < bmpWidth && bmp.getPixel(col, j) == Color.rgb(0, 0, 0)) {
						hrun.setRight(col);
					}
					hrun.countLength();
					run.getHruns().put(j, hrun);
					if(j == bmpHeight - 1 && run != null) {
						vruns.add(run);
					}
				} else if(bmp.getPixel(i, j) == Color.rgb(0, 0, 0) 
						&& bmp.getPixel(i, j - 1) == Color.rgb(255, 255, 255)) {
					run = new Vrun();
					run.setCol(i);
					run.setBegRow(j);
					run.setEndRow(j);
					// 
					hrun = new Hrun();
					hrun.setRow(j);
					hrun.setLeft(i);
					hrun.setRight(i);
					int col = i;
					while(--col >=0 && bmp.getPixel(col, j) == Color.rgb(0, 0, 0)) {
						hrun.setLeft(col);
					}
					col = i;
					while(++col < bmpWidth && bmp.getPixel(col, j) == Color.rgb(0, 0, 0)) {
						hrun.setRight(col);
					}
					hrun.countLength();
					run.getHruns().put(j, hrun);
				} else if(bmp.getPixel(i, j) == Color.rgb(255, 255, 255) 
						&& bmp.getPixel(i, j - 1) == Color.rgb(0, 0, 0)) {
					vruns.add(run);
				}
			}
		}
		return vruns;
	}
	
}









