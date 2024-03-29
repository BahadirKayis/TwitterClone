package com.bhdr.twitterclone.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bhdr.twitterclone.common.*
import com.bhdr.twitterclone.data.model.locale.TweetsRoomModel
import com.bhdr.twitterclone.data.model.locale.UsersRoomModel
import com.bhdr.twitterclone.databinding.TweetCardBinding
import com.google.android.exoplayer2.ExoPlayer
import com.like.LikeButton
import com.like.OnLikeListener


class TweetsAdapter(private val clickedTweetListener: ClickedTweetListener) :
   ListAdapter<TweetsRoomModel, TweetsAdapter.TweetViewHolder>(TweetsCallBack()) {

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetViewHolder {
      val binding =
         TweetCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return TweetViewHolder(binding)
   }

   override fun onBindViewHolder(holder: TweetViewHolder, position: Int) {

      with(holder) {
         with(binding) {
            val posts = getItem(position)
            val userModel = posts.user

            if (posts.isLiked == true) {
               favButton.isLiked = true
            }

            favButton.setOnLikeListener(object : OnLikeListener {
               override fun liked(likeButton: LikeButton?) {
                  if (root.context.checkNetworkConnection()) {

                     clickedTweetListener.crfButtonsListener("fav", posts.id!!, 1)
                     with(posts) {
                        val postCont = 1 + posts.postLike!!
                        favText.text = postCont.toString()
                        postLike = postCont
                        isLiked = true
                     }
                  } else {
                     snackBar(root.rootView, "İnternet bağlantısı yok", 1000)
                  }
               }

               override fun unLiked(likeButton: LikeButton?) {
                  if (root.context.checkNetworkConnection()) {
                     clickedTweetListener.crfButtonsListener("fav", posts.id!!, -1)
                     with(posts) {
                        val postCont = postLike!! - 1
                        favText.text = postCont.toString()
                        postLike = postCont
                        isLiked = false
                     }
                  } else {
                     snackBar(root.rootView, "İnternet bağlantısı yok", 1000)
                  }
               }

            })

            tweetDetailText.setOnClickListener {
               it.findNavController()
                  .navigate(
                     MainScreenFragmentDirections.actionMainScreenFragmentToTweetBottomDialog(
                        "@" + userModel?.userName.toString()
                     )
                  )
            }

            post(posts)
            userModel(userModel!!)
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
                  } else {
                     tweetText.text = postContent!!.replace("-n", "\n")
                  }

                  favText.text = postLike.toString()
                  if (model.tweetImage != "null") {
                     if (model.tweetImage!!.contains("video")) {
                        //Firebase kullanım limiti olduğu için kapalı burası
                        //setup
//                        exoPlayer = ExoPlayer.Builder(context!!).build()
//                        exoPlayer?.playWhenReady = true
//                        binding.playerView.player = exoPlayer
//                        //file
//                        val mediaItem =
//                           MediaItem.fromUri(model.tweetImage.toString())
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
                  "@$userName".also {
                     usernameText.text = it.userNameCount()
                  }
               } catch (e: Exception) {
                  Log.e("TweetViewHolder", "userModel: $e")
               }
            }
         }
      }
   }

   interface ClickedTweetListener {
      fun crfButtonsListener(commentRtFav: String, tweetId: Int, currentlyCRFNumber: Int)
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