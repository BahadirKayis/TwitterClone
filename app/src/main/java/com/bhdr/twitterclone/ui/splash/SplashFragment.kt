package com.bhdr.twitterclone.ui.splash

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.databinding.FragmentSplashBinding
import com.bhdr.twitterclone.common.sharedPref
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {
   private val binding by viewBinding(FragmentSplashBinding::bind)

   private val viewModel: SplashViewModel by viewModels()
   var loginStatus: Boolean? = null

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      sharedRequest()
      observable()
      motionLayoutObject()

   }

   private fun observable() {
      viewModel.loginAuto.observe(viewLifecycleOwner) {
         loginStatus = it
      }
   }

   private fun sharedRequest() {
      val userName = requireContext().sharedPref().getString("user_userName", null)
      val password = requireContext().sharedPref().getString("user_userPassword", null)
      if (userName != null && password != null) {
         viewModel.getLoginUserNameAndPassword(userName, password)
      }
   }

   private fun motionLayoutObject() {
      binding.motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
         override fun onTransitionStarted(
            motionLayout: MotionLayout?,
            startId: Int,
            endId: Int
         ) {

         }

         override fun onTransitionChange(
            motionLayout: MotionLayout?,
            startId: Int,
            endId: Int,
            progress: Float
         ) {

         }

         override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
            when (loginStatus) {
               true -> findNavController().navigate(R.id.action_splashFragment_to_main_nav)
               false -> findNavController().navigate(R.id.action_splashFragment_to_logInFragment)
               else -> findNavController().navigate(R.id.action_splashFragment_to_logInFragment)
            }
         }

         override fun onTransitionTrigger(
            motionLayout: MotionLayout?,
            triggerId: Int,
            positive: Boolean,
            progress: Float
         ) {
            TODO("Not yet implemented")
         }


      })
   }
}