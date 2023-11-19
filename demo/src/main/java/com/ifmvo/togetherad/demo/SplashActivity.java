package com.ifmvo.togetherad.demo;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    public SplashActivity() {
        super();
    }

    public SplashActivity(int contentLayoutId) {
        super(contentLayoutId);
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
        TTAdNative adNativeLoader = TTAdSdk.getAdManager().createAdNative(act);
        adNativeLoader.loadSplashAd(buildSplashAdslot(), new TTAdNative.CSJSplashAdListener() {
            @Override
            public void onSplashLoadSuccess() {
                //广告加载成功
            }

            @Override
            public void onSplashLoadFail(CSJAdError csjAdError) {
                //广告加载失败
            }

            @Override
            public void onSplashRenderSuccess(CSJSplashAd csjSplashAd) {
                //广告渲染成功，在此展示广告
                showSplashAd(csjSplashAd, splashContainer); //注 ：splashContainer为展示Banner广告的容器
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
