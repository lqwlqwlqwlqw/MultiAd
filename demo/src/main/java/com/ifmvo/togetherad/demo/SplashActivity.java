package com.ifmvo.togetherad.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;


import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.CSJAdError;
import com.bytedance.sdk.openadsdk.CSJSplashAd;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.mediation.manager.MediationAdEcpmInfo;
import com.bytedance.sdk.openadsdk.mediation.manager.MediationBaseManager;

import java.util.HashMap;

public class SplashActivity extends Activity {

    public static final String TAG = "TT-SplashActivity";
    private FrameLayout mAdContainer;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        context = this;

        setContentView(R.layout.activity_launcher_layout);

        mAdContainer = findViewById(R.id.root_container);

        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                loadSplashAd((Activity) context);
            }
        },3000);
    }

    //构造开屏广告的Adslot
    private AdSlot buildSplashAdslot() {
        return new AdSlot.Builder()
                .setCodeId("102527836") //广告位ID
                .setImageAcceptedSize(1080, 2080)  //设置广告宽高 单位px
                .build();
    }

    // 加载开屏广告
    private void loadSplashAd(Activity act) {
        Log.d(TAG, "loadSplashAd start");
        TTAdNative adNativeLoader = TTAdSdk.getAdManager().createAdNative(act);
        adNativeLoader.loadSplashAd(buildSplashAdslot(), new TTAdNative.CSJSplashAdListener() {
            @Override
            public void onSplashLoadSuccess(CSJSplashAd csjSplashAd) {

            }

            @Override
            public void onSplashLoadFail(CSJAdError csjAdError) {
                //广告加载失败
            }

            @Override
            public void onSplashRenderSuccess(CSJSplashAd csjSplashAd) {
                //广告渲染成功，在此展示广告
                showSplashAd(csjSplashAd, mAdContainer); //注 ：splashContainer为展示Banner广告的容器
            }

            @Override
            public void onSplashRenderFail(CSJSplashAd csjSplashAd, CSJAdError csjAdError) {
                //广告渲染失败
            }
        }, 3500);
    }

    //展示开屏广告
    private void showSplashAd(CSJSplashAd splashAd, FrameLayout container) {
        if (splashAd == null || container == null) {
            return;
        }

        splashAd.setSplashAdListener(new CSJSplashAd.SplashAdListener() {
            @Override
            public void onSplashAdShow(CSJSplashAd csjSplashAd) {
                //广告展示
                //获取展示广告相关信息，需要再show回调之后进行获取
                MediationBaseManager manager = splashAd.getMediationManager();
                if (manager != null && manager.getShowEcpm() != null) {
                    MediationAdEcpmInfo showEcpm = manager.getShowEcpm();
                    String ecpm = showEcpm.getEcpm(); //展示广告的价格
                    String sdkName = showEcpm.getSdkName();  //展示广告的adn名称
                    String slotId = showEcpm.getSlotId(); //展示广告的代码位ID
                }
            }

            @Override
            public void onSplashAdClick(CSJSplashAd csjSplashAd) {
                //广告点击
            }

            @Override
            public void onSplashAdClose(CSJSplashAd csjSplashAd, int i) {
                //广告关闭
            }
        });
        splashAd.showSplashView(container);//展示开屏广告
    }

}
