package com.bhdr.twitterclone.fragments.loginupfragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.databinding.FragmentSigInForgetPasswordBinding
import com.bhdr.twitterclone.helperclasses.loadingDialogStart

import com.bhdr.twitterclone.helperclasses.snackBar
import com.bhdr.twitterclone.repos.LoginRepository.LogInUpStatus.*
import com.bhdr.twitterclone.viewmodels.loginupviewmodel.ForgetPasswordViewModel
import com.google.android.material.snackbar.Snackbar
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding


class SignInForgetPasswordFragment : Fragment(R.layout.fragment_sig_in_forget_password) {
    private val binding by viewBinding(FragmentSigInForgetPasswordBinding::bind)
    private val forgetViewModel by lazy { ForgetPasswordViewModel() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserIdObservable()
        binding.searchButton.setOnClickListener {

            if (binding.emailphonenicknameEditText.text.isNotEmpty()) {

                forgetViewModel.userForgetId(binding.emailphonenicknameEditText.text.toString())

            } else {
                snackBar(requireView(),"Kullanıcı Bilgisini  Giriniz",1000)
            }

        }
        binding.cancel.setOnClickListener {
            findNavController().popBackStack()
        }


    }

    private fun getUserIdObservable() {
        forgetViewModel.userId.observe(viewLifecycleOwner) {
            Log.e("UserId", it.toString())
            when (it!!) {
                0 -> snackBar(requireView(),"Girilen Bilgiye Ait Hesap Bulunamadı",1500)
                else -> {
                    try {
                        findNavController().navigate(
                            SignInForgetPasswordFragmentDirections.actionSigInForgetPasswordFragmentToSignInForgetPasswordSecondFragment(
                                it
                            )
                        )
                        loadingDialogStart(requireActivity()).dismiss()
                    } catch (e: Exception) {
                        Log.e("ErrorGetUserIdObserve", e.toString())
                    }
                }

            }
        }
        forgetViewModel.executeStatus.observe(viewLifecycleOwner) {
            when (it!!) {
                LOADING -> loadingDialogStart(requireActivity())
                ERROR -> {
                    loadingDialogStart(requireActivity()).dismiss();Snackbar.make(
                        requireView(),
                        "Hata Oluştu Lütfen Daha Sonra Tekrar Deneyiniz",
                        1500
                    ).show()
                }
                DONE -> loadingDialogStart(requireActivity()).dismiss()
            }
        }

    }
}