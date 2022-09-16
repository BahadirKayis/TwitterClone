package com.bhdr.twitterclone.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.databinding.FragmentLogInBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogInFragment : Fragment(R.layout.fragment_log_in) {

   private val binding by viewBinding(FragmentLogInBinding::bind)

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)


      binding.signUpButton.setOnClickListener {
         findNavController().navigate(R.id.action_logInFragment_to_signUpFragment)
      }

      binding.signInText.setOnClickListener {
         findNavController().navigate(R.id.action_logInFragment_to_sigInFragment)
      }
   }

}