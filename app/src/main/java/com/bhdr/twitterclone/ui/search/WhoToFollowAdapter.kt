package com.bhdr.twitterclone.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bhdr.twitterclone.databinding.WhoToFollowCardBinding
import com.bhdr.twitterclone.common.picasso
import com.bhdr.twitterclone.data.model.remote.Users

class WhoToFollowAdapter(
   private val clickedUserFollow: ClickedUserFollow, private val userList: List<Users>,
   val onClickDelete: (Int) -> Unit
) : RecyclerView.Adapter<WhoToFollowAdapter.ViewHolder>() {
   private var user: List<Users> = userList

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val binding =
         WhoToFollowCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return ViewHolder(binding)
   }

   override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val user = user[position]
      holder.binding.apply {
         profilePicture.picasso(user.photoUrl.toString())
         nameText.text = user.name
         followButton.setOnClickListener {
            onClickDelete(position)
            clickedUserFollow.followButtonsListener(user.id!!)
         }
         "@${user.userName}".also { idText.text = it }
      }
   }

   override fun getItemCount(): Int {
      return user.size
   }

   inner class ViewHolder(val binding: WhoToFollowCardBinding) :
      RecyclerView.ViewHolder(binding.root) {
   }

   interface ClickedUserFollow {
      fun followButtonsListener(followId: Int)
   }

   fun setItem(items: List<Users>, position: Int) {
      user = items
      notifyItemRemoved(position)
   }
}

