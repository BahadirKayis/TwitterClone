package com.bhdr.twitterclone.ui.main

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
import com.bhdr.twitterclone.common.gone
import com.bhdr.twitterclone.common.picasso
import com.bhdr.twitterclone.common.toCalendar
import com.bhdr.twitterclone.common.visible
import com.bhdr.twitterclone.data.model.locale.TweetsRoomModel
import com.bhdr.twitterclone.data.model.locale.UsersRoomModel
import com.bhdr.twitterclone.databinding.TweetCardBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
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
      Log.e("TAG", position.toString())
      with(holder) {
         with(binding) {

            val posts = getItem(position)
            val userModel = posts.user

            if (posts.isLiked == true) {
               favButton.isLiked = true
            }
            favButton.setOnLikeListener(object : OnLikeListener {

               override fun liked(likeButton: LikeButton?) {
                  clickedTweetListener.crfButtonsListener("fav", posts.id!!, 1)
                  with(posts) {
                     val postCont = 1 + posts.postLike!!
                     favText.text = postCont.toString()
                     postLike = postCont
                     isLiked = true
                  }
               }

               override fun unLiked(likeButton: LikeButton?) {
                  clickedTweetListener.crfButtonsListener("fav", posts.id!!, -1)
                  with(posts) {
                     val postCont = postLike!! - 1
                     favText.text = postCont.toString()
                     postLike = postCont
                     isLiked = false
                  }
               }
            })
            try {
               tweetMenuText.setOnClickListener {
                  it.findNavController()
                     .navigate(
                        MainScreenFragmentDirections.actionMainScreenFragmentToTweetBottomDialog(
                           "@" + userModel?.userName.toString().maxOf { 10 }
                        )
                     )
               }
               post(posts)
               userModel(userModel!!)
            } catch (e: Exception) {
               Log.e("TweetAdapterHolderCatch", e.toString())
            }
         }
      }
   }


   inner class TweetViewHolder(val binding: TweetCardBinding) :
      RecyclerView.ViewHolder(binding.root) {

      fun post(model: TweetsRoomModel) {
         try {
            with(model) {
               with(binding) {

                  tweetText.text = postContent.toString()


                  timeText.text = date!!.toLong().toCalendar()

                  if (postContent?.contains("#") == true) {

                     tweetText.setSpannableFactory(spannableFactory())

                     tweetText.setText(
                        postContent.replace("-n", "\n"),
                        TextView.BufferType.SPANNABLE
                     )
                  }
                  favText.text = postLike.toString()
                  if (model.tweetImage != "null") {
                     if (model.tweetImage!!.contains(".webm")) {
                        //Firebase boyutu bitmesin diye
//                        //setup
//                        exoPlayer = ExoPlayer.Builder(context!!).build()
//                        exoPlayer?.playWhenReady = false
//                        binding.playerView.player = exoPlayer
//                        //file
//                        val mediaItem =
//                           MediaItem.fromUri(model.tweetImage!!.toString())
//                        exoPlayer?.addMediaItem(mediaItem)
//
//                        exoPlayer?.playWhenReady = playWhenReady
//                        exoPlayer?.prepare()
                        playerView.visible()
                        binding.tweetImage.gone()
                     } else {
                        binding.playerView.gone()
                        tweetImage.picasso(model.tweetImage.toString())
                     }

                  }

               }

            }
         } catch (e: Exception) {
            Log.e("TweetViewHolder", "post: $e")
         }
      }


      fun userModel(model: UsersRoomModel) {
         with(binding) {
            with(model) {
               try {
                  profilePicture.picasso(photo)
                  nameText.text = name
                  "@$userName".also { usernameText.text = it }
               } catch (e: Exception) {
                  Log.e("TweetAdapterModelCatch", e.toString())
               }
            }
         }
      }
   }

   interface ClickedTweetListener {
      fun crfButtonsListener(commentRtFav: String, tweetId: Int, currentlyCRFNumber: Int)
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


   private var exoPlayer: ExoPlayer? = null
   private var playbackPosition = 0L
   private var playWhenReady = true

   fun releasePlayer() {
      exoPlayer?.let { player ->
         playbackPosition = player.currentPosition
         playWhenReady = player.playWhenReady
         player.release()
         exoPlayer = null
      }
   }

}