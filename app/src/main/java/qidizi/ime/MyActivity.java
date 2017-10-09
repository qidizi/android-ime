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
import android.view.inputmethod.*;

public class MyActivity extends Activity
{
    /**
     * 计算得到的单键连长
     */
    private static int keySize;
    /**
     * 大屏时，最大键边长
     */
    private final static int keyMaxSize = 106;
    /**
     * 最小的屏幕，计算出边长达不到时拒绝使用
     */
    private final static int keyMinSize = 30;

    /**
     * 行数
     */
    private static final int keyboardRows = 5;
    /**
     * 列数
     */
    private static final int keyboardCells = 10;
    /**
     键符个数
     */
    private final static int keySymbolNum = 9;
    /**
     * 中间大字textsize
     */
    private static int keyCenterSize = 0;
    /**
     * 边上的字textsize
     */
    private static int keySideSize = 0;

    //
    private View board;

    // debug view
    private EditText view;
    // 用来缓存文字的集合
    private static String logs = "";

    // 键中下标
    private final static int KEY_CENTER_INDEX = 0;
    // 键左上下标
    private final static int KEY_LEFT_TOP_INDEX = 1;
    // 键正上下标
    private final static int KEY_TOP_INDEX = 2;
    // 键右上下标
    private final static int KEY_RIGHT_TOP_INDEX = 3;
    // 键右下标
    private final static int KEY_RIGHT_INDEX = 4;
    // 键右下下标
    private final static int KEY_RIGHT_BOTTOM_INDEX = 5;
    // 键正下下标
    private final static int KEY_BOTTOM_INDEX = 6;
    // 键左下下标
    private final static int KEY_LEFT_BOTTOM_INDEX = 7;
    // 键左下标
    private final static int KEY_LEFT_INDEX = 8;


    // 下标用途常量
    // a(shift未亮)面符号🔣
    private final static int KEY_SYMBOL = 0;
    // 非shift时符号对应code
    private final static int KEY_SYMBOL_CODE = 1;
    // shift时符号
    private final static int KEY_SYMBOL_SHIFT = 2;
    // shift符号对应code
    private final static int KEY_SYMBOL_SHIFT_CODE = 3;
    private final static int KEY_SYMBOL_MAX = 3;
    private final static String SYMBOL_BACKSPACE ="\u232b";
    private final static String SYMBOL_SHIFT ="\u21e7";
    private final static String SYMBOL_CAPSLOCK ="\u21ea";
    private final static String SYMBOL_ALT ="alt";
    private final static String SYMBOL_ENTER ="\u23ce";
    private final static String SYMBOL_LEFT ="\u21e0";
    private final static String SYMBOL_UP ="\u21e1";
    private final static String SYMBOL_RIGHT ="\u21e2";
    private final static String SYMBOL_DOWN ="\u21e3";
    private final static String SYMBOL_SPACE ="\u23b5";
    private final static String SYMBOL_TAB ="\u21b9";
    private final static String SYMBOL_DELETE ="\u2326";
    private final static String SYMBOL_ESC ="\u241B";
    private final static String SYMBOL_CTRL ="ctrl";



    // 键符,按照单个键显示字符，键内,中>左上>正上>右上>右>右下>下>左下>左，左键>右键>上行>下行对应
    private static String[][][][] keySymbols = new String[keyboardRows][keyboardCells][keySymbolNum][KEY_SYMBOL_MAX];

    //down到up移动距离在这个内属于click
    private static  int maxClickMove = 10;
    //按下时坐标
    private static float[] down ={0,0};
    // 多指
    private static boolean isMultiFinger = false;
    // 移动多少距离内属于键位8边（角）符号，超过就算是手势了
    private static int keyBoxRange = 0;


