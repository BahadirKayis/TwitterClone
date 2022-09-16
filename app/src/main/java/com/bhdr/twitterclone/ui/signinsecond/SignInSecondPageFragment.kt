package com.bhdr.twitterclone.ui.signinsecond


import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.common.sharedPref
import com.bhdr.twitterclone.data.model.remote.Users
import com.bhdr.twitterclone.databinding.FragmentSigInSecondPageBinding
import com.google.android.material.snackbar.Snackbar
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInSecondPageFragment : Fragment(R.layout.fragment_sig_in_second_page) {
   private val binding by viewBinding(FragmentSigInSecondPageBinding::bind)
   private val args: SignInSecondPageFragmentArgs by navArgs()
   lateinit var shared: SharedPreferences
   private var userModel: Users? = null

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      shared = requireContext().sharedPref()
      userModel = args.userModel
      binding()

   }

   private fun nextButton() {
      if (binding.passwordInput.text.toString().isNotEmpty()) {
         if (userModel?.userPassword.toString() == binding.passwordInput.text.toString()) {
            shared.edit().apply {
               userModel?.apply {
                  putInt("user_Id", id!!)
                  putString("user_name", name)
                  putString("user_photoUrl", photoUrl!!)
                  putString("user_userName", userModel?.userName!!)
                  putString("user_userPassword", userModel?.userPassword!!)
                  apply()
               }
            }
            Snackbar.make(requireView(), "Giriş Başarılı ", 1000).show()
            findNavController().navigate(R.id.action_sigInSecondPageFragment_to_main_nav)


         } else {
            Snackbar.make(requireView(), "Girilen Şifre Yanlış", 2000).show()
         }
      } else {
         Snackbar.make(requireView(), "Şifre Giriniz ", 2000).show()
      }
   }

   private fun binding() {
      with(binding) {
         cancel.setOnClickListener {
            findNavController().popBackStack()

         }
         forgetPasswordText.setOnClickListener {
            findNavController().navigate(R.id.action_sigInSecondPageFragment_to_sigInForgetPasswordFragment)
         }

         signInButton.setOnClickListener {
            nextButton()
         }
      }
   }

}