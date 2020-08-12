package com.hengkx.gdt.adnet

import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.Nullable
import com.facebook.react.bridge.Arguments
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.uimanager.events.RCTEventEmitter
import com.google.gson.Gson
import com.hengkx.gdt.adnet.view.UnifiedBanner
import com.qq.e.ads.banner2.UnifiedBannerADListener
import com.qq.e.comm.util.AdError


class GDTUnifiedBannerViewManager : SimpleViewManager<View>(), UnifiedBannerADListener {
  private var TAG = "GDTUnifiedBannerViewManager"
  private var mContainer: FrameLayout? = null
  private var mEventEmitter: RCTEventEmitter? = null
  private var mThemedReactContext: ThemedReactContext? = null
  private var mBanner: UnifiedBanner? = null

  // 重写getName()方法, 返回的字符串就是RN中使用该组件的名称
  override fun getName(): String {
    return TAG
  }

  override fun onNoAD(adError: AdError) {
    Log.e(TAG, "onNoAD: eCode=" + adError.errorCode + ",eMsg=" + adError.errorMsg)
    val event = Arguments.createMap()
    event.putString("error", Gson().toJson(adError))
    mEventEmitter!!.receiveEvent(mContainer!!.id, Events.EVENT_FAIL_TO_RECEIVED.toString(), event)
  }

  override fun onADReceive() {
    Log.e(TAG, "onADReceive")
    mEventEmitter!!.receiveEvent(mContainer!!.id, Events.EVENT_RECEIVED.toString(), null)
  }

  override fun onADExposure() {
    Log.e(TAG, "onADExposure")
    mEventEmitter!!.receiveEvent(mContainer!!.id, Events.EVENT_WILL_EXPOSURE.toString(), null)
  }

  override fun onADClosed() {
    Log.e(TAG, "onADClosed")
    mEventEmitter!!.receiveEvent(mContainer!!.id, Events.EVENT_WILL_CLOSE.toString(), null)
  }

  override fun onADClicked() {
    Log.e(TAG, "onADClicked")
    mEventEmitter!!.receiveEvent(mContainer!!.id, Events.EVENT_ON_CLICK.toString(), null)
  }

  override fun onADLeftApplication() {
    Log.e(TAG, "onADLeftApplication")
    mEventEmitter!!.receiveEvent(mContainer!!.id, Events.EVENT_WILL_LEAVE_APP.toString(), null)
  }

  override fun onADOpenOverlay() {
    Log.e(TAG, "onADOpenOverlay")
    mEventEmitter!!.receiveEvent(mContainer!!.id, Events.EVENT_WILL_OPEN_FULL_SCREEN.toString(), null)
    mEventEmitter!!.receiveEvent(mContainer!!.id, Events.EVENT_DID_OPEN_FULL_SCREEN.toString(), null)
  }

  override fun onADCloseOverlay() {
    Log.e(TAG, "onADCloseOverlay")
    mEventEmitter!!.receiveEvent(mContainer!!.id, Events.EVENT_WILL_CLOSE_FULL_SCREEN.toString(), null)
    mEventEmitter!!.receiveEvent(mContainer!!.id, Events.EVENT_DID_CLOSE_FULL_SCREEN.toString(), null)
  }

  enum class Events(private val mName: String) {
    EVENT_FAIL_TO_RECEIVED("onFailToReceived"), EVENT_RECEIVED("onReceived"), EVENT_WILL_LEAVE_APP("onViewWillLeaveApplication"), EVENT_WILL_CLOSE("onViewWillClose"), EVENT_WILL_EXPOSURE("onViewWillExposure"), EVENT_ON_CLICK("onClicked"), EVENT_WILL_OPEN_FULL_SCREEN("onViewWillPresentFullScreenModal"), EVENT_DID_OPEN_FULL_SCREEN("onViewDidPresentFullScreenModal"), EVENT_WILL_CLOSE_FULL_SCREEN("onViewWillDismissFullScreenModal"), EVENT_DID_CLOSE_FULL_SCREEN("onViewDidDismissFullScreenModal");

    override fun toString(): String {
      return mName
    }

  }


  override fun createViewInstance(reactContext: ThemedReactContext): View {
    mThemedReactContext = reactContext
    mEventEmitter = reactContext.getJSModule(RCTEventEmitter::class.java)
    val container = FrameLayout(reactContext)
    mContainer = container
    return container
  }

  @Nullable
  override fun getExportedCustomDirectEventTypeConstants(): Map<String, Any>? {
    val builder = MapBuilder.builder<String, Any>()
    for (event in Events.values()) {
      builder.put(event.toString(), MapBuilder.of("registrationName", event.toString()))
    }
    return builder.build()
  }

  @ReactProp(name = "posId")
  fun setPosId(view: FrameLayout, posId: String) {
    val banner = UnifiedBanner(mThemedReactContext!!.currentActivity!!, posId, this)
    mBanner = banner
    view.addView(banner)
  }

  @ReactProp(name = "interval")
  fun setInterval(view: FrameLayout?, interval: Int) {
    if (mBanner != null) mBanner!!.setInterval(interval)
  }
}
