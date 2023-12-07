package com.example.projectone.userdetail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.projectone.data.model.ResponseUserGithub
import com.example.projectone.data.repo.FavoriteRepository
import com.example.projectone.data.service.ApiClient
import com.example.projectone.utils.ResultViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DetailUserViewModel(application: Application) : ViewModel() {
    val resultDetailUser = MutableLiveData<ResultViewModel>()
    val resultFollowersUser = MutableLiveData<ResultViewModel>()
    val resultFollowingUser = MutableLiveData<ResultViewModel>()
    val resultFavoriteAdd = MutableLiveData<ResultViewModel>()
    val resultFavoriteDelete = MutableLiveData<ResultViewModel>()
    val resultFavoriteCheck = MutableLiveData<ResultViewModel>()

    val myGithubDetail = MutableLiveData<ResultViewModel>()
    val myFollowerUser = MutableLiveData<ResultViewModel>()
    val myFollowingUser = MutableLiveData<ResultViewModel>()

    private val mFavRepo: FavoriteRepository = FavoriteRepository(application)

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    fun insert(fav: ResponseUserGithub.Item) {
        mFavRepo.insert(fav)
        _isFavorite.value = true
    }

    fun delete(fav: ResponseUserGithub.Item) {
        mFavRepo.delete(fav)
        _isFavorite.value = false
    }



    fun findById(id: Int, function: () -> Unit) {
        viewModelScope.launch {
            val user = mFavRepo.findById(id)
            if(user != null) {
                function()
                _isFavorite.value = true
            }
        }
    }

    fun getUser(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiClient
                    .githubService
                    .getDetailUserFromGithub(username)
                emit(response)
            } .onStart {
                resultDetailUser.value = ResultViewModel.Loading(true)
            } .onCompletion {
                resultDetailUser.value = ResultViewModel.Loading(false)
            } .catch {
                Log.e("Error ", it.message.toString())
                it.printStackTrace()
                resultDetailUser.value = ResultViewModel.Error(it)
            } .collect {
                // Ubah tipe data yang dikirimkan ke resultSuccess.value
                resultDetailUser.value = ResultViewModel.Success(it)
            }
        }
    }

    fun getFollowerUser(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiClient
                    .githubService
                    .getFollowerUserFromGithub(username)
                emit(response)
            } .onStart {
                resultFollowersUser.value = ResultViewModel.Loading(true)
            } .onCompletion {
                resultFollowersUser.value = ResultViewModel.Loading(false)
            } .catch {
                Log.e("Error ", it.message.toString())
                it.printStackTrace()
                resultFollowersUser.value = ResultViewModel.Error(it)
            } .collect {
                // Ubah tipe data yang dikirimkan ke resultSuccess.value
                resultFollowersUser.value = ResultViewModel.Success(it)
            }
        }
    }

    fun getFollowingUser(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiClient
                    .githubService
                    .getFollowingUserFromGithub(username)
                emit(response)
            } .onStart {
                resultFollowingUser.value = ResultViewModel.Loading(true)
            } .onCompletion {
                resultFollowingUser.value = ResultViewModel.Loading(false)
            } .catch {
                Log.e("Error ", it.message.toString())
                it.printStackTrace()
                resultFollowingUser.value = ResultViewModel.Error(it)
            } .collect {
                // Ubah tipe data yang dikirimkan ke resultSuccess.value
                resultFollowingUser.value = ResultViewModel.Success(it)
            }
        }
    }

    fun getMyGithub(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiClient
                    .githubService
                    .getMyGithub(username)
                emit(response)
            } .onStart {
                myGithubDetail.value = ResultViewModel.Loading(true)
            } .onCompletion {
                myGithubDetail.value = ResultViewModel.Loading(false)
            } .catch {
                Log.e("Error ", it.message.toString())
                it.printStackTrace()
                myGithubDetail.value = ResultViewModel.Error(it)
            } .collect {
                // Ubah tipe data yang dikirimkan ke resultSuccess.value
                myGithubDetail.value = ResultViewModel.Success(it)
            }
        }
    }

    fun getMyFollower(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiClient
                    .githubService
                    .getMyFollowerGithub(username)
                emit(response)
            } .onStart {
                myFollowerUser.value = ResultViewModel.Loading(true)
            } .onCompletion {
                myFollowerUser.value = ResultViewModel.Loading(false)
            } .catch {
                Log.e("Error ", it.message.toString())
                it.printStackTrace()
                myFollowerUser.value = ResultViewModel.Error(it)
            } .collect {
                // Ubah tipe data yang dikirimkan ke resultSuccess.value
                myFollowerUser.value = ResultViewModel.Success(it)
            }
        }
    }

    fun getMyFollowing(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiClient
                    .githubService
                    .getMyFollowingGithub(username)
                emit(response)
            } .onStart {
                myFollowingUser.value = ResultViewModel.Loading(true)
            } .onCompletion {
                myFollowingUser.value = ResultViewModel.Loading(false)
            } .catch {
                Log.e("Error ", it.message.toString())
                it.printStackTrace()
                myFollowingUser.value = ResultViewModel.Error(it)
            } .collect {
                // Ubah tipe data yang dikirimkan ke resultSuccess.value
                myFollowingUser.value = ResultViewModel.Success(it)
            }
        }
    }
}

class DetailViewModelFactory(private val application: Application) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailUserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailUserViewModel(application) as T
        }
        throw IllegalAccessException("Unkwon ViewModel :" + modelClass.name)
    }
}