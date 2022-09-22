package com.bhdr.twitterclone.ui.search


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bhdr.twitterclone.databinding.AgendaCardBinding

class TagsAdapter(private val tagsList: List<String>) :
   RecyclerView.Adapter<TagsAdapter.TagsViewHolder>() {

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsViewHolder {
      val binding =
         AgendaCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return TagsViewHolder(binding)
   }

   override fun onBindViewHolder(holder: TagsViewHolder, position: Int) {
      with(holder) {
         with(binding) {
            "Türkiye konumunda gündemde".also { categoryText.text = it }
            hashtagText.text = tagsList[position + 1]
         }
      }

      //  holder.binding.tweetNumberText.text=tagsList[position].TweetSayısı
   }

   override fun getItemCount(): Int {
      return tagsList.size - 1
   }

   inner class TagsViewHolder(val binding: AgendaCardBinding) :
      RecyclerView.ViewHolder(binding.root) {
   }
}