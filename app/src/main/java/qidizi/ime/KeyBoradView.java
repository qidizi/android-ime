package qidizi.ime;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
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
		keySize = getWidth() / cells;
		// 通知父类，新的大小
		setMeasuredDimension(widthMeasureSpec, keySize * rows);
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

		// 画字

		// 每键4角 + 4边 + 1中间 = 9字
		int sum = cells * rows * 9;
		String str =  "0";
		// paint.setStyle(Paint.Style.STROKE);
		paint.setTypeface(Typeface.MONOSPACE);
		// 让x=0是字中间处于viewleft边上，正数是右移
		paint.setTextAlign(Paint.Align.CENTER);
		Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "keys.ttf");
		paint.setTypeface(typeface);
		paint.setColor(Color.WHITE);
		int fontHeight = (int) (paint.descent() - paint.ascent());
		// 当y=0 => yb=y+abs(baseline - 字高/2);
		int baseline = Math.abs((int) paint.ascent());

		for (int row = 1; row <= rows; row++) {
			for (int cell = 1; cell <= cells; cell++) {
				for (int i = 1; i <= 9; i++) {
					// 键字顺序为左角，上边，右角，右边，右下角，下边，左下角，中间
					str = "" + i;

					int x = 0;
					int y = 0;

					switch (i){
						case 1:
							x = keySize * (cell - 1);
							y = keySize * (row -1);
							break;
						case 2:
							x = (int)( keySize * (cell - 1 + 0.5) );
							y = keySize * (row - 1);
							break;
						case 3:
							x = keySize * cell - fontHeight / 2;
							y = keySize * (row - 1);
							break;
						case 4:
							x = keySize * cell - fontHeight / 2;
							y = (int)( keySize * (row - 1 + 0.5) );
							break;
						case 5:
							x = keySize * cell - fontHeight / 2;
							y = keySize * row - fontHeight;
							break;
						case 6:
							x = (int)( keySize * (cell - 1 + 0.5) );
							y = keySize * row - fontHeight;
							break;
						case 7:
							x = keySize * (cell - 1);
							y = keySize * row - fontHeight;
							break;
						case 8:
							x = keySize * (cell - 1);
							y = (int)( keySize * (row - 1 + 0.5) );
							break;
						case 9:
							x = (int)( keySize * (cell - 1 + 0.5) ) - fontHeight;
							y = (int)( keySize * (row - 1 + 0.5) ) - fontHeight;
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

					float baselineY = y + baseline - fontHeight / 2;
					// x在笔刷初始时已经设定成中心为0点;
					canvas.drawText(str, x, baselineY, paint);
				}
			}
		}

		paint = null;// 释放
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
