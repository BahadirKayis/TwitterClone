package com.bhdr.twitterclone.fragments.loginupfragments


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.databinding.FragmentSigInSecondPageBinding
import com.bhdr.twitterclone.models.Users
import com.google.android.material.snackbar.Snackbar
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding


class SignInSecondPageFragment : Fragment(R.layout.fragment_sig_in_second_page) {
   private val binding by viewBinding(FragmentSigInSecondPageBinding::bind)
   private val args: SignInSecondPageFragmentArgs by navArgs()
   lateinit var shared: SharedPreferences
   private var userModel: Users? = null

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      shared = requireContext().getSharedPreferences("com.bhdr.twitterclone", Context.MODE_PRIVATE)
      userModel = args.userModel

      binding.apply {

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
      //kaydetme yeri


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

}