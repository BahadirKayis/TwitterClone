package com.bhdr.twitterclone.loginupfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.databinding.FragmentLogInBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding


class LogInFragment : Fragment(R.layout.fragment_log_in) {
private  val binding by viewBinding(FragmentLogInBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signUpButton.setOnClickListener {
findNavController().navigate(R.id.action_logInFragment_to_signUpFragment)
        }

    }

}