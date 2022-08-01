package com.bhdr.twitterclone.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bhdr.twitterclone.databinding.WhoToFollowCardBinding
import com.bhdr.twitterclone.helperclasses.picasso
import com.bhdr.twitterclone.models.Users

class WhoToFollowAdapter(
   private val clickedUserFollow: ClickedUserFollow,
   private val userFollow: List<Users>, val onClickDelete: (Int) -> Unit
) : RecyclerView.Adapter<WhoToFollowAdapter.ViewHolder>() {
   private var listData = userFollow

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val binding =
         WhoToFollowCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return ViewHolder(binding)
   }

   override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val user = listData[position]
      holder.binding.apply {
         profilePicture.picasso(user.photoUrl.toString())
         nameText.text = user.name
         followButton.setOnClickListener {
            onClickDelete(position)
            clickedUserFollow.followButtonsListener(user.id!!)

         }
         idText.text = "@" + user.userName
      }


   }

   override fun getItemCount(): Int {
      return listData.size
   }

   inner class ViewHolder(val binding: WhoToFollowCardBinding) :
      RecyclerView.ViewHolder(binding.root) {

   }

   interface ClickedUserFollow {
      fun followButtonsListener(followId: Int)
   }

   fun setItem(items: List<Users>) {
      listData = items
      notifyDataSetChanged()
   }
//    fun deleteItem(index: Int) {
//          listData.removeAt(index)
//        notifyDataSetChanged()
//    }
}

