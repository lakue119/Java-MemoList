package com.lakue.linememolist.Module;

import android.content.Context;
import android.widget.Toast;

public class Common {
    Context context;

    public Common(Context context) {
        this.context = context;
    }

    public void showToast(String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    final static int TYPE_CUSTOM_QUESTION = 1;
    public final static int TYPE_ALBUM = 1001;
    public final static int TYPE_PHOTO = 1002;
    public final static int TYPE_URL = 1003;
    public final static int TYPE_CANCEL = 1003;


    public final static int REQUEST_ALBUM = 2001;
    public final static int REQUEST_IMAGE_CAPTURE = 2002;
    public final static int REQUEST_IMAGE_TYPE = 2003;
}
