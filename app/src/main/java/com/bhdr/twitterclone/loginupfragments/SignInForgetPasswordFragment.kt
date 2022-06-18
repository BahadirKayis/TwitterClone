package com.bhdr.twitterclone.loginupfragments

import android.os.Bundle
import android.util.Log
import android.util.Log.v
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.databinding.FragmentSigInForgetPasswordBinding
import com.bhdr.twitterclone.helperclasses.LoadingDialog
import com.bhdr.twitterclone.repos.LoginRepository
import com.bhdr.twitterclone.repos.LoginRepository.LogInUpStatus.*
import com.bhdr.twitterclone.viewmodels.ForgetPasswordViewModel
import com.google.android.material.snackbar.Snackbar
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding


class SignInForgetPasswordFragment : Fragment(R.layout.fragment_sig_in_forget_password) {
    private val binding by viewBinding(FragmentSigInForgetPasswordBinding::bind)
    private val forgetViewModel by lazy { ForgetPasswordViewModel() }
    private val loadingDialog by lazy { LoadingDialog() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchButton.setOnClickListener {

            if (binding.emailphonenicknameEditText.text.isNotEmpty()) {
                getUserId()
                forgetViewModel.userForgetId(binding.emailphonenicknameEditText.text.toString())

            } else {
                Snackbar.make(requireView(), "Kullanıcı Bilgisini  Giriniz", 1500).show()
            }

        }


    }

    private fun getUserId() {
        forgetViewModel.userId.observe(viewLifecycleOwner) {
            Log.e("TAG", it.toString())
            when (it!!) {
                0 -> Snackbar.make(requireView(), "Girilen Bilgiye Ait Hesap Bulunamadı", 1500)
                    .show()
                else -> findNavController().navigate(
                    SignInForgetPasswordFragmentDirections.actionSigInForgetPasswordFragmentToSignInForgetPasswordSecondFragment(it)
                )
            }
        }
        forgetViewModel.executeStatus.observe(viewLifecycleOwner) {
            when (it!!) {
                LOADING -> loadingDialog.loadingDialogStart(requireActivity())
                ERROR -> {
                    loadingDialog.loadingDialogClose();Snackbar.make(
                        requireView(),
                        "Hata Oluştu Lütfen Daha Sonra Tekrar Deneyiniz",
                        1500
                    ).show()
                }
                DONE -> loadingDialog.loadingDialogClose()
            }
        }

    }
}