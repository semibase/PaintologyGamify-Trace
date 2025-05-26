package com.paintology.lite.trace.drawing.ads.callbacks

interface RewardedOnShowCallBack {
    fun onAdClicked()
    fun onAdDismissedFullScreenContent()
    fun onAdFailedToShowFullScreenContent()
    fun onAdImpression()
    fun onAdShowedFullScreenContent()
    fun onUserEarnedReward()
}