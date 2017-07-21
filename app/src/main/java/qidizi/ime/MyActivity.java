package qidizi.ime;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.util.*;

public class MyActivity extends Activity
{
    /**
     * 计算得到的单键连长
     */
    private static int keySize;
    /**
     * 大屏时，最大键边长
     */
    private final static int maxKeySize = 50;
    /**
    最小的屏幕，计算出边长达不到时拒绝使用
    */
    private final static int minKeySize = 30;

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

    private EditText editText;


    /**
     * 画出键盘
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addView();
    }

    private void addView()
    {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int scnheight = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;




        LinearLayout ll = new LinearLayout(this);
        // 上下排列
        ll.setOrientation(LinearLayout.VERTICAL);
        keySize = Math.round(width / cells);
        // 通过callback的方式来画键盘
        View board = new View(this){
            @Override
            protected void onDraw(Canvas canvas)
            {
                super.onDraw(canvas);
                // 调用外面的方法
                drawBoard(canvas, this);
            }

            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
            {
                Log.i("", width + "  kkkk " + keySize);
                setMeasuredDimension(width, keySize * rows);
            }
        };
        ll.addView(board);

        editText = new EditText(this);
        editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    ((EditText)v).setText("");
                }

            });

        ll.addView(editText);
        setContentView(ll);
    }
    private void tip(String str)
    {
        //  Log.i("", str);
        editText.setText(str + editText.getText());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        String str = MotionEvent.actionToString(ev.getAction())  + " (" +
            ev.getX() + ',' + ev.getY() + ')';
        tip(str);
        return true;
    }

    /**
     * 画键盘
     * @param canvas
     */
    final public void drawBoard(Canvas canvas, View view)
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int scnheight = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;
        //final int width = getWindow().getDecorView().getWidth();
        view.setBackgroundColor(Color.BLACK);
        _getFontSize();
        // 抗锯齿
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.LTGRAY);
        int height = keySize * rows;

        // 画坚线
        for (int i = 1; i < cells; i++)
        {
            canvas.drawLine(keySize * i, 0, keySize * i, height, paint);
        }

        // 画横线
        for (int i = 1; i < rows; i++)
        {
            canvas.drawLine(0, i * keySize, width, i * keySize, paint);
        }

        // 画字,x->为正；y↓为正；view的左上角为（0，0）

        // 每键4角 + 4边 + 1中间 = 9字
        String str =  "0";
        paint.setTypeface(Typeface.MONOSPACE);
        // drawtext的x指字中心离原点距离，y却是指字底边到原点距离（并非中心）
        paint.setTextAlign(Paint.Align.CENTER);
        Typeface typeface = Typeface.createFromAsset(this.getBaseContext().getAssets(), "keys.ttf");
        paint.setTypeface(typeface);
        paint.setColor(Color.WHITE);
        // 字中心点大概到y点，大概是0.4个字高
        double x = 0,y = 0, baseLine = 0.3;

        for (int row = 1; row <= rows; row++)
        {
            for (int cell = 1; cell <= cells; cell++)
            {
                for (int i = 1; i <= 9; i++)
                {
                    // 键字顺序为左角，上边，右角，右边，右下角，下边，左下角，中间
                    str = "" + i;
                    paint.setTextSize(sideFontSize);
                    paint.setColor(Color.GRAY);

                    switch (i)
                    {
                        case 1:
                            // x右移半个字
                            x = keySize * (cell - 1) + sideFontSize * 0.5;
                            // y，中心点下移半个字高
                            y = keySize * (row - 1) + sideFontSize * (0.5 + baseLine);
                            break;
                        case 2:
                            x = keySize * (cell - 1 + 0.5);
                            // y，中心点下移半个字高
                            y = keySize * (row - 1) + sideFontSize * (0.5 + baseLine);
                            break;
                        case 3:
                            x = keySize * cell - sideFontSize * 0.5;
                            // y，中心点下移半个字高
                            y = keySize * (row - 1) + sideFontSize * (0.5 + baseLine);
                            break;
                        case 4:
                            x = keySize * cell - sideFontSize * 0.5;
                            y = keySize * (row - 1 + 0.5) + sideFontSize * baseLine;
                            break;
                        case 5:
                            x = keySize * cell - sideFontSize * 0.5;
                            y = keySize * row - sideFontSize * baseLine;
                            break;
                        case 6:
                            x = keySize * (cell - 1 + 0.5);
                            y = keySize * row - sideFontSize * baseLine;
                            break;
                        case 7:
                            x = keySize * (cell - 1) + sideFontSize * 0.5;
                            y = keySize * row - sideFontSize * baseLine;
                            break;
                        case 8:
                            x = keySize * (cell - 1) + sideFontSize * 0.5;
                            y = keySize * (row - 1 + 0.5) + sideFontSize * baseLine;
                            break;
                        case 9:
                            // 中间那个字
                            paint.setColor(Color.WHITE);
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
                        )
                    {
                        str =  "\0";
                    }

                    // x在笔刷初始时已经设定成中心为0点;
                    canvas.drawText(str, (float)x, (float)y + 3, paint);
                }
            }
        }

        paint = null;// 释放
    }

    /**
     * 计算键位上边与中心字大小
     * 一般字体都是高比宽大，所以，只需要考虑高度合适即可
     */
    private void _getFontSize()
    {
        //目前不清楚机制，如果已经设置了，就没有必要再重新计算
        if (centerFontSize > 0)
        {
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
        for (int size = 1; fontHeight > height; size++)
        {
            // 不停的调整size，计算单字高度
            p.setTextSize(size);
            p.getTextBounds(s, 0, s.length(), bounds);
            int tmp = bounds.height();

            if (tmp > fontHeight)
            {
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
        for (int size = 1; fontHeight > height; size++)
        {
            // 不停的调整size，计算单字高度
            p.setTextSize(size);
            p.getTextBounds(s, 0, s.length(), bounds);
            int tmp = bounds.height();

            if (tmp > fontHeight)
            {
                // 已经超出了，就取前面的值
                break;
            }

            height = tmp;
        }

        sideFontSize = height;
        p = null;
    }
}
