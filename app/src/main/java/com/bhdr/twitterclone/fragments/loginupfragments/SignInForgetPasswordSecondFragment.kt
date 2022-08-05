package com.bhdr.twitterclone.fragments.loginupfragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.databinding.FragmentSignInForgetPasswordSecondBinding
import com.bhdr.twitterclone.helperclasses.gone
import com.bhdr.twitterclone.helperclasses.snackBar
import com.bhdr.twitterclone.helperclasses.visible
import com.bhdr.twitterclone.repos.LoginUpRepository
import com.bhdr.twitterclone.viewmodels.loginupviewmodel.ForgetPasswordViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding


class SignInForgetPasswordSecondFragment :
   Fragment(R.layout.fragment_sign_in_forget_password_second) {
   private val binding by viewBinding(FragmentSignInForgetPasswordSecondBinding::bind)
   private val args: SignInForgetPasswordSecondFragmentArgs by navArgs()
   private val forgetViewModel by lazy { ForgetPasswordViewModel() }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      binding.passwordUpdateButton.setOnClickListener {
         passwordUpdate()
      }

   }

   private fun passwordUpdate() {
      if (binding.passwordInput.text.toString() == binding.passwordInput2.text.toString()) {
         passwordObservable()
         forgetViewModel.userChangePassword(args.userId, binding.passwordInput.text.toString())

      } else {
         snackBar(requireView(), "Şifreler uyuşmuyor", 1000)
      }
   }

   private fun passwordObservable() {
      forgetViewModel.userChangePassword.observe(viewLifecycleOwner) {
         when (it) {
            true -> snackBar(requireView(), "Şifre Değiştirildi", 1500)
            false -> snackBar(
               requireView(),
               "Hata Oluştu Lütfen Daha Sonra Tekrar Deneyiniz",
               1500
            )
         }
      }
      forgetViewModel.executeStatus.observe(viewLifecycleOwner) {
         when (it!!) {
            LoginUpRepository.LogInUpStatus.LOADING -> binding.lottiAnim.visible()

            LoginUpRepository.LogInUpStatus.ERROR -> {
               binding.lottiAnim.gone()
               snackBar(requireView(), "Hata Oluştu Lütfen Daha Sonra Tekrar Deneyiniz", 1500)
            }
            LoginUpRepository.LogInUpStatus.DONE -> binding.lottiAnim.gone()
         }
      }
   }
}