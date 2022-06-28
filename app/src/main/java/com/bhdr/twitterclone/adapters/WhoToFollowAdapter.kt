package com.bhdr.twitterclone.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bhdr.twitterclone.databinding.TweetCardBinding
import com.bhdr.twitterclone.databinding.WhoToFollowCardBinding
import com.bhdr.twitterclone.models.Posts
import com.bhdr.twitterclone.models.Users
import com.google.firebase.firestore.auth.User
import com.squareup.picasso.Picasso

class WhoToFollowAdapter(
    private val clickedUserFollow: ClickedUserFollow,
    private val userFollow: List<Users>
) : RecyclerView.Adapter<WhoToFollowAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            WhoToFollowCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userFollow[position]
        Picasso.get().load(user.photoUrl).into(holder.binding.profilePicture)
        holder.binding.nameText.text = user.name
        holder.binding.followButton.setOnClickListener {
            clickedUserFollow.followButtonsListener(user.id!!)
            Log.e("Click", user.id.toString() )
        }
        holder.binding.idText.text = "@" + user.userName

    }

    override fun getItemCount(): Int {
        return userFollow.size
    }

    inner class ViewHolder(val binding: WhoToFollowCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    interface ClickedUserFollow {
        fun followButtonsListener(followId: Int)
    }
}

