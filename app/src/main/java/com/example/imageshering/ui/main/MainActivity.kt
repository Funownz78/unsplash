package com.example.imageshering.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.imageshering.App
import com.example.imageshering.R
import com.example.imageshering.data.AppAuthComponent
import com.example.imageshering.databinding.ActivityMainBinding
import com.example.imageshering.ui.onboarding.OnBoardingActivity
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse

private const val TAG = "SecurityTesting"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appAuthComponent: AppAuthComponent
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    var viewIntentId: String? = null
        get() {
            val result = field
            field = null
            return result
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "MainActivity onCreate: ")
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        appAuthComponent = App.daggerComponent.getAppAuthComponent()
        setContentView(binding.root)
//        initialUi()

        val intentAction = intent.action
        val intentData = intent.data

        val authorizationResponse = AuthorizationResponse.fromIntent(intent)
        val authorizationException = AuthorizationException.fromIntent(intent)

        if (!appAuthComponent.isAuthorized) {
            if (authorizationResponse == null && authorizationException == null) {
                startActivity(Intent(this@MainActivity, OnBoardingActivity::class.java))
            } else {
                if (authorizationResponse != null) {
                    appAuthComponent.performAccessTokenRequest(
                        authorizationResponse,
                        authorizationException
                    ) {
                        startActivity(Intent(this@MainActivity, OnBoardingActivity::class.java))
                    }
                } else {
                    startActivity(Intent(this@MainActivity, OnBoardingActivity::class.java))
                }
            }
        } else {
            if (intentAction == "android.intent.action.VIEW") {
                intentData?.let { viewIntentId = it.lastPathSegment }
            }
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_view_pager) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = binding.tabLayout
        bottomNavigationView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeListFragment, R.id.collectionsFragment, R.id.profileFragment)
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

//    private fun initialUi() {
//        binding.mainViewPager.adapter = PagerAdapter(this)
////        binding.tabLayout.tabIconTint = ColorStateList()
//        TabLayoutMediator(binding.tabLayout, binding.mainViewPager) { tab, position ->
//            when (position) {
//                0 -> {
//                    tab.setIcon(R.drawable.outline_home_24)
//                }
//                1 -> {
//                    tab.setIcon(R.drawable.baseline_star_outline_24)
////                    tab.icon?.setTint(ContextCompat.getColor(this, androidx.appcompat.R.color.material_grey_300))
//                }
//                else -> {
//                    tab.setIcon(R.drawable.outline_person_24)
////                    tab.icon?.setTint(ContextCompat.getColor(this, androidx.appcompat.R.color.material_grey_300))
//                }
//            }
//        }.attach()
//        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab?) {
////                tab?.icon?.setTint(ContextCompat.getColor(this@MainActivity, R.color.black))
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
////                tab?.icon?.setTint(ContextCompat.getColor(this@MainActivity, androidx.appcompat.R.color.material_grey_300))
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab?) {
////                tab?.icon?.setTint(ContextCompat.getColor(this@MainActivity, R.color.black))
//            }
//
//        })
//        binding.tabLayout.getTabAt(0)?.select()
////        MainScope().launch {
////            delay(5_000)
////            startActivity(Intent(this@MainActivity, OnBoardingActivity::class.java))
////        }
//
//    }


}