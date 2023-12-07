package com.example.projectone

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_one.R
import com.example.project_one.databinding.ActivityMainBinding
import com.example.projectone.data.model.ResponseUserGithub
import com.example.projectone.fav.FavoriteActivity
import com.example.projectone.settings.SettingActivity
import com.example.projectone.settings.SettingPreferences
import com.example.projectone.settings.ThemeViewModel
import com.example.projectone.settings.ViewModelFactory
import com.example.projectone.settings.dataStore
import com.example.projectone.userdetail.AboutMe
import com.example.projectone.userdetail.DetailUserActivity
import com.example.projectone.utils.ResultViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding

    private lateinit var drawerLayout: DrawerLayout


    private val adapter by lazy {
        UserAdapter {
            Intent(this@MainActivity, DetailUserActivity::class.java).apply {
                putExtra("userItems", it)
                startActivity(this)
            }
        }
    }

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

//        val bottomNV = findViewById<BottomNavigationView>(R.id.bottomNav)
//        bottomNV.selectedItemId = R.id.bottom_home

//        bottomNV.setOnItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.bottom_home -> true
//                R.id.bottom_favorite -> {
//                    startActivity(Intent(applicationContext, FavoriteActivity::class.java))
//                    finish()
//                    true
//                }
//
//                else -> false
//            }
//        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.getSearchUser(query.toString())
                if (query.isNullOrBlank()) {
                    viewModel.getUser()
                    return true
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    viewModel.getUser()
                    return true
                }
                return false
            }
        })

        viewModel.resultUser.observe(this) {
            when (it) {
                is ResultViewModel.Success<*> -> {
                    adapter.setData(it.data as MutableList<ResponseUserGithub.Item>)
                }

                is ResultViewModel.Error -> {
                    Toast.makeText(this, it.exception.message.toString(), Toast.LENGTH_SHORT).show()
                }

                is ResultViewModel.Loading -> {
                    binding.progressBar.isVisible = it.isLoading
                }
            }
        }
        viewModel.getUser()

        drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_nav,
            R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

            R.id.nav_setting -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }

            R.id.nav_about_me -> {
                val intent = Intent(this, AboutMe::class.java)
                startActivity(intent)
            }

            R.id.nav_fav -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUser()
    }


}