package com.lqw.gromore;

import android.content.Context;

import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTCustomController;
import com.bytedance.sdk.openadsdk.mediation.init.MediationPrivacyConfig;

public class GroMoreMain {
    //初始化聚合sdk
    private void initMediationAdSdk(Context context) {
        TTAdSdk.init(context, buildConfig(context));
        TTAdSdk.start(new TTAdSdk.Callback() {
            @Override
            public void success() {
                //初始化成功
                //在初始化成功回调之后进行广告加载
            }

            @Override
            public void fail(int i, String s) {
                //初始化失败
            }
        });
    }

    // 构造TTAdConfig
    private TTAdConfig buildConfig(Context context) {
        return new TTAdConfig.Builder()
                .appId("5197188") //APP ID
                .useMediation(true)  //开启聚合功能
                .debug(true)  //打开debug开关
                .themeStatus(0)  //正常模式  0是正常模式；1是夜间模式；
                /**
                 * 多进程增加注释说明：V>=5.1.6.0支持多进程，如需开启可在初始化时设置.supportMultiProcess(true) ，默认false；
                 * 注意：开启多进程开关时需要将ADN的多进程也开启，否则广告展示异常，影响收益。
                 * CSJ、gdt无需额外设置，KS、baidu、Sigmob、Mintegral需要在清单文件中配置各家ADN激励全屏xxxActivity属性android:multiprocess="true"
                 */
                .supportMultiProcess(false)  //不支持
                .customController(getTTCustomController())  //设置隐私权
                .build();
    }
    //设置隐私合规
    private TTCustomController getTTCustomController() {
        return new TTCustomController() {
            @Override
            public boolean isCanUseLocation() {  //是否授权位置权限
                return false;
            }

            @Override
            public boolean isCanUsePhoneState() {  //是否授权手机信息权限
                return false;
            }

            @Override
            public boolean isCanUseWifiState() {  //是否授权wifi state权限
                return true;
            }

            @Override
            public boolean isCanUseWriteExternal() {  //是否授权写外部存储权限
                return false;
            }

            @Override
            public boolean isCanUseAndroidId() {  //是否授权Android Id权限
                return false;
            }

            @Override
            public MediationPrivacyConfig getMediationPrivacyConfig() {
                return new MediationPrivacyConfig() {
                    @Override
                    public boolean isLimitPersonalAds() {  //是否限制个性化广告
                        return false;
                    }

                    @Override
                    public boolean isProgrammaticRecommend() {  //是否开启程序化广告推荐
                        return true;
                    }
                };
            }
        };
    }
}
