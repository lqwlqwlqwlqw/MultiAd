package com.ifmvo.togetherad.csj.provider

import android.app.Activity
import android.os.CountDownTimer
import android.view.View
import android.view.ViewGroup
import com.bytedance.sdk.openadsdk.*
import com.ifmvo.togetherad.core.listener.SplashListener
import com.ifmvo.togetherad.csj.TogetherAdCsj
import kotlin.math.roundToInt

/**
 *
 * Created by Matthew Chen on 2020/11/25.
 */
abstract class CsjProviderSplash : CsjProviderReward() {

    private var mTimer: CountDownTimer? = null

    private var mListener: SplashListener? = null
    private var mAdProviderType: String? = null

    private var mSplashAd: CSJSplashAd? = null
    override fun loadOnlySplashAd(activity: Activity, adProviderType: String, alias: String, listener: SplashListener) {

        mListener = listener
        mAdProviderType = adProviderType

        callbackSplashStartRequest(adProviderType, alias, listener)

        val adSlotBuilder = AdSlot.Builder()
        adSlotBuilder.setCodeId(TogetherAdCsj.idMapCsj[alias])
//        adSlotBuilder.setSplashButtonType(CsjProvider.Splash.splashButtonType)
//        adSlotBuilder.setDownloadType(TogetherAdCsj.downloadType)
        if (CsjProvider.Splash.isExpress) {
            adSlotBuilder.setExpressViewAcceptedSize(CsjProvider.Splash.imageAcceptedSizeWidth.toFloat(), CsjProvider.Splash.imageAcceptedSizeHeight.toFloat())
        } else {
            adSlotBuilder.setImageAcceptedSize(CsjProvider.Splash.imageAcceptedSizeWidth, CsjProvider.Splash.imageAcceptedSizeHeight)
        }

        TogetherAdCsj.mTTAdManager.createAdNative(activity).loadSplashAd(adSlotBuilder.build(), object : TTAdNative.CSJSplashAdListener {

            override fun onSplashLoadSuccess(p0: CSJSplashAd?) {
                callbackSplashLoaded(adProviderType, alias, listener)
            }

            override fun onSplashLoadFail(p0: CSJAdError?) {
                if (p0 == null) {
                    callbackSplashFailed(adProviderType, alias, listener, null, "返回错误空")
                    return
                }

                if (p0.code == 23) {
                    callbackSplashFailed(adProviderType, alias, listener, null, "请求超时了")
                } else {
                    callbackSplashFailed(adProviderType, alias, listener, p0.code, p0.msg)
                }
            }

            override fun onSplashRenderSuccess(p0: CSJSplashAd?) {
                if (p0 == null) {
                    callbackSplashFailed(adProviderType, alias, listener, null, "请求成功，但是返回的广告为null")
                    return
                }

                mSplashAd = p0

                p0.setSplashAdListener(object : CSJSplashAd.SplashAdListener {
                    override fun onSplashAdShow(p0: CSJSplashAd?) {
                        callbackSplashExposure(adProviderType, listener)
                    }

                    override fun onSplashAdClick(p0: CSJSplashAd?) {
                        callbackSplashClicked(adProviderType, listener)
                    }

                    override fun onSplashAdClose(p0: CSJSplashAd?, p1: Int) {
                        CsjProvider.Splash.customSkipView = null
                        callbackSplashDismiss(adProviderType, listener)
                    }
                })
            }

            override fun onSplashRenderFail(p0: CSJSplashAd?, p1: CSJAdError?) {
                if (p1 != null) {
                    callbackSplashFailed(adProviderType, alias, listener, p1.code, p1.msg)
                }
            }

//            override fun onTimeout() {
//                callbackSplashFailed(adProviderType, alias, listener, null, "请求超时了")
//            }
//
//            override fun onError(errorCode: Int, errorMsg: String?) {
//                callbackSplashFailed(adProviderType, alias, listener, errorCode, errorMsg)
//            }
        }, CsjProvider.Splash.maxFetchDelay)//超时时间，demo 为 3000
    }

