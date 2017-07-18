package qidizi.ime;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

public class FloatActivity extends Activity {

	/** Called when the activity is first created. */

	private WindowManager wm = null;
	private static WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

	private TextView myFV = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = new View(this.getApplicationContext());
		setContentView(view);

	}

	private void createView() {
		myFV = new TextView(getApplicationContext());
		myFV.setBackgroundColor(Color.WHITE);
		myFV.setText("text");
		// 获取WindowManager
		wm = (WindowManager) getApplicationContext().getSystemService("window");
		// 设置LayoutParams(全局变量）相关参数

		/**
		 * 以下都是WindowManager.LayoutParams的相关属性 具体用途可参考SDK文档
		 */
		wmParams.type = LayoutParams.TYPE_PHONE; // 设置window type
		wmParams.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明

		// 设置Window flag
		wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
		/*
		 * 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
		 * wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL |
		 * LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
		 */

		wmParams.gravity = Gravity.START | Gravity.TOP; // 调整悬浮窗口至左上角
		// 以屏幕左上角为原点，设置x、y初始值
		wmParams.x = 0;
		wmParams.y = 0;

		// 设置悬浮窗口长宽数据
		wmParams.width = 40;
		wmParams.height = 40;

		// 显示myFloatView图像
		wm.addView(myFV, wmParams);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 在程序退出(Activity销毁）时销毁悬浮窗口
		wm.removeView(myFV);
	}

	@Override
	protected void onRestart() {
		wm.removeView(myFV);
		super.onRestart();
	}

	@Override
	protected void onStop() {
		// 创建悬浮窗口
		createView();
		super.onStop();
	}
}
