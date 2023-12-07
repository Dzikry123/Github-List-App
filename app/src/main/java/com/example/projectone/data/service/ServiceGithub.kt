package com.example.projectone.data.service

import com.example.project_one.BuildConfig
import com.example.projectone.data.model.ResponseDetailUserGithub
import com.example.projectone.data.model.ResponseUserGithub
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ServiceGithub {

    @JvmSuppressWildcards
    @GET("users")
    suspend fun getUserFromGithub(  @Header("Authorization")
                                        authorization: String = BuildConfig.TOKEN) : MutableList<ResponseUserGithub.Item>

    @JvmSuppressWildcards
    @GET("users/{username}")
    suspend fun getDetailUserFromGithub( @Path("username") username: String,
                                         @Header("Authorization")
                                         authorization: String = BuildConfig.TOKEN) : ResponseDetailUserGithub

    @JvmSuppressWildcards
    @GET("users/{username}/followers")
    suspend fun getFollowerUserFromGithub( @Path("username") username: String,
                                           @Header("Authorization")
                                           authorization: String = BuildConfig.TOKEN) : MutableList<ResponseUserGithub.Item>

    @JvmSuppressWildcards
    @GET("users/{username}/following")
    suspend fun getFollowingUserFromGithub( @Path("username") username: String,
                                            @Header("Authorization")
                                            authorization: String = BuildConfig.TOKEN) : MutableList<ResponseUserGithub.Item>

    @JvmSuppressWildcards
    @GET("search/users")
    suspend fun searchUserFromGithub( @QueryMap params: Map<String, Any>,
                                      @Header("Authorization")
                                      authorization: String = BuildConfig.TOKEN) : ResponseUserGithub


//    My Github
    @JvmSuppressWildcards
    @GET("users/Dzikry123")
    suspend fun getMyGithub(
                            @Header("Authorization")
                            authorization: String = BuildConfig.TOKEN) : ResponseDetailUserGithub

    @JvmSuppressWildcards
    @GET("users/dzikry123/followers")
    suspend fun getMyFollowerGithub(@Path("Dzikry123") username: String,
                                    @Header("Authorization")
                                    authorization: String = BuildConfig.TOKEN) : MutableList<ResponseUserGithub>

    @JvmSuppressWildcards
    @GET("users/dzikry123/following")
    suspend fun getMyFollowingGithub(@Path("Dzikry123") username: String,
                                     @Header("Authorization")
                                     authorization: String = BuildConfig.TOKEN) : MutableList<ResponseUserGithub.Item>



}