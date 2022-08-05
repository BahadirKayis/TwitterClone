package com.bhdr.twitterclone

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bhdr.twitterclone.databinding.ActivityMainBinding
import com.bhdr.twitterclone.fragments.mainfragments.MainScreenFragment
import com.bhdr.twitterclone.helperclasses.*
import com.bhdr.twitterclone.viewmodels.mainviewmodel.MainViewModel
import com.canerture.e_commerce_app.common.delegate.viewBinding
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), MainScreenFragment.MainScreenInterFace {
   private val binding by viewBinding(ActivityMainBinding::inflate)

   private lateinit var navController: NavController
   lateinit var toggle: ActionBarDrawerToggle

   private val viewModel by lazy { MainViewModel() }
   var itemsLayout: View? = null

   var notificationCount: Int = 0

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(binding.root)



      val navHostFragment =
         supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
      navController = navHostFragment.navController
      NavigationUI.setupWithNavController(binding.bottomNav, navController)

      navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->

         when (destination.id) {
            R.id.splashFragment, R.id.logInFragment, R.id.sigInForgetPasswordFragment, R.id.signInForgetPasswordSecondFragment,
            R.id.sigInFragment, R.id.sigInSecondPageFragment, R.id.signUpFragment -> {
               binding.bottomNav.gone()
               binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
            else -> {

             //  userRequest()
               binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED)
               binding.bottomNav.visible()
            }
         }
      }
      itemsLayout = binding.navMenu.inflateHeaderView(R.layout.menu_nav_header)
      toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
      binding.drawerLayout.addDrawerListener(toggle)
      toggle.syncState()
      supportActionBar?.setDisplayHomeAsUpEnabled(true)
      binding.navMenu.setNavigationItemSelectedListener {
         when (it.itemId) {
            R.id.logInFragment -> {
               snackBar(
                  binding.drawerLayout,
                  "Çıkış Yapılıyor",
                  1000
               )
               binding.drawerLayout.close()
               logOut()

               this.lifecycleScope.launch {
                  delay(1100)
                  navHostFragment.navController.navigate(
                     R.id.logInFragment
                  )
               }

            }
         }
         true
      }
      observable()
   }

   private fun logOut() {
      this.sharedPref().edit().clear().commit()
   }

   private fun userRequest() {
      viewModel.followCount(this.userId())
      viewModel.followedCount(this.userId())
      viewModel.getFollowedUserIdList(this.userId())
      shared = getSharedPreferences("com.bhdr.twitterclone", Context.MODE_PRIVATE)


   }

   private fun observable() {
      viewModel.apply {
         followCount.observe(this@MainActivity, Observer {
            itemsLayout!!.findViewById<TextView>(R.id.userFollow).text = it.toString()

            itemsLayout!!.findViewById<TextView>(R.id.userNameSurname).text =
               this@MainActivity.sharedPref().getString("user_name", "").toString()

            itemsLayout!!.findViewById<TextView>(R.id.userName).text = "@" +
                    this@MainActivity.sharedPref().getString("user_userName", "").toString()

            itemsLayout!!.findViewById<CircleImageView>(R.id.circleImageView)
               .picasso(this@MainActivity.userPhotoUrl())
         })
         followedCount.observe(this@MainActivity, Observer {
            itemsLayout!!.findViewById<TextView>(R.id.userFollowed).text = it.toString()
         })

         mutableNotFollowTweetOrLike.observe(this@MainActivity) {
            try {

               notificationCount++
               binding.notificationCount.text = notificationCount.toString()
               binding.notificationCount.visible()

            } catch (e: Exception) {
               Log.e("Exception", e.toString())
               e.printStackTrace()

            }
         }
      }
   }

   override fun onOptionsItemSelected(item: MenuItem): Boolean {
      if (toggle.onOptionsItemSelected(item)) {
         return true
      }
      return super.onOptionsItemSelected(item)
   }

   override fun openDrawerClick() {
      Log.e("TAG", "openDrawer: ")
      //  binding.drawerLayout.openDrawer(R.id.drawerLayout)
      // toggle.onDrawerClosed(binding.drawerLayout)
      binding.drawerLayout.openDrawer(GravityCompat.START)
   }

   override fun onDestroy() {
      super.onDestroy()

   }

   override fun onStart() {
      CoroutineScope(Dispatchers.Main).launch {
         delay(3000)
         viewModel.startSignalR(this@MainActivity.userId())
      }

      super.onStart()

   }
}