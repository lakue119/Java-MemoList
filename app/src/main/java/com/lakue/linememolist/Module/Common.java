package com.lakue.linememolist.Module;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Toast;

public class Common {
    Context context;

    public Common(Context context) {
        this.context = context;
    }

    public void showToast(String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public void printLog(String log){
        Log.i(context.getClass().getSimpleName()+"Log",log);
    }
    public void printErrortLog(String log){
        Log.e(context.getClass().getSimpleName()+"Log",log);
    }

    public final static int TYPE_ALBUM = 1001;
    public final static int TYPE_PHOTO = 1002;
    public final static int TYPE_URL = 1003;
    public final static int TYPE_CANCEL = 1003;

    public final static int REQUEST_ALBUM = 2001;
    public final static int REQUEST_IMAGE_CAPTURE = 2002;
    public final static int REQUEST_IMAGE_TYPE = 2003;
    public final static int REQUEST_UPDATE_MEMO = 2004;
    public final static int REQUEST_REFRESH_MEMO = 2005;

    public final static int RECYCLER_TYPE_MEMO_LIST = 3001;
    public final static int RECYCLER_TYPE_MEMO_IMGE = 3002;


    public final static int TYPE_INTENT_INSERT = 4001;
    public final static int TYPE_INTENT_UPDATE = 4002;

    public static int convertPixelsToDp(float px, Context context) {
        int value = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, context.getResources().getDisplayMetrics());
        return value;
    }
}
