package com.bhdr.twitterclone.room

//class RoomRepository(private val tweetDao: TweetDaoInterface) {
//   val tweetsRoom: LiveData<List<TweetsRoomModel?>> = tweetDao.allTweet()
//
//   suspend fun tweetsInsert(tweets: List<TweetsRoomModel>) {
//      try {
//         tweetDao.addTweet(tweets)
//      } catch (e: ClassCastException) {
//         Log.e("class", e.toString())
//      }
//   }
//   suspend fun tweetsUpdate(tweets: List<TweetsRoomModel>) {
//      //tweetDao.updateTweet(tweets)
//   }
//
//   suspend fun tweetsDelete() {
//      tweetDao.deleteTweet()
//   }
//}