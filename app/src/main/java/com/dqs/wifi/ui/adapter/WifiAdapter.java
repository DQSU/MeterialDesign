package com.dqs.wifi.ui.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dqs.wifi.R;
import com.dqs.wifi.ui.actvity.CheeseDetailActivity;
import com.dqs.wifi.ui.actvity.DesActivity;

import java.util.List;

/**
 * Created by Daniel Du on 2015/12/30.
 */
public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.ViewHolder> {

    /** These values are matched in string arrays -- changes must be kept in sync */
    static final int SECURITY_NONE = 0;
    static final int SECURITY_WEP = 1;
    static final int SECURITY_PSK = 2;
    static final int SECURITY_EAP = 3;

    private static final int[] STATE_SECURED = {
            R.attr.state_encrypted
    };
    private static final int[] STATE_NONE = {};

    private String mTag = "DQS-Debug:" + this.getClass().getSimpleName();
    private List<ScanResult> mScanResults;
    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;

    public WifiAdapter(Context context, List<ScanResult> scanResults){
        Log.d(mTag, "WifiAdapter init");
        mScanResults = scanResults;

        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;

        if (mScanResults == null) {
            return;
        }
        for (int i = 0; i < mScanResults.size();){
            if (mScanResults.get(i) == null ||TextUtils.isEmpty(mScanResults.get(i).SSID)){
                mScanResults.remove(i);
            } else {
                i++;
            }
        }
    }

    @Override
    public int getItemCount() {
        int count = mScanResults == null ? 0 : mScanResults.size();
        Log.d(mTag, "getItemCount(), count : " + count);
        return count;
    }

    @Override
    public WifiAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(mTag, "onCreateViewHolder()");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wifi, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final WifiAdapter.ViewHolder holder, final int position) {
        Log.d(mTag, "onBindViewHolder()");
        holder.ssidText.setText(mScanResults.get(position).SSID);
        holder.sigalImage.setImageLevel(WifiManager.calculateSignalLevel(mScanResults.get(position).level, 4));
        int security = getSecurity(mScanResults.get(position));
        holder.sigalImage.setImageState((security != SECURITY_NONE) ?
                STATE_SECURED : STATE_NONE, true);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (position == 1) {
                    intent = new Intent(holder.view.getContext(), DesActivity.class);
                } else {

                    intent = new Intent(holder.view.getContext(), CheeseDetailActivity.class);
                    intent.putExtra(CheeseDetailActivity.EXTRA_NAME, holder.ssidText.getText());
                }

                holder.view.getContext().startActivity(intent,
                        ActivityOptions.makeSceneTransitionAnimation((Activity) holder.view.getContext()).toBundle());

                //                Toast.makeText(holder.view.getContext(),
                //                        position + " : " + mScanResults.get(position).SSID, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{
        public View view;
        public TextView ssidText;
        public ImageView sigalImage;

        public ViewHolder(View view) {
            super(view);
            Log.d(mTag, "ViewHolder init");
            this.view = view;
            this.ssidText = (TextView) view.findViewById(R.id.ssid_name);
            this.sigalImage = (ImageView) view.findViewById(R.id.ssid_signal);

        }
    }

    private static int getSecurity(ScanResult result) {
        if (result.capabilities.contains("WEP")) {
            return SECURITY_WEP;
        } else if (result.capabilities.contains("PSK")) {
            return SECURITY_PSK;
        } else if (result.capabilities.contains("EAP")) {
            return SECURITY_EAP;
        }
        return SECURITY_NONE;
    }


}
