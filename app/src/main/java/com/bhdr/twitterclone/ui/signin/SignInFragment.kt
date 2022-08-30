package com.bhdr.twitterclone.ui.signin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.databinding.FragmentSigInBinding
import com.bhdr.twitterclone.common.snackBar
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sig_in) {
   private val binding by viewBinding(FragmentSigInBinding::bind)
   private val viewModel: SignInViewModel by viewModels()
   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      observable()
      binding()
   }

   private fun observable() {
      with(viewModel) {
         userModel.observe(viewLifecycleOwner, Observer {
            if (it != null) {
               findNavController().navigate(
                  SignInFragmentDirections.actionSigInFragmentToSigInSecondPageFragment(it)
               )
            } else {
               snackBar(requireView(), "Girilen kullanıcı adı bulunamadı", 1500)
            }

         })
      }
   }

   private fun binding() {
      with(binding) {
         nextButton.setOnClickListener {
            if (emailphonenicknameEditText.text.isNotEmpty()) {
               viewModel.getUserSigIn(emailphonenicknameEditText.text.toString())

            } else {

               snackBar(requireView(), "Kullanıcı Adı Giriniz", 1500)
            }
         }
         cancel.setOnClickListener {
            findNavController().popBackStack()
         }
         forgetPasswordText.setOnClickListener {
            findNavController().navigate(R.id.action_sigInFragment_to_sigInForgetPasswordFragment2)
         }
      }
   }
}

