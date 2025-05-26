package com.paintology.lite.trace.drawing.ads

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.paintology.lite.trace.drawing.BuildConfig
import com.paintology.lite.trace.drawing.ads.constants.AdsConstants.isOpenAdLoading
import com.paintology.lite.trace.drawing.ads.constants.AdsConstants.mAppOpenAd
import com.paintology.lite.trace.drawing.ads.koin.DIComponent
import com.paintology.lite.trace.drawing.minipaint.FirstScreen
import java.util.Date


class AdmobAppOpen(private val myApplication: Application) : LifecycleObserver,
    ActivityLifecycleCallbacks {
    private var currentActivity: Activity? = null
    private var loadTime: Long = 0
    private val diComponent by lazy { DIComponent() }

    /**
     * 0 = Ads Off
     * 1 = Ads On
     */

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        try {
            if (!diComponent.sharedPreferenceUtils.isAppPurchased && diComponent.sharedPreferenceUtils.rcvOpenAd != 0) {
                showAdIfAvailable()
            }
        } catch (ignored: Exception) {
        }
    }

    fun fetchAd() {
        // Have unused ad, no need to fetch another.
        if (isAdAvailable) {
            return
        }
        val loadCallback: AppOpenAdLoadCallback = object : AppOpenAdLoadCallback() {
            override fun onAdLoaded(appOpenAd: AppOpenAd) {
                super.onAdLoaded(appOpenAd)
                isOpenAdLoading = false
                mAppOpenAd = appOpenAd
                Log.i("AdsInformation", "appOpen onAdLoaded")

                mAppOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        Log.d("AdsInformation", "appOpen onAdDismissedFullScreenContent")
                        mAppOpenAd = null
                        isShowingAd = false
                        fetchAd()
                        if (appOpenListener != null) {
                            appOpenListener?.onOpenAdClosed()
                        }
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        if (appOpenListener != null) {
                            appOpenListener?.onOpenAdClosed()
                            Log.e("AdsInformation", "appOpen onAdFailedToShowFullScreenContent")
                        }
                    }

                    override fun onAdShowedFullScreenContent() {
                        Log.d("AdsInformation", "appOpen onAdShowedFullScreenContent")
                        isShowingAd = true
                    }
                }
                loadTime = Date().time
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                super.onAdFailedToLoad(loadAdError)
                Log.e("AdsInformation", "appOpen onAdFailedToLoad ${loadAdError.message}")
                isOpenAdLoading = false
                mAppOpenAd = null
            }
        }

        if (!diComponent.sharedPreferenceUtils.isAppPurchased && diComponent.sharedPreferenceUtils.rcvOpenAd != 0) {
            if (mAppOpenAd == null && !isOpenAdLoading) {
                isOpenAdLoading = true
                try {
                    AppOpenAd.load(
                        myApplication,
                        if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/9257395921" else diComponent.sharedPreferenceUtils.rcvOpenAdID,
                        AdRequest.Builder().build(),
                        loadCallback
                    )
                } catch (ignored: Exception) {
                }
            }
        }
    }

    private fun showAdIfAvailable() {
        try {
            if (!diComponent.sharedPreferenceUtils.isAppPurchased && diComponent.sharedPreferenceUtils.rcvOpenAd != 0) {
                if (currentActivity is FirstScreen || currentActivity is AdActivity)
                    return

                mAppOpenAd?.show(currentActivity!!)
            } else {
                fetchAd()
            }
        } catch (ignored: Exception) {
        }
    }

    private var appOpenListener: AppOpenListener? = null

    interface AppOpenListener {
        fun onOpenAdClosed()
    }

    private fun wasLoadTimeLessThanNHoursAgo(): Boolean {
        val dateDifference = Date().time - loadTime
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numMilliSecondsPerHour * 4.toLong()
    }

    private val isAdAvailable: Boolean
        get() = mAppOpenAd != null && wasLoadTimeLessThanNHoursAgo()

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityStopped(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        currentActivity = null
    }

    companion object {
        private var isShowingAd = false
    }

    init {
        this.myApplication.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

}