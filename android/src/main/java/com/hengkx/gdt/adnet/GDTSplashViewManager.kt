package com.hengkx.gdt.adnet

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.Nullable

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.google.gson.Gson;
import com.hengkx.gdt.adnet.view.Splash
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;


class GDTSplashViewManager : SimpleViewManager<View>(), SplashADListener {
  private var TAG = "GDTSplashViewManager"
  // 重写getName()方法, 返回的字符串就是RN中使用该组件的名称
  override fun getName(): String {
    return TAG
  }

  override fun onNoAD(adError: AdError) {
    Log.e(TAG, "onNoAD: eCode=" + adError.getErrorCode().toString() + ",eMsg=" + adError.getErrorMsg())
    val event: WritableMap = Arguments.createMap()
    event.putString("error", Gson().toJson(adError))
    mEventEmitter!!.receiveEvent(mContainer!!.id, Events.EVENT_FAIL_TO_RECEIVED.toString(), event)
    val alreadyDelayMills = System.currentTimeMillis() - fetchSplashADTime //从拉广告开始到onNoAD已经消耗了多少时间
    //为防止加载广告失败后立刻跳离开屏可能造成的视觉上类似于"闪退"的情况，根据设置的minSplashTimeWhenNoAD 计算出还需要延时多久
    val shouldDelayMills = if (alreadyDelayMills > minSplashTimeWhenNoAD) 0 else minSplashTimeWhenNoAD - alreadyDelayMills
    Log.e(TAG, "shouldDelayMills: $shouldDelayMills")
    handler.postDelayed(Runnable { nextAction() }, shouldDelayMills)
  }

  override fun onADPresent() {
    Log.e(TAG, "onADPresent")
    if (mSplash != null) {
      mSplash!!.hideSplashHolder(true)
      mSplash!!.requestLayout()
    }
    mEventEmitter!!.receiveEvent(mContainer!!.id, Events.EVENT_PRESENT.toString(), null)
  }

  override fun onADLoaded(p0: Long) {
    Log.e(TAG, "onADLoaded" + p0)
  }

  override fun onADExposure() {
    Log.e(TAG, "onADExposure")
    mEventEmitter!!.receiveEvent(mContainer!!.id, Events.EVENT_WILL_EXPOSURE.toString(), null)
  }

  override fun onADDismissed() {
    Log.e(TAG, "onADDismissed")
    mEventEmitter!!.receiveEvent(mContainer!!.id, Events.EVENT_WILL_DISMISSED.toString(), null)
    nextAction()
  }

  override fun onADClicked() {
    Log.e(TAG, "onADClicked")
    mEventEmitter!!.receiveEvent(mContainer!!.id, Events.EVENT_ON_CLICK.toString(), null)
  }

  override fun onADTick(millisUntilFinished: Long) {
    Log.e(TAG, "onADTick " + millisUntilFinished + "ms")
    if (mSplash != null) {
      mSplash!!.setTickTxt(millisUntilFinished)
    }
    val event: WritableMap = Arguments.createMap()
    event.putString("timeLeft", millisUntilFinished.toString() + "")
    mEventEmitter!!.receiveEvent(mContainer!!.id, Events.EVENT_ON_TICK.toString(), event)
  }

  private fun nextAction() {
    mEventEmitter!!.receiveEvent(mContainer!!.id, Events.EVENT_NEXT_ACTION.toString(), null)
  }

  enum class Events(private val mName: String) {
    EVENT_FAIL_TO_RECEIVED("onFailToReceived"), EVENT_PRESENT("onPresent"), EVENT_WILL_DISMISSED("onDismissed"), EVENT_NEXT_ACTION("onNextAction"), EVENT_WILL_EXPOSURE("onViewWillExposure"), EVENT_ON_CLICK("onClicked"), EVENT_ON_TICK("onTick"), EVENT_WILL_LEAVE_APP("onViewWillLeaveApplication"), EVENT_WILL_OPEN_FULL_SCREEN("onViewWillPresentFullScreenModal"), EVENT_DID_OPEN_FULL_SCREEN("onViewDidPresentFullScreenModal"), EVENT_WILL_CLOSE_FULL_SCREEN("onViewWillDismissFullScreenModal"), EVENT_DID_CLOSE_FULL_SCREEN("onViewDidDismissFullScreenModal");

    override fun toString(): String {
      return mName
    }

  }

  private var mContainer: FrameLayout? = null
  private var mEventEmitter: RCTEventEmitter? = null
  private var mThemedReactContext: ThemedReactContext? = null
  private var mSplash: Splash? = null
  private var fetchDelay = 0
  private val minSplashTimeWhenNoAD = 2000
  private var fetchSplashADTime: Long = 0
  private val handler: Handler = Handler(Looper.getMainLooper())
  override fun createViewInstance(reactContext: ThemedReactContext): View {
    mThemedReactContext = reactContext
    mEventEmitter = reactContext.getJSModule(RCTEventEmitter::class.java)
    val viewGroup = FrameLayout(reactContext)
    mContainer = viewGroup
    return viewGroup
  }

  override fun getExportedCustomDirectEventTypeConstants(): Map<String, Any>? {
    val builder = MapBuilder.builder<String, Any>()
    for (event in Events.values()) {
      builder.put(event.toString(), MapBuilder.of("registrationName", event.toString()))
    }
    return builder.build()
  }

  // 其中，可以通过@ReactProp（或@ReactPropGroup）注解来导出属性的设置方法。
  // 该方法有两个参数，第一个参数是泛型View的实例对象，第二个参数是要设置的属性值。
  // 方法的返回值类型必须为void，而且访问控制必须被声明为public。
  // 组件的每一个属性的设置都会调用Java层被对应ReactProp注解的方法
  @ReactProp(name = "posId")
  fun setPosId(view: FrameLayout, posId: String) {
    Log.e(TAG, "fetchDelay: $fetchDelay")
    fetchSplashADTime = System.currentTimeMillis()
    val splash = Splash(mThemedReactContext!!.currentActivity, posId, this, fetchDelay)
    mSplash = splash
    view.addView(splash)
  }

//  @ReactProp(name = "showLogo")
//  fun showLogo(view: FrameLayout?, show: Boolean) {
//    if (mSplash != null) {
//      mSplash!!.showLogo(show)
//    }
//  }

  /***
   * 需要放在 appInfo 属性之后设置才能生效
   * @param view
   * @param delay
   */
  @ReactProp(name = "fetchDelay")
  fun setFetchDelay(view: FrameLayout?, delay: Int) {
    fetchDelay = delay
  }

}
