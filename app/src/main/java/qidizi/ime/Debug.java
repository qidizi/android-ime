package qidizi.ime;

import android.util.Log;

class Debug {
    
	// 使用Log来显示调试信息,因为log在实现上每个message有4k字符长度限制
	// 所以这里使用自己分节的方式来输出足够长度的message
	public static void show(String str) {
		str = str.trim();
		int index = 0;
		int maxLength = 4000;
		String sub;
		while (index < str.length()) {
			// java的字符不允许指定超过总的长度end
			if (str.length() <= index + maxLength) {
				sub = str.substring(index);
			} else {
				sub = str.substring(index, maxLength);
			}
			
			index += maxLength;
			Log.e("qidizi_debug", sub.trim());
		}
	}
	/**
	* 浮动提示
	*/
	public static void floatShow (String str) {
		}
		
	}
