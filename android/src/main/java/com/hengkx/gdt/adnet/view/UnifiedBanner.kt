package com.hengkx.gdt.adnet.view

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.hengkx.gdt.adnet.R
import com.qq.e.ads.banner2.UnifiedBannerADListener
import com.qq.e.ads.banner2.UnifiedBannerView


class UnifiedBanner(context: Context, posID: String, listener: UnifiedBannerADListener?) : FrameLayout(context) {
  var banner: UnifiedBannerView? = null
  private var mLayoutRunnable: Runnable? = null

  private fun closeBanner() {
    removeAllViews()
    if (banner != null) {
      banner!!.destroy()
      banner = null
      Log.e("UnifiedBanner", "关闭广告")
    }
    if (mLayoutRunnable != null) {
      removeCallbacks(mLayoutRunnable)
    }
  }

  fun setInterval(interval: Int) {
    banner!!.setRefresh(interval)
  }

  override fun onDetachedFromWindow() {
    closeBanner()
    super.onDetachedFromWindow()
  }

  override fun requestLayout() {
    super.requestLayout()
    if (mLayoutRunnable != null) {
      removeCallbacks(mLayoutRunnable)
    }
    mLayoutRunnable = Runnable {
      measure(
        MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
        MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY))
      layout(left, top, right, bottom)
    }
    post(mLayoutRunnable)
  }

  init {
    // 把布局加载到这个View里面
    View.inflate(context, R.layout.layout_banner, this)
    closeBanner()
    banner = UnifiedBannerView(this.context as Activity, posID, listener)
    banner!!.setRefresh(30)
    addView(banner)
    banner!!.loadAD()
  }
}
