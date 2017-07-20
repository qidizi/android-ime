package qidizi.ime;
import android.view.*;
import android.content.*;

public class MySimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener
{
    /**获取麻烦，直接传过来*/
    private Context context;
    public void MySimpleOnGestureListener(Context context){
        this.context = context;
    }
}
