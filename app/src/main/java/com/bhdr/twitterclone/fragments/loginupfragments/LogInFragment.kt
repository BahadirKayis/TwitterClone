package com.bhdr.twitterclone.fragments.loginupfragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.TweetActivity
import com.bhdr.twitterclone.databinding.FragmentLogInBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding


class LogInFragment : Fragment(R.layout.fragment_log_in) {
    private val binding by viewBinding(FragmentLogInBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //  findNavController().navigate(R.id.xml2)
        //startActivity(Intent(requireActivity(), TweetActivity::class.java))
        binding.signUpButton.setOnClickListener {
            findNavController().navigate(R.id.action_logInFragment_to_signUpFragment)
        }

        binding.signInText.setOnClickListener {
            findNavController().navigate(R.id.action_logInFragment_to_sigInFragment)
        }
    }

}