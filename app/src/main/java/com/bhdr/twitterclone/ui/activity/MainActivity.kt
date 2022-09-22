package com.bhdr.twitterclone.ui.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.common.*
import com.bhdr.twitterclone.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OpenMenu {
   private val binding by viewBinding(ActivityMainBinding::inflate)
   private lateinit var navController: NavController
   lateinit var toggle: ActionBarDrawerToggle
   private val viewModel: MainViewModel by viewModels()
   var itemsLayout: View? = null
   var signalRStart = false
   var notification = 0
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
               when (destination.id) {
                  R.id.mainScreenFragment -> userRequest()
               }
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
               logOut()
               viewModel.roomDelete.observe(this) {
                  when (it) {
                     true -> {
                        snackBar(
                           binding.drawerLayout,
                           "Çıkış Yapılıyor",
                           1000
                        )
                        binding.drawerLayout.close()

                        this.lifecycleScope.launch {
                           delay(1100)
                           navHostFragment.navController.navigate(
                              R.id.logInFragment
                           )
                        }
                     }
                     false -> {
                        snackBar(
                           binding.drawerLayout,
                           "Çıkış yapılamıyor",
                           1000
                        )
                     }

                  }
               }

            }
         }
         true
      }

   }

   private fun logOut() {
      viewModel.roomDelete()
      this.sharedPref().edit().clear().apply()
   }

   private fun userRequest() {
      if (this.checkNetworkConnection()) {
         if (!signalRStart) {
            signalRStart = true
            observable()
            shared = getSharedPreferences("com.bhdr.twitterclone", Context.MODE_PRIVATE)
            viewModel.startSignalR(this@MainActivity.userId())
         }
         viewModel.followCount(this.userId())
         viewModel.followedCount(this.userId())
         viewModel.getFollowedUserIdList(this.userId())
      }
   }


   private fun observable() {

      with(viewModel) {
         followCount.observe(this@MainActivity, Observer {
            itemsLayout!!.findViewById<TextView>(R.id.userFollow).text = it.toString()

            itemsLayout!!.findViewById<TextView>(R.id.userNameSurname).text =
               this@MainActivity.sharedPref().getString("user_name", "").toString()

            val userName = "@ ${this@MainActivity.sharedPref().getString("user_userName", "")}"
            itemsLayout!!.findViewById<TextView>(R.id.userName).text = userName

            itemsLayout!!.findViewById<CircleImageView>(R.id.circleImageView)
               .picasso(this@MainActivity.userPhotoUrl())
         })
         followedCount.observe(this@MainActivity, Observer {
            itemsLayout!!.findViewById<TextView>(R.id.userFollowed).text = it.toString()
         })
         notificationCount.observe(this@MainActivity) {
            when (it) {
               true -> {
                  notification += 1;binding.notificationCount.visible(); binding.notificationCount.text =
                     notification.toString()
               }

               else -> {
                  Log.i("Return notiSignal", "false")
               }
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
      binding.drawerLayout.openDrawer(GravityCompat.START)
   }

}