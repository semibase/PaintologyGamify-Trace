package com.paintology.lite.trace.drawing.ads.callbacks

interface RewardedOnLoadCallBack {
    fun onAdFailedToLoad(adError:String)
    fun onAdLoaded()
    fun onPreloaded()
}