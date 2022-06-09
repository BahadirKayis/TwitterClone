package com.bhdr.twitterclone.LogInUpFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.databinding.FragmentSigInBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding


class SigInFragment : Fragment(R.layout.fragment_sig_in) {
    private val binding by viewBinding (FragmentSigInBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}