package com.example.projectone.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.projectone.data.model.ResponseUserGithub

@Dao
interface FavDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFav(user: ResponseUserGithub.Item)

    @Query("SELECT * FROM USER")
    fun loadAll(): LiveData<MutableList<ResponseUserGithub.Item>>

    @Query("SELECT * FROM USER WHERE id LIKE :id LIMIT 1")
    fun findByid(id: Int): ResponseUserGithub.Item?

    @Delete
    fun deleteFav(user: ResponseUserGithub.Item)
}