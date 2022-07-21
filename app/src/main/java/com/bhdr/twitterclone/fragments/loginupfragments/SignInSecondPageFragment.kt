package com.bhdr.twitterclone.fragments.loginupfragments


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.databinding.FragmentSigInSecondPageBinding
import com.bhdr.twitterclone.models.Users
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding


class SignInSecondPageFragment : Fragment(R.layout.fragment_sig_in_second_page) {
    private val binding by viewBinding(FragmentSigInSecondPageBinding::bind)
    private val args: SignInSecondPageFragmentArgs by navArgs()
    lateinit var shared : SharedPreferences
    private var userModel: Users? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shared = requireContext().getSharedPreferences("com.bhdr.twitterclone", Context.MODE_PRIVATE)
        userModel = args.userModel

        binding.cancel.setOnClickListener {
            findNavController().popBackStack()

        }
        binding.forgetPasswordText.setOnClickListener {
            findNavController().navigate(R.id.action_sigInSecondPageFragment_to_sigInForgetPasswordFragment)
        }

        binding.signInButton.setOnClickListener {
            nextButton()
        }

        //kaydetme yeri

        val edit = shared.edit()
        edit.putInt("user_Id" , userModel?.id!!)
        edit.putString("user_name" , userModel?.name!!)
        edit.putString("user_photoUrl" ,  userModel?.photoUrl!!)
        edit.putString("user_userName" , userModel?.userName!!)
        edit.putString("user_userPassword" , userModel?.userPassword!!)
        edit.apply()
        Log.e("TAG", shared.getString("user_name","").toString() )
    }

    private fun nextButton() {
        if (binding.passwordInput.text.toString().isNotEmpty()) {
            if (userModel?.userPassword.toString() == binding.passwordInput.text.toString()) {
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