package com.dqs.wifi.ui.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

/**
 * Created by Daniel Du on 2015/12/30.
 */
public abstract class SubAdapter  extends BaseAdapter{

    protected String mTag = "DQS-Debug:" + this.getClass().getSimpleName();
    protected Context mContext;

}
