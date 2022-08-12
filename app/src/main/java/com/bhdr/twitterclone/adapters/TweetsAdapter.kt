package com.bhdr.twitterclone.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.toSpannable
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bhdr.twitterclone.databinding.TweetCardBinding
import com.bhdr.twitterclone.diffcallback.TweetsCallBack
import com.bhdr.twitterclone.fragments.mainfragments.MainScreenFragmentDirections
import com.bhdr.twitterclone.helperclasses.picasso
import com.bhdr.twitterclone.room.TweetsRoomModel
import com.bhdr.twitterclone.room.UsersRoomModel
import com.like.LikeButton
import com.like.OnLikeListener


class TweetsAdapter(private val clickedTweetListener: ClickedTweetListener) :
   ListAdapter<TweetsRoomModel, TweetsAdapter.TweetViewHolder>(TweetsCallBack()) {

   private var context: Context? = null
   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetViewHolder {
      val binding =
         TweetCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      context = parent.context
      return TweetViewHolder(binding)
   }

   override fun onBindViewHolder(holder: TweetViewHolder, position: Int) {
      val posts = getItem(position)

      val userModel = posts.user

      if (posts.isLiked == true) {
         holder.binding.favButton.isLiked = true
      }
      holder.binding.favButton.setOnLikeListener(object : OnLikeListener {
         override fun liked(likeButton: LikeButton?) {
            clickedTweetListener.crfButtonsListener("fav", posts.id!!, 1)
            val postCont = 1 + posts.postLike!!
            holder.binding.favText.text = postCont.toString()
            posts.postLike = postCont
            posts.isLiked = true
         }

         override fun unLiked(likeButton: LikeButton?) {
            clickedTweetListener.crfButtonsListener("fav", posts.id!!, -1)
            val postCont = posts.postLike!! - 1
            holder.binding.favText.text = postCont.toString()
            posts.postLike = postCont
            posts.isLiked = false
         }
      })
      try {
         holder.binding.tweetMenuText.setOnClickListener {
            it.findNavController()
               .navigate(
                  MainScreenFragmentDirections.actionMainScreenFragmentToTweetBottomDialog(
                     "@" + userModel?.userName.toString()
                  )
               )
         }

         holder.post(posts)
         holder.userModel(userModel!!)
      } catch (e: Exception) {
         Log.e("TweetAdapterHolderCatch", e.toString())
      }
   }

   inner class TweetViewHolder(val binding: TweetCardBinding) :
      RecyclerView.ViewHolder(binding.root) {

      fun post(model: TweetsRoomModel) {
         try {

            binding.apply {

               tweetText.text = model.postContent.toString()
               tweetImage.picasso(model.tweetImage.toString())
               timeText.text = model.date.toString()

               if (model.postContent?.contains("#") == true) {

                  tweetText.setSpannableFactory(spannableFactory())

                  tweetText.setText(
                     model.postContent.replace("-n", "\n"),
                     TextView.BufferType.SPANNABLE
                  )
               }

               favText.text = model.postLike.toString()
            }
         } catch (e: Exception) {
            Log.e("TweetViewHolder", "post: $e")
         }
      }

      @SuppressLint("SetTextI18n")
      fun userModel(model: UsersRoomModel) {
         binding.apply {
            try {
               //   binding.profilePicture.load(Uri.parse(model.photo))
               profilePicture.picasso(model.photo.toString())
               nameText.text = model.name
               usernameText.text = "@" + model.userName
            } catch (e: Exception) {
               Log.e("TweetAdapterModelCatch", e.toString())
            }
         }
      }
   }

   interface ClickedTweetListener {
      fun crfButtonsListener(commentrtfav: String, tweetId: Int, currentlyCRFNumber: Int)
   }

   private fun spannableFactory(): Spannable.Factory {
      val spannableFactory = object : Spannable.Factory() {
         override fun newSpannable(source: CharSequence?): Spannable {
            val spannable = source!!.toSpannable()
            val len1 = source.split(" ")
            val getTagIndex = source.indexOf("#")
            var lastIndex = 0
            len1.forEach {
               if (it.contains("#")) {

                  lastIndex = it.length
               }

            }
            spannable.setSpan(
               ForegroundColorSpan(Color.parseColor("#03A9F4")),
               getTagIndex,
               getTagIndex + lastIndex,
               Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            return spannable
         }
      }
      return spannableFactory
   }

}