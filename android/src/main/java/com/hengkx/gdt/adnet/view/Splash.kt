package com.hengkx.gdt.adnet.view

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.qq.e.ads.splash.SplashAD
import com.qq.e.ads.splash.SplashADListener
import com.hengkx.gdt.adnet.adnet.R
import java.util.*
import kotlin.collections.HashMap


class Splash(context: Context?, posID: String, listener: SplashADListener, fetchDelay: Int) : RelativeLayout(context) {
  private val container: ViewGroup
  private val skipView: TextView?
//  private val logoView: View?
  private val splashHolder: ImageView?
  private var mSplashAD: SplashAD? = null
  private var mLayoutRunnable: Runnable? = null

  /**
   * 初始化View
   */
  private fun initView(posID: String, listener: SplashADListener, fetchDelay: Int) {
    mSplashAD = SplashAD(this.context as Activity, posID, listener, fetchDelay)
    mSplashAD!!.showAd(container!!)
  }

//  fun showLogo(show: Boolean) {
//    if (logoView != null) {
//      logoView.visibility = if (show) View.VISIBLE else View.GONE
//    }
//  }

  fun hideSplashHolder(hide: Boolean) {
    if (splashHolder != null) {
      splashHolder.setVisibility(if (hide) View.INVISIBLE else View.VISIBLE)
    }
  }

  fun setTickTxt(timeLeft: Long) {
    skipView?.setText(java.lang.String.format(Locale.CHINA, SKIP_TEXT, Math.round(timeLeft / 1000f)))
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

  companion object {
    private const val SKIP_TEXT = "点击跳过 %d"
  }

  init {
    // 把布局加载到这个View里面
    View.inflate(context, R.layout.layout_splash, this)
    container = findViewById(R.id.splash_container)
    skipView = findViewById(R.id.skip_view)
//    logoView = findViewById(R.id.app_logo)
    splashHolder = findViewById(R.id.splash_holder)
    initView(posID, listener, fetchDelay)
  }
}
