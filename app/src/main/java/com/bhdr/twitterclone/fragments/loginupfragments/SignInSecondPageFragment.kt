package com.bhdr.twitterclone.fragments.loginupfragments

import android.os.Bundle
import android.text.Layout
import androidx.fragment.app.Fragment
import android.view.View
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

    private var userModel: Users? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
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

    }

    private fun nextButton() {
        if (binding.passwordInput.text.toString().isNotEmpty()) {
            if (userModel?.userPassword.toString() == binding.passwordInput.text.toString()) {
                Snackbar.make(requireView(), "Giriş Başarılı ", 1000).show()

             //   findNavController().navigate(R.id.xml)

            } else {
                Snackbar.make(requireView(), "Girilen Şifre Yanlış", 2000).show()
            }
        } else {
            Snackbar.make(requireView(), "Şifre Giriniz ", 2000).show()
        }
    }

}