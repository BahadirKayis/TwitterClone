package com.bhdr.twitterclone.fragments.mainfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.databinding.FragmentMainScreenBinding
import com.bhdr.twitterclone.databinding.FragmentSignInForgetPasswordSecondBinding
import com.bhdr.twitterclone.fragments.loginupfragments.SignInForgetPasswordSecondFragmentArgs
import com.bhdr.twitterclone.viewmodels.loginıupviewmodel.ForgetPasswordViewModel
import com.bhdr.twitterclone.viewmodels.mainviewmodel.PostViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class MainScreenFragment : Fragment(R.layout.fragment_main_screen) {
    private val binding by viewBinding(FragmentMainScreenBinding::bind)
    private val args: SignInForgetPasswordSecondFragmentArgs by navArgs()//
    private val forgetViewModel by lazy { PostViewModel() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        forgetViewModel.getPosts(1)
    }
}