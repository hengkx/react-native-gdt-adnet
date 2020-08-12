package com.hengkx.gdt.adnet

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.hengkx.gdt.adnet.view.UnifiedInterstitial
import com.qq.e.comm.managers.GDTADManager


class GdtAdModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

  private var mReactContext: ReactApplicationContext? = null

  init {
    this.mReactContext = reactContext
  }

  override fun getName(): String {
    return "GdtAd"
  }

  @ReactMethod
  fun init(appId: String, promise: Promise) {
    var res = GDTADManager.getInstance().initWith(mReactContext, appId);
    promise.resolve(res)
  }

  @ReactMethod
  fun showUnifiedInterstitialAD(posID: String?, asPopup: Boolean) {
    val unifiedInterstitial = UnifiedInterstitial.getInstance(mReactContext!!)
    unifiedInterstitial!!.showUnifiedInterstitialAD(posID!!, asPopup)
  }
}
