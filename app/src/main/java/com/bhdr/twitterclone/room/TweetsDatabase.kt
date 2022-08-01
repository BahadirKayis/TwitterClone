package com.bhdr.twitterclone.room

//
//@Database(entities = [Posts::class], version = 1)
//abstract class TweetsDatabase : RoomDatabase() {
//
//   abstract fun tweetDao(): TweetDaoInterface
//
//   companion object {
//      private var INSTANCE: TweetsDatabase? = null
//
//      fun getTweetsDatabase(context: Context): TweetsDatabase? {
//         if (INSTANCE == null) {
//            INSTANCE = Room.databaseBuilder(context, TweetsDatabase::class.java, "tweets.db")
//               .allowMainThreadQueries().build()
//         }
//         return INSTANCE
//      }
//   }
//}