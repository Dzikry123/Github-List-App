package com.example.projectone

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectone.data.service.ApiClient
import com.example.projectone.utils.ResultViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    val resultUser = MutableLiveData<ResultViewModel>()

    fun getUser() {
        viewModelScope.launch {
            flow {
                val response = ApiClient
                    .githubService
                    .getUserFromGithub()
                emit(response)
            } .onStart {
                resultUser.value = ResultViewModel.Loading(true)
            } .onCompletion {
                resultUser.value = ResultViewModel.Loading(false)
            } .catch {
                Log.e("Error ", it.message.toString())
                it.printStackTrace()
                resultUser.value = ResultViewModel.Error(it)
            } .collect {
                // Ubah tipe data yang dikirimkan ke resultSuccess.value
                resultUser.value = ResultViewModel.Success(it)
            }
        }
    }

    fun getSearchUser(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiClient
                    .githubService
                    .searchUserFromGithub(mapOf(
                        "q" to username,
                        "per_page" to 10
                    ))
                emit(response)
            } .onStart {
                resultUser.value = ResultViewModel.Loading(true)
            } .onCompletion {
                resultUser.value = ResultViewModel.Loading(false)
            } .catch {
                Log.e("Error ", it.message.toString())
                it.printStackTrace()
                resultUser.value = ResultViewModel.Error(it)
            } .collect {
                // Ubah tipe data yang dikirimkan ke resultSuccess.value
                resultUser.value = ResultViewModel.Success(it.items)
            }
        }
    }
}