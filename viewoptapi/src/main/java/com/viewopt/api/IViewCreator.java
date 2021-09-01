package com.viewopt.api;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public interface IViewCreator {

    View createView(String name, Context context, AttributeSet attrs);

}
