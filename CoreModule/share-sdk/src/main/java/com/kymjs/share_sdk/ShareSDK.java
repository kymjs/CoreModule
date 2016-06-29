package com.kymjs.share_sdk;

import android.content.Context;
import android.content.Intent;

/**
 * Created by ZhangTao on 6/29/16.
 */
public class ShareSDK {
    public static void shareUrl(Context context, String url) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "来自开源实验室的分享:" + url);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, "发送到:"));
    }
}
