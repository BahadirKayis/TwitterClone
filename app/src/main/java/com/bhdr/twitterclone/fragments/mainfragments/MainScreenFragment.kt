package com.bhdr.twitterclone.fragments.mainfragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.databinding.FragmentMainScreenBinding
import com.bhdr.twitterclone.databinding.FragmentSignInForgetPasswordSecondBinding
import com.bhdr.twitterclone.fragments.loginupfragments.SignInForgetPasswordSecondFragmentArgs
import com.bhdr.twitterclone.helperclasses.LoadingDialog
import com.bhdr.twitterclone.models.Users
import com.bhdr.twitterclone.repos.MainRepository
import com.bhdr.twitterclone.viewmodels.loginıupviewmodel.ForgetPasswordViewModel
import com.bhdr.twitterclone.viewmodels.loginıupviewmodel.SignInViewModel
import com.bhdr.twitterclone.viewmodels.mainviewmodel.PostViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.flow.observeOn

class MainScreenFragment : Fragment(R.layout.fragment_main_screen) {
    private val binding by viewBinding(FragmentMainScreenBinding::bind)
    private val args: SignInForgetPasswordSecondFragmentArgs by navArgs()//
    private val postViewModel by lazy { PostViewModel() }
    private var userModel: Users? = null
    private val loadingDialog by lazy { LoadingDialog() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postViewModel.mainStatus.observe(viewLifecycleOwner) {
            when (it!!) {
                MainRepository.MainStatus.LOADING -> loadingDialog.loadingDialogStart(
                    requireActivity()
                )
                MainRepository.MainStatus.ERROR -> loadingDialog.loadingDialogClose()
                MainRepository.MainStatus.DONE -> loadingDialog.loadingDialogClose()
            }
        }


        try {
            postViewModel.getPosts(1)


            Log.e("userId", userModel!!.id.toString())
        } catch (e: Exception) {
            Log.e("userId", e.toString())
        }

    }
}