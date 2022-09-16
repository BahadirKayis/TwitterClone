package com.bhdr.twitterclone.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import com.bhdr.twitterclone.common.picasso
import com.bhdr.twitterclone.data.model.remote.Users
import com.bhdr.twitterclone.databinding.AutocompletetextviewSearchBinding

class SearchUserAdapter(
   context: Context,
   @LayoutRes private val layoutResource: Int
) : ArrayAdapter<Users>(context, layoutResource)/*, Filterable*/ {
   // private var nList: List<Users> = emptyList() //Local Filter
   private var listUser: List<Users> = emptyList()

   var onClickItem: (String) -> Unit = {}
   override fun getCount(): Int {
      return listUser.size
   }

   override fun getItem(position: Int): Users {
      return listUser[position]
   }

   override fun getItemId(position: Int): Long {
      return listUser[position].id!!.toLong()
   }

   fun userDataChanged(list: List<Users>) {
      //  nList = list //Local Filter
      listUser = list //Cloud Filter
      notifyDataSetChanged()
   }

   override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
      val binding: AutocompletetextviewSearchBinding
      var row = convertView

      if (row == null) {
         val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
         binding = AutocompletetextviewSearchBinding.inflate(inflater, parent, false)
         row = binding.root
      } else {
         binding = AutocompletetextviewSearchBinding.bind(row)
      }

      with(binding) {
         with(listUser[position]) {
            cons.setOnClickListener { onClickItem(userName.toString()) }
            profilePicture.picasso(photoUrl.toString())
            nameSurname.text = name
            "@$userName".also { userNameID.text = it }

         }

      }

      return row
   }
//Local Filter
//   override fun getFilter(): Filter {
//      return object : Filter() {
//         override fun publishResults(charSequence: CharSequence?, filterResult: FilterResults?) {
//            listUser = filterResult!!.values as List<Users>
//            notifyDataSetChanged()
//         }
//
//         override fun performFiltering(charSequence: CharSequence?): FilterResults {
//            val queryString = charSequence.toString().toLowerCasePreservingASCIIRules()
//            val filterResults = FilterResults()
//            filterResults.values = if (queryString.isEmpty())
//               nList else
//               nList.filter {
//                  it.userName!!.toLowerCasePreservingASCIIRules()
//                     .contains(queryString) || it.name!!.toLowerCasePreservingASCIIRules()
//                     .contains(queryString)
//               }
//            return filterResults
//         }
//
//
//      }
//   }

}