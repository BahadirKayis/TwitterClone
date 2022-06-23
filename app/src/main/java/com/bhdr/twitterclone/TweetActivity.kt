package com.bhdr.twitterclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.bhdr.twitterclone.databinding.ActivityMainBinding
import com.bhdr.twitterclone.databinding.ActivityTweetBinding
import com.bhdr.twitterclone.databinding.FragmentLogInBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class TweetActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweet)
        val navHostFragment= supportFragmentManager.findFragmentById(R.id.fragmentContainerView)as NavHostFragment
        val navController=navHostFragment.navController
        findViewById<BottomNavigationView> (R. id.bottomNavigationView)
            .setupWithNavController(navController)
    }
}