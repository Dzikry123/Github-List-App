package com.example.projectone.data.db

import android.content.Context
import android.provider.ContactsContract
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projectone.data.model.ResponseUserGithub

@Database(entities = [ResponseUserGithub.Item::class], version = 1)
abstract class FavDb : RoomDatabase() {
    abstract fun FavDao(): FavDao
    companion object {
        @Volatile
        private var INSTANCE: FavDb? = null
        @JvmStatic
        fun getDatabase(context: Context): FavDb {
            if (INSTANCE == null) {
                synchronized(FavDb::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        FavDb::class.java, "fav_database")
                        .build()
                }
            }
            return INSTANCE as FavDb
        }
    }
}