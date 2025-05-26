package com.paintology.lite.trace.drawing.ads.callbacks

interface InterstitialOnLoadCallBack {
    fun onAdFailedToLoad(adError: String)
    fun onAdLoaded()
    fun onPreloaded()
}