    /**
     配置键
     */
    private void setKeys()
    {   
        String
            centerKeys = "0123456789qwertyuiopasdfghjkl"
            + SYMBOL_BACKSPACE
            + "zxcvbnm"

            + SYMBOL_UP

            + SYMBOL_SPACE 
            + SYMBOL_ENTER
            + "\0\0"
            + SYMBOL_TAB

            + "\0\0\0"
            + SYMBOL_LEFT

            + SYMBOL_DOWN
            + SYMBOL_RIGHT

            + SYMBOL_SHIFT




            ,
            //上面字符

            upKeys     = "!@#$^%\"'`?QWERTYUIOPASDFGHJKL"
            + SYMBOL_DELETE
            + "ZXCVBNM"
            ,bottomKeys=";,·.|:_*=~&"
            ,leftKeys  ="\0({<[+\\"
            ,rightKeys ="\0)}>]-/"    
            ,ltKeys="\0“‘（［｛《〈〔『「【〖。？！、：；～·…—￥"
            ,rtKeys="，”’）］｝》〉〕』」】〗"
            ,rbKeys=""
            ,lbKeys="\0"
            ;

        int fx =1;
        for (int cell =0,row=0,si=0;si < keyboardRows * keyboardCells;si++)
        {
            if (cell >= keyboardCells)
            {
                cell = 0;
                row++;
            }

            if (si < centerKeys.length())
            {
                keySymbols[row][cell][KEY_CENTER_INDEX][KEY_SYMBOL] = "" + centerKeys.charAt(si);
            }

            if (si < upKeys.length())
            {
                keySymbols[row][cell][KEY_TOP_INDEX][KEY_SYMBOL] = "" + upKeys.charAt(si);
            }

            if (si < rightKeys.length())
            {
                keySymbols[row][cell][KEY_RIGHT_INDEX][KEY_SYMBOL] = "" + rightKeys.charAt(si);
            }


            if (si < bottomKeys.length())
            {
                keySymbols[row][cell][KEY_BOTTOM_INDEX][KEY_SYMBOL] = "" + bottomKeys.charAt(si);
            }


            if (si < leftKeys.length())
            {
                keySymbols[row][cell][KEY_LEFT_INDEX][KEY_SYMBOL] = "" + leftKeys.charAt(si);
            }


            if (si < ltKeys.length())
            {
                keySymbols[row][cell][KEY_LEFT_TOP_INDEX][KEY_SYMBOL] = "" + ltKeys.charAt(si);
            }



            if (si < rtKeys.length())
            {
                keySymbols[row][cell][KEY_RIGHT_TOP_INDEX][KEY_SYMBOL] = "" + rtKeys.charAt(si);
            }



            if (si < rbKeys.length())
            {
                keySymbols[row][cell][KEY_RIGHT_BOTTOM_INDEX][KEY_SYMBOL] = "" + rbKeys.charAt(si);
            }


            if (si < lbKeys.length())
            {
                keySymbols[row][cell][KEY_LEFT_BOTTOM_INDEX][KEY_SYMBOL] = "" + lbKeys.charAt(si);
            }

            //f*

            if ((row == 2 && cell >= 5) || (row == 3 && cell <= 6))
            {
                keySymbols[row][cell][KEY_BOTTOM_INDEX][KEY_SYMBOL] = "F" + (fx++);
            }
            cell++;
        }

        keySymbols[4][0][KEY_TOP_INDEX][KEY_SYMBOL] = "Esc";
        keySymbols[4][0][KEY_CENTER_INDEX][KEY_SYMBOL] = "Ctrl";
        keySymbols[4][1][KEY_CENTER_INDEX][KEY_SYMBOL] = "Alt";
        keySymbols[4][6][KEY_TOP_INDEX][KEY_SYMBOL] = "Home";
        keySymbols[4][8][KEY_TOP_INDEX][KEY_SYMBOL] = "End";
        keySymbols[3][7][KEY_TOP_INDEX][KEY_SYMBOL] = "PgUp";
        keySymbols[4][7][KEY_TOP_INDEX][KEY_SYMBOL] = "PgDn";
        keySymbols[4][1][KEY_TOP_INDEX][KEY_SYMBOL] = "Inst";
        keySymbols[4][2][KEY_TOP_INDEX][KEY_SYMBOL] = "\u2756";
        keySymbols[4][9][KEY_TOP_INDEX][KEY_SYMBOL] = "Fn";
        keySymbols[4][5][KEY_CENTER_INDEX][KEY_SYMBOL] = "中";
        keySymbols[4][4][KEY_CENTER_INDEX][KEY_SYMBOL] = "常";
        keySymbols[4][3][KEY_CENTER_INDEX][KEY_SYMBOL] = "\u2328";

    }
    /**
     * 画出键盘
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setKeys();
        createBoard();
    }

    /**
     * 初始化键盘
     */
    private void createBoard()
    {
        if (keySize > 0)
        {
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
        keySize = Math.round(minSide / keyboardCells);
        keyBoxRange = keySize * 2;
        maxClickMove = keySize / 2;
        // 键边有一个上限
        keySize = Math.min(keySize, keyMaxSize);

        if (keySize < keyMinSize)
        {
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
    private void addView()
    {
        LinearLayout ll = new LinearLayout(this);
        ll.setPadding(0, 10, 0, 0);
        // 上下排列
        ll.setOrientation(LinearLayout.VERTICAL);
        // 左右剧中
        ll.setHorizontalGravity(Gravity.CENTER);
        // 通过callback的方式来画键盘
        board = new View(this) {
            @Override
            protected void onDraw(Canvas canvas)
            {
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
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
            {
                // 通知android，重新调整大小
                setMeasuredDimension(keySize * keyboardCells, keySize * keyboardRows);
            }
        };
        // 显示煞
        ll.addView(board);

        // 创建展示测试数据的view
        view = new EditText(this);
        view.setTextSize(8);
        // 显现view
        ll.addView(view);
        // 显示rootview
        setContentView(ll);
    }

    private void debug(String str)
    {
        logs = str.concat('\n' + logs);
        logs = logs.substring(0, Math.min(1500, logs.length() - 1));
        // 只显示前面部分字符
        view.setText(logs);
    }

    /**
     屏幕左上角为0点,向下或向右为正
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        String str;
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:

                down[0] = ev.getX();
                down[1] = ev.getY();
                int[] xy = new int[2];
                board.getLocationOnScreen(xy);
                str = String.format(
                    "down(%d,%d)-0点(%d,%d)=(%d, %d)",
                    // 
                    (int) ev.getX(),
                    (int) ev.getY(),
                    xy[0],
                    xy[1],
                    (int) (ev.getX() - xy[0]),
                    (int) (ev.getY() - xy[1])
                );
                // debug(str);
                break;
            case MotionEvent.ACTION_UP:
                str = String.format(
                    "up(%d,%d)-down(%d,%d)=(%d, %d)",
                    // MotionEvent.actionToString(ev.getAction()),
                    (int) ev.getX(),
                    (int) ev.getY(),
                    (int)down[0],
                    (int)down[1],
                    (int) (ev.getX() - down[0]),
                    (int) (ev.getY() - down[1])
                );
                //  debug(str);

                float maxMove = Math.max(Math.abs(ev.getX() - down[0]), Math.abs(ev.getY() - down[1]));

                if (maxClickMove >= maxMove)
                {
                    // 当做点击处理点击
                    click(ev);
                    return true;
                }

                // 小距离移动触发本键位的8向符号
                if (maxMove <= keyBoxRange)
                {

                    keyFinger(ev);
                    return  true;
                }
                //大距离移动触发手势
                break;

            case MotionEvent.ACTION_MOVE:
                // debug("move");
                break;
            default:
                debug(MotionEvent.actionToString(ev.getAction() & MotionEvent.ACTION_MASK));
        }

        return true;
    }

    /**
     * key内移动，触发对应边、角上的符号
     * @param ev
     */
    private void keyFinger(MotionEvent ev)
    {
        //只简单处理：按下点与放开点连直线，以按下点当直角坐标系原点，以原点画圆，以从圆心点出发射线等分8份，计算出直线在那个等分区域，
        //夹角计算公式arctan( (y放 - y按) / (x放 - x按) )，然后看线在直角坐标系那个区
        // 计算出角或边特殊情况：平面直角坐标系中，圆心坐标为坐标原点（0,0）
        // 计算夹角
        //1。设A点旋转前的角度为δ，则旋转(逆时针)到C点后角度为δ+β
        // 2。求A，B两点的距离：dist1=|AB|=y/sin(δ)=x/cos(δ)

        //up(x,y)=up - down
        double upX = ev.getX();
        double upY = ev.getY();
        double r=0;
        //up在x轴线上
        if (upY == down[1])
        {
            //在y左：y右
            r = upX <= down[1] ?0: 180;        
        }
        //up在y轴
        else if (upX == down[0])
        {
            //在x上：x下
            r = upY <= down[1] ? 90: 360;            
        }
        else
        {
            //四个区内
            //角度对边：斜边
            double l4=4,l5=5,l3=3;

            if (upX <= down[0])
            {
                //在y左

                if (upY <= down[1])
                {
                    //x上
                    r += 0;
                    l4 = upY - down[1];
                    l3 = upX - down[0];

                }
                else
                {
                    //x下
                    r += 270;

                    l4 = upX - down[0];
                    l3 = upY - down[1];
                } 
            }
            else
            {
                //在y右边
                //x上:x下
                if (upY <= down[1])
                {

                    r += 90;
                    l4 = upX - down[0];
                    l3 = upY - down[1];
                }
                else
                {

                    l4 = upY - down[1];
                    l3 = upX - down[0];
                    r += 180;
                }
            }


            l3 = Math.abs(l3);
            l4 = Math.abs(l4);
            l5 = Math.sqrt(l4 * l4 + l3 * l3);
            //计算锐角arcsin(对边/斜边)
            r +=  Math.asin(l4 / l5) * 180 / Math.PI;
        }


        int[] indexs ={
            KEY_LEFT_INDEX,
            KEY_LEFT_TOP_INDEX,
            KEY_TOP_INDEX,
            KEY_RIGHT_TOP_INDEX,
            KEY_RIGHT_INDEX,
            KEY_RIGHT_BOTTOM_INDEX,
            KEY_BOTTOM_INDEX,
            KEY_LEFT_BOTTOM_INDEX

        };
        //计算出来的角度是相对x负轴，而字分割却是x负轴上有字，所以要偏向一个角度
        double area = 360 / indexs.length;
        //偏向一个角度
        double half = area / 2;
        //在下个区域内
        int keyIndex  ;

        if (r >= 360 - half || r <= half)
        {
            //0度上下是最后一个边字
            keyIndex = 0;
        }
        else
        {
            //最小是1，最大是7
            keyIndex = (int)Math.ceil((r - half) / area);
        }
        keyIndex = indexs[keyIndex];
        // 得到down点
        int[] rc  = getKeyRowCell(down);
        String keySymbol = keySymbols[rc[0]][rc[1]][keyIndex][KEY_SYMBOL];
       // debug(String.format("fig row %d cell %d : %f 区%d %s", rc[0], rc[1], r, keyIndex, keySymbol));
        _sendKey(keySymbol);
    }
    /**
     获取当前事件对应key的
     */
    private int[] getKeyRowCell(float[] up)
    {
        int[] boardXY = new int[2];
        //键盘离屏幕位移
        board.getLocationOnScreen(boardXY);
        int[] rc = new int[2];
        rc[1] = (int)Math.abs(Math.ceil((boardXY[0] - up[0]) / keySize));
        rc[0] = (int)Math.abs(Math.ceil((boardXY[1] - up[1]) / keySize));
        return rc;
    }


     private void click(MotionEvent Ev)
    {
        int[] rc = getKeyRowCell(down);

        if (rc[0] < 0 || rc[0] >= keyboardRows || rc[1] < 0 || rc[1] >= keyboardCells)
        {
            // 超出rows或是cells
            return;
        }

        String keySymbol = keySymbols[rc[0]][rc[1]][KEY_CENTER_INDEX][KEY_SYMBOL];
       // debug(String.format("click row %d cell %d : %s", rc[0], rc[1], keySymbol));
       _sendKey(keySymbol);
    }

    private void _sendKey(String key)
    {
        
        if(null ==key){
            return;
        }
        
        Map<String,Integer> keyCode = new HashMap<String,Integer>(){{
            
                put(SYMBOL_TAB,KeyEvent.KEYCODE_TAB);
                put(SYMBOL_DELETE,KeyEvent.KEYCODE_DEL);
                put(SYMBOL_BACKSPACE,KeyEvent.KEYCODE_FORWARD_DEL);
                put(SYMBOL_RIGHT,KeyEvent.KEYCODE_DPAD_RIGHT);
                put(SYMBOL_DOWN,KeyEvent.KEYCODE_DPAD_DOWN);
                put(SYMBOL_LEFT,KeyEvent.KEYCODE_DPAD_LEFT);
                put(SYMBOL_UP,KeyEvent.KEYCODE_DPAD_UP);
                put(SYMBOL_ENTER,KeyEvent.KEYCODE_ENTER);
                put(SYMBOL_ESC,KeyEvent.KEYCODE_ESCAPE);
                put(SYMBOL_ALT,KeyEvent.KEYCODE_ALT_LEFT);
                put(SYMBOL_SPACE,KeyEvent.KEYCODE_SPACE);
        }};
        
        if(keyCode.containsKey(key)){
            keyDownUp(keyCode.get(key));
            return;
        }
        
        if(key.matches("^\\d$|^F\\d+$")){
            keyDownUp("KEYCODE_"+key);
            return;
        }
        
        if(key.matches("^[a-z]$")){
            keyDownUp("KEYCODE_" +key.toUpperCase());
            return;
        }

        if(key.matches("^[A-Z]$")){
            key="KEYCODE_" +key;
            BaseInputConnection  ic = new BaseInputConnection(view, false);
            int code = KeyEvent.keyCodeFromString(key);
            KeyEvent evt =new KeyEvent(0,0,KeyEvent.ACTION_DOWN, code,KeyEvent.META_SHIFT_LEFT_ON);
           
            ic.sendKeyEvent(evt);
            evt = evt =new KeyEvent(0,0,KeyEvent.ACTION_UP, code,KeyEvent.META_SHIFT_LEFT_ON);
            ic.sendKeyEvent(evt);
            
            return;
        }
        
        BaseInputConnection  ic = new BaseInputConnection(view, false);
       ic.commitText(key,0);
        
    }
    
    //简单实现按下放开
    private void keyDownUp(String key){
        BaseInputConnection  ic = new BaseInputConnection(view, false);
        int code = KeyEvent.keyCodeFromString(key);
        
        ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, code));
        ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, code));
        
    }

