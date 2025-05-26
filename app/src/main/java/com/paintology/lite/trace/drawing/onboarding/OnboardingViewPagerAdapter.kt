package com.paintology.lite.trace.drawing.onboarding

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.paintology.lite.trace.drawing.Model.SlideInfo


class OnboardingViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val context: Context,
    private val fromMain: Boolean,
    private val sliders: ArrayList<SlideInfo>
) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {

        return if (!fromMain && position == 1) {
            val bundle = Bundle()
            bundle.putFloat("elevation", 0f)
            bundle.putBoolean("hide_heading", false)
            LanguageFragment.newInstance(bundle)
        } else {
            val slideInfo = sliders[position]
            OnboardingFragment.newInstance(
                slideInfo.slideTitle,
                slideInfo.slideDescription,
                slideInfo.slideImageUrl,
                position,
                fromMain
            )
        }
//        return when (position) {
//            0 -> OnboardingFragment.newInstance(
//                context.resources.getString(R.string.title_onboarding_1),
//                context.resources.getString(R.string.description_onboarding_1),
//                R.raw.lottie_delivery_boy_bumpy_ride
//            )
//            1 -> OnboardingFragment.newInstance(
//                context.resources.getString(R.string.title_onboarding_2),
//                context.resources.getString(R.string.description_onboarding_2),
//                R.raw.lottie_developer
//            )
//            else -> OnboardingFragment.newInstance(
//                context.resources.getString(R.string.title_onboarding_3),
//                context.resources.getString(R.string.description_onboarding_3),
//                R.raw.lottie_girl_with_a_notebook
//            )
//        }
    }

    override fun getItemCount(): Int {
        return sliders.size
    }

    fun getCount(): Int {
        return sliders.size
    }
}