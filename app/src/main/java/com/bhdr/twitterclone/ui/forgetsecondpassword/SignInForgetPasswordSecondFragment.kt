package com.bhdr.twitterclone.ui.forgetsecondpassword

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.common.gone
import com.bhdr.twitterclone.common.snackBar
import com.bhdr.twitterclone.common.visible
import com.bhdr.twitterclone.databinding.FragmentSignInForgetPasswordSecondBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInForgetPasswordSecondFragment :
   Fragment(R.layout.fragment_sign_in_forget_password_second) {
   private val binding by viewBinding(FragmentSignInForgetPasswordSecondBinding::bind)
   private val args: SignInForgetPasswordSecondFragmentArgs by navArgs()
   private val viewModel: ForgetPasswordSecondViewModel by viewModels()
   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      binding.passwordUpdateButton.setOnClickListener {
         binding.lottiAnim.visible()
         passwordUpdate()
      }
   }

   private fun passwordUpdate() {
      if (binding.passwordInput.text.toString() == binding.passwordInput2.text.toString()) {
         observable()
         viewModel.userChangePassword(args.userId, binding.passwordInput.text.toString())
      } else {
         snackBar(requireView(), "Şifreler uyuşmuyor", 1000)
      }
   }

   private fun observable() {
      with(viewModel) {
         with(binding) {
            userChangePassword.observe(viewLifecycleOwner) {
               when (it) {
                  true -> {
                     lottiAnim.gone()
                     snackBar(requireView(), "Şifre Değiştirildi", 500)
                     findNavController().navigate(R.id.action_signInForgetPasswordSecondFragment_to_logInFragment)
                  }
                  false -> {
                     snackBar(
                        requireView(),
                        "Hata Oluştu Lütfen Daha Sonra Tekrar Deneyiniz",
                        500
                     );lottiAnim.gone()
                  }
               }
            }

         }
      }
   }
}