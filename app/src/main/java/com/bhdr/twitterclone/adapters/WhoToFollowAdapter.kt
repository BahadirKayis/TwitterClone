package com.bhdr.twitterclone.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bhdr.twitterclone.databinding.TweetCardBinding
import com.bhdr.twitterclone.databinding.WhoToFollowCardBinding
import com.bhdr.twitterclone.models.Posts
import com.bhdr.twitterclone.models.Users
import com.google.firebase.firestore.auth.User
import com.squareup.picasso.Picasso

class WhoToFollowAdapter(private val userFollow: List<Users>) : RecyclerView.Adapter<WhoToFollowAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            WhoToFollowCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(userFollow[position].photoUrl).into(holder.binding.imageView5)
    }
    override fun getItemCount(): Int {
        return userFollow.size
    }

    inner class ViewHolder(val binding: WhoToFollowCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }


}