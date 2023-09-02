package com.example.imageshering.ui.onboarding

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.imageshering.App
import com.example.imageshering.data.AppAuthComponent
import com.example.imageshering.databinding.ActivityOnboardingBinding
import kotlin.math.max
import kotlin.math.min

class OnBoardingActivity : AppCompatActivity(), ViewPagerHandler {
    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var fragments: List<Fragment>
    private lateinit var appAuthComponent: AppAuthComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appAuthComponent = App.daggerComponent.getAppAuthComponent()
        if (appAuthComponent.isAuthorized)
            Log.d("RDA", "onCreate: Authorized")
        else
            Log.d("RDA", "onCreate: Not uthorized")

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fragments = listOf(
            FirstOnBoardingFragment(),
            SecondOnBoardingFragment(),
            ThirdOnBoardingFragment(),
            LoginFragment(),
        )
        initialUi()
    }

    private fun initialUi() {
        binding.onboardingViewPager.adapter = OnBoardingPageAdapter(this, fragments)
    }

    override fun nextPage() {
        binding.onboardingViewPager.currentItem = min(
            a = fragments.size,
            b = binding.onboardingViewPager.currentItem + 1
        )
    }

    override fun prevPage() {
        binding.onboardingViewPager.currentItem = max(
            a = 0,
            b = binding.onboardingViewPager.currentItem - 1
        )
    }
}