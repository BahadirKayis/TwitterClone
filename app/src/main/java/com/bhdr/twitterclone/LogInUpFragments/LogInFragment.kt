package com.bhdr.twitterclone.LogInUpFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
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