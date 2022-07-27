package com.bhdr.twitterclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import com.bhdr.twitterclone.databinding.ActivityMainBinding
import com.bhdr.twitterclone.databinding.ActivityMainBinding.inflate
import com.bhdr.twitterclone.databinding.AgendaCardBinding.inflate
import com.bhdr.twitterclone.databinding.FragmentLogInBinding
import com.bhdr.twitterclone.databinding.MenuNavHeaderBinding
import com.bhdr.twitterclone.helperclasses.gone
import com.bhdr.twitterclone.helperclasses.sharedPref
import com.bhdr.twitterclone.helperclasses.userPhotoUrl
import com.bhdr.twitterclone.helperclasses.visible
import com.canerture.e_commerce_app.common.delegate.viewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.Drawer
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity() {
   private val binding by viewBinding(ActivityMainBinding::inflate)

   private lateinit var navController: NavController
   lateinit var toggle: ActionBarDrawerToggle


   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(binding.root)

      navMenu()
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
               navMenu()
               binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN)

               binding.bottomNav.visible()
            }
         }
      }

      toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
      binding.drawerLayout.addDrawerListener(toggle)
      toggle.syncState()
      supportActionBar?.setDisplayHomeAsUpEnabled(true)
      binding.navMenu.setNavigationItemSelectedListener {
         when (it.itemId) {
            R.id.logInFragment -> {
               Log.e("TAG", "onCreate: ");logOut()
            }
         }
         true
      }
   }

   private fun logOut() {
      this.sharedPref().edit().clear().commit()
      //  binding.fragmentContainerView.id=R.navigation.login_nav

   }

   private fun navMenu() {
      try {

         val itemsLayout: View = binding.navMenu.inflateHeaderView(R.layout.menu_nav_header)
         Picasso.get().load(this.userPhotoUrl())
            .into(itemsLayout.findViewById<CircleImageView>(R.id.circleImageView))
         val item: TextView = itemsLayout.findViewById(R.id.userFollow)
         item.text = "44"
      } catch (e: Exception) {
         Log.e("MainEx", e.toString())
      }
   }

   override fun onOptionsItemSelected(item: MenuItem): Boolean {
      if (toggle.onOptionsItemSelected(item)) {
         return true
      }
      return super.onOptionsItemSelected(item)
   }
}