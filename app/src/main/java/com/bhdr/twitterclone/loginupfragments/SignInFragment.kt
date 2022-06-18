package com.bhdr.twitterclone.loginupfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.databinding.FragmentSigInBinding
import com.google.android.material.snackbar.Snackbar
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding


class SignInFragment : Fragment(R.layout.fragment_sig_in) {
    private val binding by viewBinding(FragmentSigInBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.nextButton.setOnClickListener {
            if (binding.emailphonenicknameEditText.text.isNotEmpty()) {
                val username = binding.emailphonenicknameEditText.text.toString()
                findNavController().navigate(
                    SignInFragmentDirections.actionSigInFragmentToSigInSecondPageFragment(
                        username
                    )
                )

            } else {
                Snackbar.make(requireView(), "Kullanıcı Adı Giriniz", Snackbar.ANIMATION_MODE_FADE).show()
            }
        }

        binding.forgetPasswordText.setOnClickListener {
            findNavController().navigate(R.id.action_sigInFragment_to_sigInForgetPasswordFragment2)
        }
    }
}

