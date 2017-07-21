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
import android.widget.TextView;
import android.widget.Toast;
import android.view.*;
import java.util.*;

public class MyActivity extends Activity {
    /**
     * 计算得到的单键连长
     */
    private static int keySize;
    /**
     * 大屏时，最大键边长
     */
    private final static int maxKeySize = 106;
    /**
     * 最小的屏幕，计算出边长达不到时拒绝使用
     */
    private final static int minKeySize = 30;

    /**
     * 行数
     */
    private static final int rows = 4;
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
    键0点到屏幕0点，与键字符对应关系
    */
    private static Map<String,String> keys;

    //
    private View board;

    // debug view
    private TextView view;
    // 用来缓存文字的集合
    private static String logs = "";
    
    // 键符,按照单个键显示字符，键内,左上>正上>右上>右>右下>下>左下>左>中，左键>右键>上行>下行对应
    // 不想使用位置使用\0占位
    private final static String chars = "1~@2#%3*-4_/5'(6)?7!:8;\"9,.0";


    /**
     * 画出键盘
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createBoard();
    }

    /**
     * 初始化键盘
     */
    private void createBoard() {
        if (keySize > 0) {
            // 已经计算过
            addView();
            return;
        }

        // 计算screen的边长，不理会窗口的
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // 代码上的高与实物的高没有联系
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        // 只取最小边来使用
        int minSide = Math.min(height, width);
        keySize = Math.round(minSide / cells);
        // 键边有一个上限
        keySize = Math.min(keySize, maxKeySize);

        if (keySize < minKeySize) {
            // 屏太小，无法正常使用
            Toast.makeText(this.getBaseContext(), "设备屏幕过小，输入法无法使用", Toast.LENGTH_LONG);
            System.exit(0);
            return;
        }

        addView();
    }

    /**
     * 添加键盘与测试view
     */
    private void addView() {
        LinearLayout ll = new LinearLayout(this);
        ll.setPadding(0, 10, 0, 0);
        // 上下排列
        ll.setOrientation(LinearLayout.VERTICAL);
        // 左右剧中
        ll.setHorizontalGravity(Gravity.CENTER);
        // 通过callback的方式来画键盘
        board = new View(this) {
            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                // 回调方式，给外面类传递画布
                drawBoard(canvas, this);
            }

            /**
             * 主动控制键盘大小,不理会android自动布局
             * @param widthMeasureSpec
             * @param heightMeasureSpec
             */
            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                // 通知android，重新调整大小
                setMeasuredDimension(keySize * cells, keySize * rows);
            }
        };
        // 显示煞
        ll.addView(board);

        // 创建展示测试数据的view
        view = new TextView(this);
        view.setTextSize(8);
        // 显现view
        ll.addView(view);
        // 显示rootview
        setContentView(ll);
    }

    private void debug(String str) {
        logs = str.concat('\n' + logs);
        logs = logs.substring(0, Math.min(1500, logs.length() - 1));
        // 只显示前面部分字符
        view.setText(logs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int[] xy= new int[2];
        board.getLocationOnScreen(xy);
        String str = String.format(
                "%s (%d,%d) - (%d,%d) = (%d, %d)",
                MotionEvent.actionToString(ev.getAction()),
                (int) ev.getX(),
                (int) ev.getY(),
                xy[0],
                xy[1],
                (int) (ev.getX() - xy[0]),
                (int) (ev.getY() - xy[1])
        );
        debug(str);
        return true;
    }

    /**
     * 画键盘
     *
     * @param canvas
     */
    final public void drawBoard(Canvas canvas, View view) {
        view.setBackgroundColor(Color.BLACK);
        _getFontSize();
        // 抗锯齿
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.LTGRAY);
        int height = keySize * rows;

        // 画坚线
        for (int i = 1; i < cells; i++) {
            canvas.drawLine(keySize * i, 0, keySize * i, height, paint);
        }

        // 画横线
        for (int i = 1; i < rows; i++) {
            canvas.drawLine(0, i * keySize, keySize * cells, i * keySize, paint);
        }

        // 画字,x->为正；y↓为正；view的左上角为（0，0）

        // 每键4角 + 4边 + 1中间 = 9字
        String str = "0";
        paint.setTypeface(Typeface.MONOSPACE);
        // drawtext的x指字中心离原点距离，y却是指字底边到原点距离（并非中心）
        paint.setTextAlign(Paint.Align.CENTER);
        Typeface typeface = Typeface.createFromAsset(this.getBaseContext().getAssets(), "keys.ttf");
        paint.setTypeface(typeface);
        paint.setColor(Color.WHITE);
        // 字中心点大概到y点，大概是0.4个字高
        double x = 0, y = 0, baseLine = 0.3;
        int charIndex = 0;
        int[] xy= new int[2];
        //键盘离屏幕位移
        board.getLocationOnScreen(xy);
       
     

        for (int row = 1; row <= rows; row++) {
            for (int cell = 1; cell <= cells; cell++) {
                for (int i = 1; i <= 9; i++) {
                    // 键字顺序为左角，上边，右角，右边，右下角，下边，左下角，中间
                    str = "" + chars.charAt(charIndex++);
                    //计算出键0点与屏幕0点距离
                    keys[xy] = 
                    paint.setTextSize(sideFontSize);
                    paint.setColor(Color.GRAY);

                    switch (i) {
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
                                    (rows == row && i >= 5 && i <= 7)
                                    // 最后行下边
                                    ||
                                    (cell == cells && i >= 3 && i <= 5)
                        // 最右列右边
                            ) {
                        str = "\0";
                    }

                    // x在笔刷初始时已经设定成中心为0点;
                    canvas.drawText(str, (float) x, (float) y + 3, paint);
                }
            }
        }

        paint = null;// 释放
    }

    /**
     * 计算键位上边与中心字大小
     * 一般字体都是高比宽大，所以，只需要考虑高度合适即可
     */
    private void _getFontSize() {
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
        int fontHeight = (int) Math.round(keySize * bigHeight);
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
        fontHeight = (int) Math.round(keySize * (1 - bigHeight) / 2);


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
}
