package com.bhdr.twitterclone.room

//class RoomViewModel(application: Application) : AndroidViewModel(application) {
//   val allRoomTweets: LiveData<List<TweetsRoomModel?>>
//   private  val repository: RoomRepository
//   init {
//      val dao = TweetsDatabase.getTweetsDatabase(application)?.tweetDao()
//      repository = RoomRepository(dao!!)
//      allRoomTweets = repository.tweetsRoom
//   }
//
//   fun tweetInsert(tweets: List<TweetsRoomModel>) {
//      try {
//
//         viewModelScope.launch {
//
//            repository.tweetsInsert(tweets)
//         }
//      } catch (e: ClassCastException) {
//         Log.e("class", e.toString())
//      }
//   }
//
//}