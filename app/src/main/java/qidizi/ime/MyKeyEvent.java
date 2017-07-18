package qidizi.ime;

import java.util.Locale;

import android.os.SystemClock;
import android.text.TextDirectionHeuristic;
import android.view.KeyEvent;
import android.view.inputmethod.InputConnection;

public class MyKeyEvent extends KeyEvent {
	private final static String UNICODE_ESC = "\u238B";
	private final static String UNICODE_TAB = "\u21B9";
	private final static String UNICODE_BACKSPACE = "\u232B";
	private final static String UNICODE_DELETE = "\u2326";
	private final static String UNICODE_LEFT = "\u2190";
	private final static String UNICODE_UP = "\u2191";
	private final static String UNICODE_RIGHT = "\u2192";
	private final static String UNICODE_DOWN = "\u2193";
	private final static String UNICODE_CTRL = "\u2388";
	private final static String UNICODE_SHIFT = "\u21E7";
	private final static String UNICODE_SPACE = "\u2423";
	private final static String UNICODE_ENTER = "\u21A9";
	private final static String UNICODE_ALT = "\u2325";
	private final static String UNICODE_INSERT = "\u2380";
	public final static String KEY_LABELS = ""
			// unicode 在java中使用4个字符来表示
			// 第一行
			//
			// 0
			+ "0" + "\0\0\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			// 1
			+ "1" + "(&)" + "{$}" + "`._" + "+-="
			// 2
			+ "2" + "[|]" + "</>" + "^~\0" + "#@*"
			// 3
			+ "3" + "%\\!" + "?:;" + "\0\0\0" + "\0\0\0"
			// 4
			+ "4" + "\",'" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			// 5
			+ "5" + "\0\0\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			// 6
			+ "6" + "\0\0\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			// 7
			+ "7" + "\0\0\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			// 8
			+ "8" + "\0\0\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			// 9
			+ "9" + "\0\0\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			// 第二行
			+ "q" + "\0Q\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ "w" + "\0W\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ "e" + "\0E\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ "r" + "\0R\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ "t" + "\0T\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ "y" + "\0Y\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ "U" + "\0U\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ "i" + "\0I\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ "o" + "\0O\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ "p" + "\0P\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			// 第三行
			+ "a" + "\0A\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ "s" + "\0S\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ "d" + "\0D\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ "f" + "\0F\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ "g" + "\0G\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ "h" + "\0H\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ "j" + "\0J\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ "k" + "\0K\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ "l" + "\0L\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ UNICODE_ENTER + "\0\0\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			// 第四
			+ "z" + "\0Z\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ "x" + "\0X\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ "c" + "\0C\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ "v" + "\0V\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ "b" + "\0B\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ "n" + "\0N\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ "m" + "\0M\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ UNICODE_LEFT + "\0\0\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ UNICODE_UP + "\0\0\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ UNICODE_RIGHT + "\0\0\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			// 第五
			+ UNICODE_CTRL + "\0\0\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ UNICODE_SHIFT + "\0\0\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ UNICODE_ALT + "\0\0\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ UNICODE_TAB + "\0\0\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ UNICODE_SPACE + "\0\0\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ UNICODE_BACKSPACE + "\0\0\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ UNICODE_DELETE + "\0\0\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ UNICODE_INSERT + "\0\0\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ UNICODE_DOWN + "\0\0\0" + "\0\0\0" + "\0\0\0" + "\0\0\0"
			//
			+ UNICODE_ESC + "\0\0\0" + "\0\0\0" + "\0\0\0" + "\0\0\0";

	public MyKeyEvent(int action, int code) {
		super(action, code);
	}

	// 模拟一次字母/数字/符号keydown+keyup
	public static void sendASCIIKeyEvent(String keyName, SoftKeyboard skb, int action) {
		int unicode = keyName.codePointAt(0);
		int metas = 0;

		// 数字不用处理
		if (48 <= unicode && unicode <= 57) {
			;
		} else if (UNICODE_ENTER.equals(keyName)) {
			keyName = "ENTER";
		} else if (UNICODE_SPACE.equals(keyName)) {
			keyName = "SPACE";
		} else if (UNICODE_LEFT.equals(keyName)) {
			keyName = "DPAD_LEFT";
		} else if (UNICODE_UP.equals(keyName)) {
			keyName = "DPAD_UP";
		} else if (UNICODE_RIGHT.equals(keyName)) {
			keyName = "DPAD_RIGHT";
		} else if (UNICODE_DOWN.equals(keyName)) {
			keyName = "DPAD_DOWN";
		} else if (UNICODE_ESC.equals(keyName)) {
			keyName = "ESCAPE";
		} else if (UNICODE_TAB.equals(keyName)) {
			keyName = "TAB";
		} else if (UNICODE_BACKSPACE.equals(keyName)) {
			keyName = "DEL";
		} else if (UNICODE_DELETE.equals(keyName)) {
			keyName = "FORWARD_DEL";
		} else if (UNICODE_CTRL.equals(keyName)) {
			keyName = "CTRL_LEFT";
		} else if (UNICODE_SHIFT.equals(keyName)) {
			keyName = "SHIFT_LEFT";
		} else if (UNICODE_ALT.equals(keyName)) {
			keyName = "ALT_LEFT";
		} else if (UNICODE_INSERT.equals(keyName)) {
			keyName = "INSERT";
		} else if (65 <= unicode && unicode <= 90) {// A-Z
			metas = KeyEvent.META_CAPS_LOCK_ON;
		} else if (97 <= unicode && unicode <= 122) {// a-z
			keyName = keyName.toUpperCase(Locale.ENGLISH);
		} else {// 符号
			switch (unicode) {
			case 35:
				keyName = "POUND";
				break;
			case 39:
				keyName = "APOSTROPHE";
				break;
			case 40:
				keyName = "NUMPAD_LEFT_PAREN";
				break;
			case 41:
				keyName = "NUMPAD_RIGHT_PAREN";
				break;
			case 42:
				keyName = "STAR";
				break;
			case 43:
				keyName = "PLUS";
				break;
			case 44:
				keyName = "COMMA";
				break;
			case 45:
				keyName = "MINUS";
				break;
			case 46:
				keyName = "PERIOD";
				break;
			case 47:
				keyName = "SLASH";
				break;
			case 59:
				keyName = "SEMICOLON";
				break;
			case 61:
				keyName = "NUMPAD_EQUALS";
				break;
			case 64:
				keyName = "AT";
				break;
			case 91:
				keyName = "LEFT_BRACKET";
				break;
			case 92:
				keyName = "BACKSLASH";
				break;
			case 93:
				keyName = "RIGHT_BRACKET";
				break;
			case 96:
				keyName = "GRAVE";
				break;
			default:
				keyName = "UNKNOWN";
				break;
			}
		}

		int keyCode = KeyEvent.keyCodeFromString("KEYCODE_" + keyName);
		InputConnection ic = skb.getCurrentInputConnection();
		KeyEvent kv = new KeyEvent(action, keyCode);
		ic.sendKeyEvent(kv);
		ic = null;
	}

}
