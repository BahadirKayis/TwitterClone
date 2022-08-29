package com.bhdr.twitterclone.fragments.dialogfragments

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.toSpannable
import androidx.navigation.fragment.navArgs
import com.bhdr.twitterclone.databinding.FragmentTweetBottomDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding


class TweetBottomDialog : BottomSheetDialogFragment() {
   private val binding by viewBinding(FragmentTweetBottomDialogBinding::bind)
   private val args: TweetBottomDialogArgs by navArgs()
   override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View? {
      return inflater.inflate(
         com.bhdr.twitterclone.R.layout.fragment_tweet_bottom_dialog,
         container,
         false
      )
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      val name: String = args.userName

      val spannableFactory = object : Spannable.Factory() {
         override fun newSpannable(source: CharSequence?): Spannable {
            val spannable = source!!.toSpannable()
            val len1 = source.split(" ")[0].length
            spannable.setSpan(
               ForegroundColorSpan(Color.parseColor("#03A9F4")),
               0,
               len1,
               Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            return spannable
         }
      }
      with (binding) {
         blocktext.setSpannableFactory(spannableFactory)
         followtext.setSpannableFactory(spannableFactory)
         mutetext.setSpannableFactory(spannableFactory)

         "$name adlı kişiyi engelle".also { blocktext.setText(it, TextView.BufferType.SPANNABLE) }
         "$name adlı kişiyi takipten çık".also { followtext.setText(it, TextView.BufferType.SPANNABLE) }
         "$name adlı kişiyi sustur".also { mutetext.setText(it, TextView.BufferType.SPANNABLE) }

      }


   }

}