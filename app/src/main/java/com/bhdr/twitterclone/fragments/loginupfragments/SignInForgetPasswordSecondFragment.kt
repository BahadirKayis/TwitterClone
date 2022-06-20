package com.bhdr.twitterclone.fragments.loginupfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.navArgs
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.databinding.FragmentSignInForgetPasswordSecondBinding
import com.bhdr.twitterclone.helperclasses.LoadingDialog
import com.bhdr.twitterclone.repos.LoginRepository
import com.bhdr.twitterclone.viewmodels.loginıupviewmodel.ForgetPasswordViewModel
import com.google.android.material.snackbar.Snackbar
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding


class SignInForgetPasswordSecondFragment :
    Fragment(R.layout.fragment_sign_in_forget_password_second) {
    private val binding by viewBinding(FragmentSignInForgetPasswordSecondBinding::bind)
    private val args: SignInForgetPasswordSecondFragmentArgs by navArgs()
    private val forgetViewModel by lazy { ForgetPasswordViewModel() }
    private val loadingDialog by lazy { LoadingDialog() }
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
            Snackbar.make(requireView(), "Şifreler uyuşmuyor", 1000).show()
        }
    }

    private fun passwordObservable() {
        forgetViewModel.userChangePassword.observe(viewLifecycleOwner) {
            when (it) {
                true -> Snackbar.make(requireView(), "Şifre Değiştirildi", 1500).show()
                false -> Snackbar.make(
                    requireView(),
                    "Hata Oluştu Lütfen Daha Sonra Tekrar Deneyiniz",
                    1500
                ).show()
            }
        }
        forgetViewModel.executeStatus.observe(viewLifecycleOwner) {
            when (it!!) {
                LoginRepository.LogInUpStatus.LOADING -> loadingDialog.loadingDialogStart(
                    requireActivity()
                )
                LoginRepository.LogInUpStatus.ERROR -> {
                    loadingDialog.loadingDialogClose()
                    Snackbar.make(
                        requireView(),
                        "Hata Oluştu Lütfen Daha Sonra Tekrar Deneyiniz",
                        1500
                    ).show()
                }
                LoginRepository.LogInUpStatus.DONE -> loadingDialog.loadingDialogClose()
            }
        }
    }
}