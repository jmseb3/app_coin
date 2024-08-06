package com.wonddak.coinaverage.util

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.wonddak.coinaverage.R

class AdManager(
    private val context: Activity
) {
    companion object {
        private const val TAG = "JWH"
    }

    private var adRequest: AdRequest? = null
    private var rewardedAd: RewardedAd? = null


    fun initAd() {
        adRequest = AdRequest.Builder().build()
        MobileAds.initialize(context) {}
        RewardedAd.load(
            context,
            context.getString(R.string.reward_ad_unit_id),
            adRequest!!,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d("JWH", adError.toString())
                    rewardedAd = null
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    Log.d("JWH", "Ad was loaded.")
                    rewardedAd = ad
                    rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdClicked() {
                            // Called when a click is recorded for an ad.
                            Log.d(TAG, "Ad was clicked.")
                        }

                        override fun onAdDismissedFullScreenContent() {
                            // Called when ad is dismissed.
                            // Set the ad reference to null so you don't show the ad a second time.
                            Log.d(TAG, "Ad dismissed fullscreen content.")
                            rewardedAd = null
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            // Called when ad fails to show.
                            Log.e(TAG, "Ad failed to show fullscreen content.")
                            rewardedAd = null
                        }

                        override fun onAdImpression() {
                            // Called when an impression is recorded for an ad.
                            Log.d(TAG, "Ad recorded an impression.")
                        }

                        override fun onAdShowedFullScreenContent() {
                            // Called when ad is shown.
                            Log.d(TAG, "Ad showed fullscreen content.")
                        }
                    }

                }
            })
    }

    fun showRewardAd(action: (RewardItem) -> Unit) {
        rewardedAd?.let { ad ->
            ad.show(
                context
            ) { rewardItem ->
                action(rewardItem)
            }
        } ?: run {
            Log.d("JWH", "The rewarded ad wasn't ready yet.")
            Toast.makeText(context, "현재 준비된 광고가 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}