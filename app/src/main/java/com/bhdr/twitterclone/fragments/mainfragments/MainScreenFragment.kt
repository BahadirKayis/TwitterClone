package com.bhdr.twitterclone.fragments.mainfragments


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.adapters.TweetsAdapter
import com.bhdr.twitterclone.databinding.FragmentMainScreenBinding
import com.bhdr.twitterclone.helperclasses.*
import com.bhdr.twitterclone.models.Posts
import com.bhdr.twitterclone.models.SignalRModel
import com.bhdr.twitterclone.models.Users
import com.bhdr.twitterclone.repos.TweetRepository
import com.bhdr.twitterclone.room.RoomViewModel
import com.bhdr.twitterclone.room.TweetsRoomModel
import com.bhdr.twitterclone.viewmodels.mainviewmodel.MainViewModel
import com.bhdr.twitterclone.viewmodels.mainviewmodel.TweetViewModel
import com.squareup.picasso.Picasso
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainScreenFragment : Fragment(R.layout.fragment_main_screen),
   TweetsAdapter.ClickedTweetListener {
   private val binding by viewBinding(FragmentMainScreenBinding::bind)
   private val viewModel by lazy { TweetViewModel() }
   private val viewModelMain by lazy { MainViewModel() }
   private val tweetAdapter by lazy { TweetsAdapter(this) }
   private var mutableNotFollowTweetOrLikeList: List<SignalRModel>? = null
   var userProfileClickListener: MainScreenInterFace? = null
   private var mutableFollowNewTweetHashMap: HashMap<Int, String>? = null

   var tweetsRoom: MutableList<Posts>? = null

   //Room Repoya taşınacak
   private lateinit var allRoomTweets: List<TweetsRoomModel>
   lateinit var viewModelRoom: RoomViewModel

   var userId: Int? = null
   override fun onAttach(context: Context) {
      super.onAttach(context)

      if (context is MainScreenInterFace) {
         userProfileClickListener = context
      } else {
         throw RuntimeException(context.toString())
      }
   }


   private fun room() {

      viewModelRoom = ViewModelProvider(requireParentFragment())[RoomViewModel::class.java]

      viewModelRoom.allRoomTweets.observe(viewLifecycleOwner) {

         Log.e("Room", it.toString())
         it.forEach { itFor ->
            var post: Posts? = null
            var users: Users? = null
            itFor?.user.apply {
               users = Users(
                  id = id,
                  photoUrl = this!!.photoUrl,
                  userName = userName,
                  name = name,
                  date = null,
                  email = null,
                  followers = null,
                  messages = null,
                  phone = null,
                  userPassword = null,
                  posts = null
               )
            }
            itFor?.apply {
               post = Posts(
                  date = date,
                  id = id,
                  postContent = postContent,
                  postImageUrl = postImageUrl,
                  postLike = postLike,
                  user = users,
                  userId = userId,
                  followers = null,
                  tags = null
               )
            }
            tweetsRoom?.toMutableList()?.add(post!!)
            tweetAdapter.submitList(tweetsRoom)
            Log.e("Room", tweetsRoom.toString())
         }


      }

   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      userId = requireContext().userId()
      viewModelMain.getFollowedUserIdList(userId!!)

      viewModelMain.startSignalR(requireContext())
      binding.addTweetFAB.setOnClickListener {
         Navigation.findNavController(requireView())
            .navigate(R.id.action_mainScreenFragment_to_addTweetFragment)
      }
      viewModel.getPosts(userId!!)
      room()
      viewModelObservable()
      binding.profilePicture.picasso(requireContext().userPhotoUrl())

      binding.seeNewTweet.setOnClickListener {

      }

      viewModelMain.mutableFollowNewTweet.observe(viewLifecycleOwner) {
         Log.e("try", it.size.toString())
         if (it!!.size == 1) {
            val url: String = it.values.toTypedArray()[0]
            Log.e("try", url)
            Picasso.get().load(url).into(binding.userPhoto1)
            Picasso.get().load(it.values.toTypedArray()[0]).into(binding.userPhoto2)
            binding.seeNewTweet.visible()
            binding.userPhoto1.visible()
            binding.userPhoto2.visible()
         } else if (it.size >= 3) {
            Picasso.get().load(it.values.toTypedArray()[2]).into(binding.userPhoto3)
            binding.userPhoto3.visible()
         }
      }

      mutableFollowNewTweetHashMap = viewModelMain.mutableFollowNewTweet.value


      mutableFollowNewTweetHashMap?.apply {
         if (this.size == 1) {
            val url: String = this.values.toTypedArray()[0]
            Log.e("try", url)
            Picasso.get().load(url).into(binding.userPhoto1)
            Picasso.get().load(this.values.toTypedArray()[1]).into(binding.userPhoto2)
            binding.seeNewTweet.visible()
            binding.userPhoto1.visible()
            binding.userPhoto2.visible()
         } else if (this.size >= 3) {
            Picasso.get().load(this.values.toTypedArray()[2]).into(binding.userPhoto3)
            binding.userPhoto3.visible()
         }
      }
   }

   private fun viewModelObservable() {

      viewModel.apply {
         mainStatus.observe(viewLifecycleOwner) {
            when (it!!) {
               TweetRepository.MainStatus.LOADING -> binding.lottiAnim.visible()

               TweetRepository.MainStatus.ERROR -> binding.lottiAnim.gone()
               TweetRepository.MainStatus.DONE -> {
                  binding.lottiAnim.gone();lifecycleScope.launch {
                     delay(2000)
                     //    startSignalR(requireContext())
                  }
               }
            }
         }
//Bazı değerler olmadığı için  Tweet as TweetsRoomModel diyemiyordum ClassCastException hatası veriyordu
         //Retrofit için kullandığım modeli kullansam çok fazla tablo ve gereksiz değer çekmem gerekiyordu bende böyle bir çözüm buldum
         tweets.observe(viewLifecycleOwner) {
            //  tweetAdapter.submitList(it)
//            try {
//               it.forEach { it ->
//                  var userRoomModel: UsersRoomModel? = null
//                  it.user?.apply {
//                     userRoomModel = UsersRoomModel(id, photoUrl, userName, name)
//                  }
//                  it.apply {
//                     viewModelRoom.tweetInsert(
//                        TweetsRoomModel(
//                           date,
//                           id,
//                           postContent,
//                           postImageUrl,
//                           postLike,
//                           userRoomModel,
//                           userId
//                        )
//                     )
//                  }
//
//               }
//
//            } catch (e: Exception) {
//
//               Log.e("ex", e.toString())
//            }

         }


      }

      viewModelMain.apply {
         mutableNotFollowTweetOrLikeList = mutableNotFollowTweetOrLike.value
         mutableNotFollowTweetOrLikeList.apply {
            //SearchaDAPTER
         }
         mutableNotFollowTweetOrLike.value?.apply {


         }


      }

      viewModel.liked.observe(viewLifecycleOwner) {

      }

      binding.tweetsRecyclerView.apply {
         layoutManager = LinearLayoutManager(
            binding.tweetsRecyclerView.context,
            LinearLayoutManager.VERTICAL,
            false
         )
         adapter = tweetAdapter
      }

      binding.profilePicture.setOnClickListener {
         userProfileClickListener?.openDrawerClick()
         //   TODO invoke class yapılacak iki sayfa arasında iki interface olmadığı için
      }
   }

   override fun crfButtonsListener(
      commentrtfav: String,
      tweetId: Int,
      currentlyCRFNumber: Int
   ) {
      when (commentrtfav) {
         "fav" -> viewModel.postLiked(tweetId, currentlyCRFNumber, requireContext())
         // "comment" -> viewModel.commentTweet(tweetDocId, currentlyCRFNumber)
         // "rt" -> viewModel.rtTweet(tweetDocId, currentlyCRFNumber)

      }
   }


   interface MainScreenInterFace {
      fun openDrawerClick()
   }

}



