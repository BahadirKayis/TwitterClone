package com.bhdr.twitterclone.adapters

import android.content.Context
import android.graphics.Color
import android.net.Uri
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
import com.bhdr.twitterclone.helperclasses.gone
import com.bhdr.twitterclone.helperclasses.visible
import com.bhdr.twitterclone.models.Posts
import com.bhdr.twitterclone.models.Users
import com.like.LikeButton
import com.like.OnLikeListener
import com.squareup.picasso.Picasso
import java.util.*


class TweetsAdapter(private val clickedTweetListener: ClickedTweetListener) :
    ListAdapter<Posts, TweetsAdapter.TweetViewHolder>(TweetsCallBack()) {

    private var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetViewHolder {
        val binding =
            TweetCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return TweetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TweetViewHolder, position: Int) {
        val posts = getItem(position)


//        if (posts.user == null) {//Değişmeden önce boş geliyordu artık gelmiyor
//            posts.user = Users(
//                null,
//                null,
//                null,
//                1,
//                null,
//                "bhdr",
//                null,
//                "https://firebasestorage.googleapis.com/v0/b/twitterclone-f37d0.appspot.com/o/profilpictures%2Fe7f21d65-5e0e-4054-a754-14ba1bbf88cc.jpg?alt=media&token=7da17ba6-c227-4546-8819-c7b3a4b0208e",
//                null,
//                "testname",
//                null
//            )
//            Log.e("TAG", "nulla girdi")
//        }
        val userModel = posts.user
        holder.binding.favButton.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton?) {
                clickedTweetListener.crfButtonsListener("fav", posts.id!!, 1)
            }

            override fun unLiked(likeButton: LikeButton?) {
                clickedTweetListener.crfButtonsListener("fav", posts.id!!, -1)
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
            Log.e("holder", e.toString())
        }
    }

    inner class TweetViewHolder(val binding: TweetCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun post(model: Posts) {

            binding.tweetText.text = model.postContent.toString()
            if (model.postImageUrl != null) {
                if (model.postImageUrl.contains(".jpg") || model.postImageUrl.contains(".png")) {
                    Picasso.get().load(model.postImageUrl).into(binding.tweetImage)
                } else {

                }
            }
            binding.timeText.text = model.date.toString()
            if (model.postContent?.contains("#") == true) {
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
                        Log.e("TAG", len1.count().toString())

                        spannable.setSpan(
                            ForegroundColorSpan(Color.parseColor("#03A9F4")),
                            getTagIndex,
                            getTagIndex + lastIndex,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )

                        return spannable
                    }
                }
                binding.tweetText.setSpannableFactory(spannableFactory)

                binding.tweetText.setText(
                    model.postContent.replace("-n", "\n"),
                    TextView.BufferType.SPANNABLE
                )
            }

            binding.favText.text = model.postLike.toString()
        }

        fun userModel(model: Users) {
            try {

                Picasso.get().load(model.photoUrl).into(binding.profilePicture)
                binding.nameText.text = model.name
                binding.usernameText.text = "@" + model.userName
            } catch (e: Exception) {
                Log.e("userModel", e.toString())
            }

        }
    }

    interface ClickedTweetListener {
        fun crfButtonsListener(commentrtfav: String, tweetId: Int, currentlyCRFNumber: Int)
    }
}