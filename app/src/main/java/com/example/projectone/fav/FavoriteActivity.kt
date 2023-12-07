package com.example.projectone.fav

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_one.R
import com.example.project_one.databinding.ActivityFavoriteBinding
import com.example.projectone.MainActivity
import com.example.projectone.UserAdapter
import com.example.projectone.settings.SettingPreferences
import com.example.projectone.settings.ThemeViewModel
import com.example.projectone.settings.ViewModelFactory
import com.example.projectone.settings.dataStore
import com.example.projectone.userdetail.DetailUserActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding

    private val adapter by lazy {
        UserAdapter{
            Intent(this@FavoriteActivity, DetailUserActivity::class.java).apply {
                putExtra("userItems", it)
                startActivity(this)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.FavoriteListRv.layoutManager = LinearLayoutManager(this)
        binding.FavoriteListRv.adapter = adapter

        // Inisialisasi ViewModel
        val viewModel = ViewModelProvider(this, FavoriteViewModelFactory(application)).get(
            FavoriteViewModel::class.java
        )

//        val bottomNV = findViewById<BottomNavigationView>(R.id.bottomNav)
//        bottomNV.selectedItemId = R.id.bottom_favorite
//
//        bottomNV.setOnItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.bottom_home -> {
//                    startActivity(Intent(applicationContext, MainActivity::class.java))
//                    finish()
//                    true
//                }
//                R.id.bottom_favorite -> {
//                    startActivity(Intent(applicationContext, FavoriteActivity::class.java))
//                    finish()
//                    true
//                }
//                else -> false
//            }
//        }

        // Mengamati LiveData dari ViewModel
        viewModel.getAllFavUser().observe(this) { favUsers ->
            // Update data dalam adapter
            adapter.setData(favUsers)
        }

        val preference = SettingPreferences.getInstansiasi(application.dataStore)
        val themeViewModel = ViewModelProvider(this, ViewModelFactory(preference)).get(
            ThemeViewModel::class.java
        )

        themeViewModel.getThemeConfig().observe(this) { isDarkMode: Boolean ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}
