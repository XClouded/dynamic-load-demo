
package com.ldw.skinmain;

import android.content.Context;
import android.graphics.drawable.Drawable;

public interface IPlugin {

    public void setContext(Context context);

    public String getString(String packageName);

    public Drawable getDrawable(String name, String packageName);

}
