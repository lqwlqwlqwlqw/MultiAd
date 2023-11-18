package com.ifmvo.togetherad.gdt

import android.content.Context
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.entity.AdProviderEntity
import com.ifmvo.togetherad.core.utils.logi
import com.ifmvo.togetherad.gdt.provider.GdtProvider
import com.qq.e.comm.compliance.DownloadConfirmListener
import com.qq.e.comm.managers.GDTAdSdk
import com.qq.e.comm.managers.setting.GlobalSetting
import org.jetbrains.annotations.NotNull


/**
 * 初始化广点通
 *
 * Created by Matthew Chen on 2020-04-04.
 */
object TogetherAdGdt {

    var idMapGDT = mutableMapOf<String, String>()

    //下载确认的回调，可参考 DownloadConfirmHelper 进行自定义
    var downloadConfirmListener: DownloadConfirmListener? = null

    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull gdtAdAppId: String) {
        init(context, adProviderType, gdtAdAppId, null, null)
    }

    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull gdtAdAppId: String, providerClassPath: String? = null) {
        init(context, adProviderType, gdtAdAppId, null, providerClassPath)
    }

    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull gdtAdAppId: String, gdtIdMap: Map<String, String>? = null) {
        init(context, adProviderType, gdtAdAppId, gdtIdMap, null)
    }

    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull gdtAdAppId: String, gdtIdMap: Map<String, String>? = null, providerClassPath: String? = null) {

        val params: HashMap<String, Boolean> = HashMap<String, Boolean>()
        // 优量汇SDK将不采集mac地址
        params["mac_address"] = false

        // 不允许优量汇SDK采集android_id
        params["android_id"] = false

        // 唤起行为能力，用于设置应用安装完成后是否显示打开应用的提示弹窗，不设置或设置为true都显示打开应用弹窗，设置为false不显示弹窗，也不会自动打开应用
        params["soate"] = false

        GlobalSetting.setAgreeReadPrivacyInfo(params)

        TogetherAd.addProvider(AdProviderEntity(adProviderType, if (providerClassPath?.isEmpty() != false) GdtProvider::class.java.name else providerClassPath))
        gdtIdMap?.let { idMapGDT.putAll(it) }
        GDTAdSdk.init(context, gdtAdAppId)

        "gdt 初始化完毕".logi()
    }
}