    //简单实现按下放开
    private void keyDownUp(int code){
        BaseInputConnection  ic = new BaseInputConnection(view, false);
         ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, code));
        ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, code));
       

    }
    /**
     * 画键盘
     *
     * @param canvas
     */
    final public void drawBoard(Canvas canvas, View view)
    {
        view.setBackgroundColor(Color.BLACK);
        _getFontSize();
        // 抗锯齿
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.LTGRAY);
        int height = keySize * keyboardRows;

        // 画坚线
        for (int i = 1; i < keyboardCells; i++)
        {
            canvas.drawLine(keySize * i, 0, keySize * i, height, paint);
        }

        // 画横线
        for (int i = 1; i < keyboardRows; i++)
        {
            canvas.drawLine(0, i * keySize, keySize * keyboardCells, i * keySize, paint);
        }

        // 画字,x->为正；y↓为正；view的左上角为（0，0）
        paint.setTypeface(Typeface.MONOSPACE);
        // drawtext的x指字中心离原点距离，y却是指字底边到原点距离（并非中心）
        paint.setTextAlign(Paint.Align.CENTER);
        Typeface typeface = Typeface.createFromAsset(this.getBaseContext().getAssets(), "keys.ttf");
        paint.setTypeface(typeface);
        paint.setColor(Color.WHITE);
        // 字中心点大概到y点，大概是0.4个字高
        double baseLine = 0.3;
        int[] xy = new int[2];
        //键盘离屏幕位移
        board.getLocationOnScreen(xy);
        int row,cell;

        for (row = 1; row <= keyboardRows; row++)
        {
            for (cell = 1; cell <= keyboardCells; cell++)
            {

                paint.setTextSkewX(0);
                // 中间字 
                paint.setColor(Color.WHITE);
                paint.setTextSize(keyCenterSize);
                _draw(row, cell, KEY_CENTER_INDEX,

                      // 半个键
                      keySize * (cell - 1 + 0.5),
                      // 向下移半个字高的4/10
                      keySize * (row - 1 + 0.5) + keyCenterSize * baseLine,
                      canvas, paint);
                paint.setTextSize(keySideSize);
                paint.setColor(Color.rgb(230, 230, 230));


                _draw(row, cell, KEY_LEFT_TOP_INDEX,
                      // x右移半个字
                      keySize * (cell - 1) + keySideSize * 0.5,
                      // y，中心点下移半个字高
                      keySize * (row - 1) + keySideSize * (0.5 + baseLine)
                      , canvas, paint);

                _draw(row, cell, KEY_TOP_INDEX,
                      keySize * (cell - 1 + 0.5),
                      // y，中心点下移半个字高
                      keySize * (row - 1) + keySideSize * (0.5 + baseLine),
                      canvas, paint);

                _draw(row, cell, KEY_RIGHT_TOP_INDEX,
                      keySize * cell - keySideSize * 0.5,
                      // y，中心点下移半个字高
                      keySize * (row - 1) + keySideSize * (0.5 + baseLine),
                      canvas, paint);


                _draw(row, cell, KEY_RIGHT_INDEX,
                      keySize * cell - keySideSize * 0.5,
                      keySize * (row - 1 + 0.5) + keySideSize * baseLine,
                      canvas, paint);

                _draw(row, cell, KEY_RIGHT_BOTTOM_INDEX,
                      keySize * cell - keySideSize * 0.5,
                      keySize * row - keySideSize * baseLine,
                      canvas, paint);


                _draw(row, cell, KEY_BOTTOM_INDEX,
                      keySize * (cell - 1 + 0.5),
                      keySize * row - keySideSize * baseLine,
                      canvas, paint);

                _draw(row, cell, KEY_LEFT_BOTTOM_INDEX,
                      keySize * (cell - 1) + keySideSize * 0.5,
                      keySize * row - keySideSize * baseLine,
                      canvas, paint);


                _draw(row, cell, KEY_LEFT_INDEX,
                      keySize * (cell - 1) + keySideSize * 0.5,
                      keySize * (row - 1 + 0.5) + keySideSize * baseLine,
                      canvas, paint);




            }
        }

        paint = null;// 释放
    }

    private void _draw(int row, int cell, int num, double x, double y, Canvas canvas, Paint paint)
    {
        String keySymbol = keySymbols[row - 1][cell - 1][num][KEY_SYMBOL];
        if (null != keySymbol && keySymbol.length() > 1 && num == KEY_CENTER_INDEX)
        {

            //paint.setTextSkewX(0.4f);
            paint.setTextSize(paint.getTextSize() / 2);
        }
        canvas.drawText(null == keySymbol ? "\0" : keySymbol, (float)x, (float)y + 3, paint);
    }
    /**
     * 计算键位上边与中心字大小
     * 一般字体都是高比宽大，所以，只需要考虑高度合适即可
     */
    private void _getFontSize()
    {
        //目前不清楚机制，如果已经设置了，就没有必要再重新计算
        if (keyCenterSize > 0)
        {
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

        keyCenterSize = height;
        // 重设
        height = 0;
        fontHeight = (int) Math.round(keySize * (1 - bigHeight) / 2);


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

        keySideSize = height;
        p = null;
    }
}
