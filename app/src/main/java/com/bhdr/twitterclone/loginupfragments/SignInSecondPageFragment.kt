package com.bhdr.twitterclone.loginupfragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.navArgs
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.databinding.FragmentSigInSecondPageBinding
import com.bhdr.twitterclone.databinding.FragmentSignUpBinding
import com.bhdr.twitterclone.models.Users
import com.bhdr.twitterclone.viewmodels.SignInViewModel
import com.google.android.material.snackbar.Snackbar
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding


class SignInSecondPageFragment : Fragment(R.layout.fragment_sig_in_second_page) {
    private val binding by viewBinding(FragmentSigInSecondPageBinding::bind)
    private val args: SignInSecondPageFragmentArgs by navArgs()
    private val sigInModelView by lazy { SignInViewModel() }
    private var userModel: Users? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        sigInModelView.getUserSigIn(args.userName.toString())
        Log.e("UserModel", args.userName.toString())

        sigInModelView.userModel.observe(viewLifecycleOwner) {
            userModel = it
            Log.e("UserModel", it.userPassword!!)
        }

        binding.signInButton.setOnClickListener {
            nextButton()
        }

    }
    private  fun nextButton(){
        if (binding.passwordInput.text.toString().isNotEmpty()) {
             if (userModel?.userPassword.toString() == binding.passwordInput.text.toString()){
                 Snackbar.make(requireView(), "Giriş Başarılı ", 1000).show()
             }
        }
        else{
            Snackbar.make(requireView(), "Şifre Giriniz ", 2000).show()
        }
    }
}