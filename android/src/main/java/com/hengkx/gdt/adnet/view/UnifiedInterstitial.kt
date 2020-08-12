package com.hengkx.gdt.adnet.view

import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD;
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener;
import com.qq.e.comm.util.AdError;

import java.util.HashMap;
import java.util.Map;

class UnifiedInterstitial private constructor(private val mContext: ReactApplicationContext) : UnifiedInterstitialADListener {
  private var iad: UnifiedInterstitialAD? = null
  private var posID: String? = null
  private var asPopup = false
  override fun onNoAD(adError: AdError) {
    Log.e(TAG, "onNoAD: eCode=" + adError.getErrorCode().toString() + ",eMsg=" + adError.getErrorMsg())
    val event: WritableMap = Arguments.createMap()
    event.putString("error", "BannerNoAD，eCode=$adError")
  }

  override fun onADReceive() {
    Log.e(TAG, "onADReceive")
    if (iad != null) {
      if (asPopup) {
        iad!!.showAsPopupWindow()
      } else {
        iad!!.show()
      }
    }
  }

  override fun onADOpened() {
    Log.e(TAG, "onADOpened")
  }

  override fun onADExposure() {
    Log.e(TAG, "onADExposure")
  }

  override fun onVideoCached() {
    TODO("Not yet implemented")
  }

  override fun onADClosed() {
    Log.e(TAG, "onADClosed")
  }

  override fun onADClicked() {
    Log.e(TAG, "onADClicked")
  }

  override fun onADLeftApplication() {
    Log.e(TAG, "onADLeftApplication")
  }

  fun showUnifiedInterstitialAD(posID: String, asPopup: Boolean) {
    Log.e(TAG, "showUnifiedInterstitialAD")
    this.asPopup = asPopup
    getIAD(posID).loadAD()
  }

  private fun getIAD(posID: String): UnifiedInterstitialAD {
    if (iad != null && this.posID == posID) {
      Log.e(TAG, "======相同IAD无需创建新的======")
      return iad as UnifiedInterstitialAD
    }
    this.posID = posID
    if (iad != null) {
      iad!!.close()
      iad!!.destroy()
      iad = null
    }
    iad = UnifiedInterstitialAD(mContext.currentActivity, posID, this)
    return iad!!
  }

  companion object {
    private val TAG = UnifiedInterstitial::class.java.simpleName
    private var mInstance: UnifiedInterstitial? = null

    @Synchronized
    fun getInstance(reactContext: ReactApplicationContext): UnifiedInterstitial? {
      if (mInstance == null) {
        mInstance = UnifiedInterstitial(reactContext)
      }
      return mInstance
    }
  }
}
