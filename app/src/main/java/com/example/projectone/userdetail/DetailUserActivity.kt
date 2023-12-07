package com.example.projectone.userdetail

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import coil.load
import coil.transform.CircleCropTransformation
import com.example.project_one.R
import com.example.project_one.databinding.ActivityDetailUserBinding
import com.example.projectone.data.model.ResponseDetailUserGithub
import com.example.projectone.data.model.ResponseUserGithub
import com.example.projectone.userdetail.fragment.UserFollower
import com.example.projectone.utils.ResultViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch


class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private val viewModel by viewModels<DetailUserViewModel>{
        DetailViewModelFactory(application)
    }

    private var isFavorite = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val item = intent.getParcelableExtra<ResponseUserGithub.Item>("userItems")
        val username = item?.login ?: "User Not Found"

        viewModel.resultDetailUser.observe(this) {
            when (it) {
                is ResultViewModel.Success<*> -> {
                    val user = it.data as ResponseDetailUserGithub
                    binding.imageUser.load(user.avatar_url) {
                        transformations(CircleCropTransformation())
                    }

                    binding.fullNames.text = user.name
                    binding.username.text = user.login
                    binding.followerCount.text = user.followers.toString()
                    binding.followingCount.text = user.following.toString()
                    binding.repositoryCount.text = user.public_repos.toString()


                }

                is ResultViewModel.Error -> {
                    Toast.makeText(this, it.exception.message.toString(), Toast.LENGTH_SHORT).show()
                }

                is ResultViewModel.Loading -> {
                    binding.progressBar.isVisible = it.isLoading
                }
            }
        }

        viewModel.getUser(username)
        val fragments = mutableListOf<Fragment>(
            UserFollower.newInstance(UserFollower.FOLLOWERS),
            UserFollower.newInstance(UserFollower.FOLLOWING)
        )
        val titleTabFragments = mutableListOf(
            getString(R.string.follower_tab), getString(R.string.following_tab)
        )
        val adapter = DetailUserAdapter(this, fragments)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = titleTabFragments[position]
        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    viewModel.getFollowerUser(username)
                } else {
                    viewModel.getFollowingUser(username)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
        viewModel.getFollowerUser(username)

        viewModel.resultFavoriteAdd.observe(this) {
            updateFavoriteButtonColor(true)
        }
        viewModel.resultFavoriteDelete.observe(this) {
            updateFavoriteButtonColor(false)
        }

        viewModel.findById(item?.id ?: 0) {
            viewModel.viewModelScope.launch {
                updateFavoriteButtonColor(true )
            }
        }

        binding.favoriteButton.setOnClickListener {
            val titleAdd = getString(R.string.notif_title_add)
            val titleDelete = getString(R.string.notif_title_delete)
            val addMessage = getString(R.string.notif_add_message)
            val deleteMessage = getString(R.string.notif_delete_message)
            val userId = item
            if (userId != null) {
                viewModel.viewModelScope.launch {
                    if (viewModel.isFavorite.value == true) {
                        Log.d("DetailUserActivity", "isFavorite: $isFavorite")
                        viewModel.delete(userId)
//                        viewModel.setFavorite(isFavorite)
                        updateFavoriteButtonColor(isFavorite)
                        sendNotif(titleDelete, deleteMessage)
                        Log.d("DetailUserActivity", "User dengan ID $userId dihapus dari favorit.")
                    } else {
                        viewModel.insert(userId)
//                        viewModel.setFavorite(!isFavorite)
                        updateFavoriteButtonColor(!isFavorite)
                        Log.d("DetailUserActivity", "User dengan ID $userId ditambah ke favorit.")
                        sendNotif(titleAdd, addMessage)
                    }
                }
            }

        }

        // Notifications Message


        if (Build.VERSION.SDK_INT >= 33) {
            permitReqNotif.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

    }


    // Notification Utility
    private fun sendNotif(title: String, message: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.fav_pink)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSubText(getString(R.string.notif_subtittle))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            builder.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private val permitReqNotif =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notifications permission rejected", Toast.LENGTH_SHORT).show()
            }
        }

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "github_channel_01"
        private const val CHANNEL_NAME = "github list user"
    }

    private fun updateFavoriteButtonColor(isFavorite: Boolean) {
        val colorRes = if (isFavorite) R.color.colorFav else android.R.color.white
        val color = ContextCompat.getColor(this, colorRes)
        binding.favoriteButton.setColorFilter(color)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}