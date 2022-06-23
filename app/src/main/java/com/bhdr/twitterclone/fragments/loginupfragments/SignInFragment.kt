package com.bhdr.twitterclone.fragments.loginupfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.databinding.FragmentSigInBinding
import com.bhdr.twitterclone.viewmodels.loginıupviewmodel.SignInViewModel
import com.google.android.material.snackbar.Snackbar
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding


class SignInFragment : Fragment(R.layout.fragment_sig_in) {
    private val binding by viewBinding(FragmentSigInBinding::bind)
    private val sigInModelView by lazy { SignInViewModel() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUserModelObservable()
        binding.nextButton.setOnClickListener {
            if (binding.emailphonenicknameEditText.text.isNotEmpty()) {
                sigInModelView.getUserSigIn(binding.emailphonenicknameEditText.text.toString())

            } else {
                Snackbar.make(requireView(), "Kullanıcı Adı Giriniz", 1500).show()
            }
        }
        binding.cancel.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.forgetPasswordText.setOnClickListener {
            findNavController().navigate(R.id.action_sigInFragment_to_sigInForgetPasswordFragment2)
        }
    }

    private fun getUserModelObservable() {
        sigInModelView.userModel.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                findNavController().navigate(
                    SignInFragmentDirections.actionSigInFragmentToSigInSecondPageFragment(it)
                )
            } else {
                Snackbar.make(requireView(), "Girilen kullanıcı adı bulunamadı", 1500)
                    .show()
            }

        })
    }
}

