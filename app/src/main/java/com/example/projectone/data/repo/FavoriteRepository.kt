package com.example.projectone.data.repo

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.projectone.data.db.FavDao
import com.example.projectone.data.db.FavDb
import com.example.projectone.data.model.ResponseUserGithub
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository( application: Application) {

    private val mFavDao: FavDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = FavDb.getDatabase(application)
        mFavDao = db.FavDao()
    }
    fun getAllFavUser(): LiveData<MutableList<ResponseUserGithub.Item>> = mFavDao.loadAll()
    fun insert(fav: ResponseUserGithub.Item) {
        executorService.execute { mFavDao.insertFav(fav) }
    }
    fun delete(fav: ResponseUserGithub.Item) {
        executorService.execute { mFavDao.deleteFav(fav) }
    }


    suspend fun findById(id: Int): ResponseUserGithub.Item? {
        return withContext(Dispatchers.IO) {
            mFavDao.findByid(id)
        }
    }

}