package com.bhdr.twitterclone.fragments.loginupfragments


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.databinding.FragmentSigInForgetPasswordBinding
import com.bhdr.twitterclone.helperclasses.gone
import com.bhdr.twitterclone.helperclasses.snackBar
import com.bhdr.twitterclone.helperclasses.visible
import com.bhdr.twitterclone.repos.LoginUpRepository.LogInUpStatus.*
import com.bhdr.twitterclone.viewmodels.loginupviewmodel.ForgetPasswordViewModel
import com.google.android.material.snackbar.Snackbar
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding


class SignInForgetPasswordFragment : Fragment(R.layout.fragment_sig_in_forget_password) {
   private val binding by viewBinding(FragmentSigInForgetPasswordBinding::bind)
   private val viewModel: ForgetPasswordViewModel by viewModels()

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      observableAndBinding()

   }

   private fun observableAndBinding() {
      with(viewModel) {
         with(binding) {
            userId.observe(viewLifecycleOwner) {
               when (it!!) {
                  0 -> snackBar(requireView(), "Girilen Bilgiye Ait Hesap Bulunamadı", 1500)
                  else -> {
                     try {
                        findNavController().navigate(
                           SignInForgetPasswordFragmentDirections.actionSigInForgetPasswordFragmentToSignInForgetPasswordSecondFragment(
                              it
                           )
                        )
                        lottiAnim.gone()
                     } catch (e: Exception) {
                        Log.e("userIdObservable-Ex", e.toString())
                     }
                  }

               }
            }
            executeStatus.observe(viewLifecycleOwner) {
               when (it!!) {
                  LOADING -> lottiAnim.visible()
                  ERROR -> {
                     lottiAnim.gone();Snackbar.make(
                        requireView(),
                        "Hata Oluştu Lütfen Daha Sonra Tekrar Deneyiniz",
                        1500
                     ).show()
                  }
                  DONE -> lottiAnim.gone()
               }
            }
            searchButton.setOnClickListener {

               if (emailphonenicknameEditText.text.isNotEmpty()) {

                  viewModel.userForgetId(emailphonenicknameEditText.text.toString())

               } else {
                  snackBar(requireView(), "Kullanıcı Bilgisini  Giriniz", 1000)
               }

            }
            cancel.setOnClickListener {
               findNavController().popBackStack()
            }
         }

      }
   }

}