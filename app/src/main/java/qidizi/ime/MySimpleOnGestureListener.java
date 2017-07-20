package qidizi.ime;

import android.view.*;
import android.content.*;
import android.widget.TextView;

public class MySimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener {
    /**
     * 获取麻烦，直接传过来
     */
    private View view;

    public MySimpleOnGestureListener(View view) {
        super();
        this.view = view;
    }

    private  void tip(String str) {
        TextView tv = (TextView)view.getRootView().findViewById(R.id.textView);
        tv.append("\n" + str);
    }


    @Override
    public boolean onDown(MotionEvent e) {
        tip("onDown:" + MotionEvent.actionToString(e.getAction()));
        return true;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        tip("onSingleTapUp:" + MotionEvent.actionToString(e.getAction()));
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        tip("onDoubleTap:" + MotionEvent.actionToString(e.getAction()));
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        tip("onFling:" +
                "e1." + MotionEvent.actionToString(e1.getAction()) +
                "e2." + MotionEvent.actionToString(e1.getAction()) +
                "x." + velocityX +
                "y." + velocityY
        );
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        tip("onScroll:" +
                "e1." + MotionEvent.actionToString(e1.getAction()) +
                "e2." + MotionEvent.actionToString(e1.getAction()) +
                "x." + distanceX +
                "y." + distanceY
        );
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        tip("onDoubleTap:" + MotionEvent.actionToString(e.getAction()));
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        tip("onLongPress:" + MotionEvent.actionToString(e.getAction()));
    }

    @Override
    public void onShowPress(MotionEvent e) {
        tip("onShowPress:" + MotionEvent.actionToString(e.getAction()));
    }

}