    override fun showSplashAd(container: ViewGroup): Boolean {

        if (mSplashAd?.splashView == null) {
            return false
        }

        container.removeAllViews()
        container.addView(mSplashAd!!.splashView)

        val customSkipView = CsjProvider.Splash.customSkipView
        val skipView = customSkipView?.onCreateSkipView(container.context)

        if (customSkipView != null) {
//            mSplashAd?.setNotAllowSdkCountdown()
            skipView?.run {
                container.addView(this, customSkipView.getLayoutParams())
                setOnClickListener {
                    mTimer?.cancel()
                    if (mAdProviderType != null && mListener != null) {
                        CsjProvider.Splash.customSkipView = null
                        callbackSplashDismiss(mAdProviderType!!, mListener!!)
                    }
                }
            }

            //开始倒计时
            mTimer?.cancel()
            mTimer = object : CountDownTimer(5000, 1000) {
                override fun onFinish() {
                    if (mAdProviderType != null && mListener != null) {
                        CsjProvider.Splash.customSkipView = null
                        callbackSplashDismiss(mAdProviderType!!, mListener!!)
                    }
                }

                override fun onTick(millisUntilFinished: Long) {
                    val second = (millisUntilFinished / 1000f).roundToInt()
                    customSkipView.handleTime(second)
                }
            }
            mTimer?.start()
        }

        return true
    }

    override fun loadAndShowSplashAd(activity: Activity, adProviderType: String, alias: String, container: ViewGroup, listener: SplashListener) {

        callbackSplashStartRequest(adProviderType, alias, listener)

        val customSkipView = CsjProvider.Splash.customSkipView
        val skipView = customSkipView?.onCreateSkipView(activity)

        val adSlotBuilder = AdSlot.Builder()
//        adSlotBuilder.setDownloadType(TogetherAdCsj.downloadType)
        adSlotBuilder.setCodeId(TogetherAdCsj.idMapCsj[alias])
        if (CsjProvider.Splash.isExpress) {
            adSlotBuilder.setExpressViewAcceptedSize(CsjProvider.Splash.imageAcceptedSizeWidth.toFloat(), CsjProvider.Splash.imageAcceptedSizeHeight.toFloat())
        } else {
            adSlotBuilder.setImageAcceptedSize(CsjProvider.Splash.imageAcceptedSizeWidth, CsjProvider.Splash.imageAcceptedSizeHeight)
        }

        TogetherAdCsj.mTTAdManager.createAdNative(activity).loadSplashAd(adSlotBuilder.build(), object : TTAdNative.CSJSplashAdListener {
            override fun onSplashLoadSuccess(p0: CSJSplashAd?) {
                callbackSplashLoaded(adProviderType, alias, listener)
            }

            override fun onSplashLoadFail(p0: CSJAdError?) {
                if (p0 == null) {
                    callbackSplashFailed(adProviderType, alias, listener, null, "返回错误空")
                    return
                }

                if (p0.code == 23) {
                    callbackSplashFailed(adProviderType, alias, listener, null, "请求超时了")
                } else {
                    callbackSplashFailed(adProviderType, alias, listener, p0.code, p0.msg)
                }
            }

            override fun onSplashRenderSuccess(p0: CSJSplashAd?) {
                if (p0 == null) {
                    callbackSplashFailed(adProviderType, alias, listener, null, "请求成功，但是返回的广告为null")
                    return
                }

                container.removeAllViews()
                container.addView(p0.splashView)

                p0.setSplashAdListener(object : CSJSplashAd.SplashAdListener {
                    override fun onSplashAdShow(p0: CSJSplashAd?) {
                        callbackSplashExposure(adProviderType, listener)
                    }

                    override fun onSplashAdClick(p0: CSJSplashAd?) {
                        callbackSplashClicked(adProviderType, listener)
                    }

                    override fun onSplashAdClose(p0: CSJSplashAd?, p1: Int) {
                        CsjProvider.Splash.customSkipView = null
                        callbackSplashDismiss(adProviderType, listener)
                    }
                })

                //自定义跳过按钮和计时逻辑
                if (customSkipView != null) {
//                    p0.setNotAllowSdkCountdown()
                    skipView?.run {
                        container.addView(this, customSkipView.getLayoutParams())
                        setOnClickListener {
                            mTimer?.cancel()
                            CsjProvider.Splash.customSkipView = null
                            callbackSplashDismiss(adProviderType, listener)
                        }
                    }

                    //开始倒计时
                    mTimer?.cancel()
                    mTimer = object : CountDownTimer(5000, 1000) {
                        override fun onFinish() {
                            CsjProvider.Splash.customSkipView = null
                            callbackSplashDismiss(adProviderType, listener)
                        }

                        override fun onTick(millisUntilFinished: Long) {
                            val second = (millisUntilFinished / 1000f).roundToInt()
                            customSkipView.handleTime(second)
                        }
                    }
                    mTimer?.start()
                }
            }

            override fun onSplashRenderFail(p0: CSJSplashAd?, p1: CSJAdError?) {
                if (p1 != null) {
                    callbackSplashFailed(adProviderType, alias, listener, p1.code, p1.msg)
                }
            }
        }, CsjProvider.Splash.maxFetchDelay)//超时时间，demo 为 3000
    }

}