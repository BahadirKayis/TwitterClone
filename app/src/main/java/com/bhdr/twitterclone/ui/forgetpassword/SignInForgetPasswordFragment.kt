package com.bhdr.twitterclone.ui.forgetpassword


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.common.Status
import com.bhdr.twitterclone.common.gone
import com.bhdr.twitterclone.common.snackBar
import com.bhdr.twitterclone.common.visible
import com.bhdr.twitterclone.databinding.FragmentSigInForgetPasswordBinding
import com.google.android.material.snackbar.Snackbar
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
                  Status.LOADING -> lottiAnim.visible()
                  Status.ERROR -> {
                     lottiAnim.gone();Snackbar.make(
                        requireView(),
                        "Hata Oluştu Lütfen Daha Sonra Tekrar Deneyiniz",
                        1500
                     ).show()
                  }
                  Status.DONE -> lottiAnim.gone()
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