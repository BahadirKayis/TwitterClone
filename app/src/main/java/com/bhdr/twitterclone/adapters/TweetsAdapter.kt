package com.bhdr.twitterclone.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.databinding.TweetCardBinding
import com.bhdr.twitterclone.diffcallback.TweetsCallBack
import com.bhdr.twitterclone.fragments.mainfragments.MainScreenFragmentDirections

import com.bhdr.twitterclone.models.Posts
import com.bhdr.twitterclone.models.Users
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.type.Date
import com.google.type.DateTime
import com.like.LikeButton
import com.like.OnLikeListener
import com.squareup.picasso.Picasso
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.time.Duration.Companion.hours


class TweetsAdapter(private val clickedTweetListener: ClickedTweetListener) :
    ListAdapter<Posts, TweetsAdapter.TweetViewHolder>(TweetsCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetViewHolder {
        val binding =
            TweetCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TweetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TweetViewHolder, position: Int) {
        val posts = getItem(position)
        val userModel = posts.user


        holder.binding.favButton.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton?) {
                clickedTweetListener.crfButtonsListener("fav", posts.id!!, 1)
            }

            override fun unLiked(likeButton: LikeButton?) {
                clickedTweetListener.crfButtonsListener("fav", posts.id!!, -1)
            }
        })

        holder.binding.tweetMenuText.setOnClickListener {
            it.findNavController()
                .navigate(MainScreenFragmentDirections.actionMainScreenFragmentToTweetBottomDialog("@" + userModel?.userName.toString()))
        }

        holder.post(posts)
        holder.userModel(userModel!!)
    }

    inner class TweetViewHolder(val binding: TweetCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun post(model: Posts) {

            Picasso.get().load(model.postImageUrl).into(binding.tweetImage)


            binding.timeText.text = model.date.toString()
            binding.tweetText.text = model.postContent.toString()
            binding.favText.text = model.postLike.toString()
        }

        fun userModel(model: Users) {


            Picasso.get().load(model.photoUrl).into(binding.profilePicture)
            binding.nameText.text = model.name
            binding.usernameText.text = "@" + model.userName


        }
    }
    interface ClickedTweetListener {
        fun crfButtonsListener(commentrtfav: String, tweetId: Int, currentlyCRFNumber: Int)
    }
}