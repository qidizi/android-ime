package qidizi.ime;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.DeadObjectException;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

public class KeyBoradView extends View  {
	/**
	 * 计算得到的单键连长
	 */
	private static int keySize;
	/**
	 * 行数
 	 */
	private static final  int rows  = 5;
	/**
	 * 列数
	 */
	private static final int cells = 10;
	/**
	 * 中间大字textsize
	 */
	private static int centerFontSize = 0;
	/**
	 * 边上的字textsize
	 */
	private static int sideFontSize = 0;

	/**
	 * 字
	 * @param context
	 */

	public KeyBoradView(Context context) {
		super(context);
		this.setBackgroundColor(Color.BLACK);
	}


	/**
	 * 自定义view大小
	 * @param widthMeasureSpec
	 * @param heightMeasureSpec
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 上面二个值是建议，最大值
        int width  = MeasureSpec.getSize(widthMeasureSpec);
        keySize = Math.round(width / cells);
        _getFontSize();
		// 通知父类，新的大小
		setMeasuredDimension(width, keySize * rows);
	}

	/**
	 * 画出键盘
	 * @param canvas
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// 抗锯齿
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.LTGRAY);
		int height = getHeight();
		int width = getWidth();

		// 画坚线
		for (int i = 1; i < cells; i++) {
			canvas.drawLine(keySize * i,0,keySize * i,height,paint);
		}

		// 画横线
		for (int i = 1; i < rows; i++) {
			canvas.drawLine(0,i * keySize,width,i * keySize,paint);
		}

        paint.setColor(Color.RED);
        canvas.drawRect(5,2,100,100,paint);
		// 画字,x->为正；y↓为正；view的左上角为（0，0）

		// 每键4角 + 4边 + 1中间 = 9字
		int sum = cells * rows * 9;
		String str =  "0";
		paint.setTypeface(Typeface.MONOSPACE);
		// drawtext的x指字中心离原点距离，y却是指字底边到原点距离（并非中心）
		paint.setTextAlign(Paint.Align.CENTER);
		Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "keys.ttf");
		paint.setTypeface(typeface);
		paint.setColor(Color.WHITE);
        // 字中心点大概到y点，大概是0.4个字高
        double x = 0,y = 0, baseLine = 0.4;

		for (int row = 1; row <= rows; row++) {
			for (int cell = 1; cell <= cells; cell++) {
				for (int i = 1; i <= 9; i++) {
					// 键字顺序为左角，上边，右角，右边，右下角，下边，左下角，中间
					str = "" + i;
                    paint.setTextSize(sideFontSize);

					switch (i){
						case 1:
						    // x右移半个字
							x = keySize * (cell - 1) + sideFontSize * 0.5;
                            // y，中心点下移半个字高
							y = keySize * (row -1) + sideFontSize * (0.5 + baseLine);
							break;
						case 2:
                            x = keySize * (cell - 1 + 0.5) + sideFontSize * 0.5;
                            // y，中心点下移半个字高
                            y = keySize * (row -1) + sideFontSize * (0.5 + baseLine);
							break;
						case 3:
							x = keySize * cell - sideFontSize * 0.5;
                            // y，中心点下移半个字高
                            y = keySize * (row -1) + sideFontSize * (0.5 + baseLine);
							break;
						case 4:
                            x = keySize * cell - sideFontSize * 0.5;
                            y = keySize * (row -1 + 0.5);
							break;
						case 5:
                            x = keySize * cell - sideFontSize * 0.5;
							y = keySize * row - sideFontSize * 05;
							break;
						case 6:
                            x = keySize * (cell - 1 + 0.5) + sideFontSize * 0.5;
                            y = keySize * row - sideFontSize * 05;
							break;
						case 7:
                            x = keySize * (cell - 1) + sideFontSize * 0.5;
                            y = keySize * row - sideFontSize * 05;
							break;
						case 8:
                            x = keySize * (cell - 1) + sideFontSize * 0.5;
                            y = keySize * (row -1 + 0.5);
							break;
						case 9:
						    // 中间那个字
                            paint.setTextSize(centerFontSize);
                            // 半个键
							x = keySize * (cell - 1 + 0.5);
                            // 向下移半个字高的4/10
							y = keySize * (row - 1 + 0.5) + centerFontSize * baseLine;
							break;
					}


					if (
							(1 == row && i < 4)
									// 第一行，上边无字
									||
									(1 == cell && (1 == i || 7 == i || 8 == i))
									// 第一列左边
									||
									(5 == row && i >= 5 && i <= 7)
									// 最后行下边
									||
									(cell == cells && i >= 3 && i <= 5)
						// 最右列右边
							) {
						str =  "\0";
					}

					// x在笔刷初始时已经设定成中心为0点;
					canvas.drawText(str, (float)x, (float)y, paint);
				}
			}
		}

		paint = null;// 释放
	}

	/**
	 * 计算键位上边与中心字大小
	 * 一般字体都是高比宽大，所以，只需要考虑高度合适即可
	 */
	private void _getFontSize () {
		//目前不清楚机制，如果已经设置了，就没有必要再重新计算
		if (centerFontSize > 0) {
			return;
		}

		// 在26个字母拼写中，小写f占用4行
		String s = "f";
		// 中心那个字的高度占整个键比例
		double bigHeight = 0.5;
		Paint p = new Paint();
		Rect bounds = new Rect();
		int fontHeight = (int)Math.round(keySize * bigHeight);
		int height = 0;

		// 先计算中间字高度
		for (int size = 1; fontHeight > height; size++) {
			// 不停的调整size，计算单字高度
			p.setTextSize(size);
			p.getTextBounds(s, 0, s.length(), bounds);
			int tmp = bounds.height();

			if (tmp > fontHeight) {
				// 已经超出了，就取前面的值
				break;
			}

			height = tmp;
		}

		centerFontSize = height;
		// 重设
		height = 0;
        fontHeight = (int)Math.round(keySize * (1 - bigHeight) / 2);


        // 先计算边上字高度
		for (int size = 1; fontHeight > height; size++) {
			// 不停的调整size，计算单字高度
			p.setTextSize(size);
			p.getTextBounds(s, 0, s.length(), bounds);
			int tmp = bounds.height();

			if (tmp > fontHeight) {
				// 已经超出了，就取前面的值
				break;
			}

			height = tmp;
		}

		sideFontSize = height;
        p = null;
	}

	/**
	 * 按键只需要接click；和移动
	 * @param event
	 * @return
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (1 != event.getPointerCount()) {
			// 多个手指，当做手势处理
			return true;
		}

		String str = "key:" + MotionEvent.actionToString(event.getAction());
		str += " (" + event.getX() + "," + event.getY() + ")";
		//str += " " + 	TextUtils.join(",",char5);
		Debug.show(str);
		return true;
	}


}
