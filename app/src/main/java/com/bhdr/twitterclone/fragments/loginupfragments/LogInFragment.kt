package com.bhdr.twitterclone.fragments.loginupfragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.databinding.FragmentLogInBinding
import com.bhdr.twitterclone.viewmodels.loginupviewmodel.SignInViewModel

import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding


class LogInFragment : Fragment(R.layout.fragment_log_in) {

   private val binding by viewBinding(FragmentLogInBinding::bind)


   private val viewModel by lazy { SignInViewModel() }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      viewModel.loginAuto.observe(viewLifecycleOwner) {//Zaman yetmez vs i

         when (it) {
            true -> findNavController().navigate(R.id.action_logInFragment_to_main_nav)

            else -> {}
         }
      }

      binding.signUpButton.setOnClickListener {
         findNavController().navigate(R.id.action_logInFragment_to_signUpFragment)
      }

      binding.signInText.setOnClickListener {
         findNavController().navigate(R.id.action_logInFragment_to_sigInFragment)
      }
   }

}