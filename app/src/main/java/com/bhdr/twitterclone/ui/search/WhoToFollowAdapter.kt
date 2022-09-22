package com.bhdr.twitterclone.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bhdr.twitterclone.common.picasso
import com.bhdr.twitterclone.data.model.remote.Users
import com.bhdr.twitterclone.databinding.WhoToFollowCardBinding

class WhoToFollowAdapter(
   private val clickedUserFollow: ClickedUserFollow, private var userList: MutableList<Users>
) : RecyclerView.Adapter<WhoToFollowAdapter.ViewHolder>() {


   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val binding =
         WhoToFollowCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return ViewHolder(binding)
   }

   override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val user = userList[position]
      holder.binding.apply {
         profilePicture.picasso(user.photoUrl.toString())
         nameText.text = user.name
         followButton.setOnClickListener {
            itemDelete(position)
            clickedUserFollow.followButtonsListener(user.id!!)
         }
         "@${user.userName}".also { idText.text = it }
      }
   }

   override fun getItemCount(): Int {
      return userList.size
   }

   inner class ViewHolder(val binding: WhoToFollowCardBinding) :
      RecyclerView.ViewHolder(binding.root) {
   }

   interface ClickedUserFollow {
      fun followButtonsListener(followId: Int)
   }

   private fun itemDelete(position: Int) {
      userList.removeAt(position)
      notifyItemRemoved(position)

   }
}

