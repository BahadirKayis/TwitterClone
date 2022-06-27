package com.bhdr.twitterclone.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bhdr.twitterclone.databinding.AgendaCardBinding

import com.bhdr.twitterclone.models.Tags

class TagsAdapter(private  val tagsList:List<Tags>): RecyclerView.Adapter<TagsAdapter.TagsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsViewHolder {
        val binding =
            AgendaCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TagsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TagsViewHolder, position: Int) {
        holder.binding.categoryText.text = "Türkiye konumunda gündemde"
        holder.binding.hashtagText.text= tagsList[position].tagName
      //  holder.binding.tweetNumberText.text=tagsList[position].TweetSayısı
        //tage tıklanınca başka sayfada o tagli postları açacak

    }

    override fun getItemCount(): Int {
        return tagsList.size
    }
    inner class TagsViewHolder(val binding: AgendaCardBinding) :
        RecyclerView.ViewHolder(binding.root) {


    }
}