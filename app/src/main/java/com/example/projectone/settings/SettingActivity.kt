package com.example.projectone.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.project_one.R
import com.example.project_one.databinding.ActivitySettingBinding
import com.example.projectone.MainViewModel
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding

    private val userViewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val changeTheme = findViewById<SwitchMaterial>(R.id.switch_theme)

        val preference = SettingPreferences.getInstansiasi(application.dataStore)
        val themeViewModel = ViewModelProvider(this, ViewModelFactory(preference)).get(
            ThemeViewModel::class.java
        )

        themeViewModel.getThemeConfig().observe(this) { isDarkMode: Boolean ->
            if (isDarkMode) {
                binding.switchTheme.text = "Dark Mode"
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                binding.switchTheme.text = "Light Mode"
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            binding.switchTheme.isChecked = isDarkMode
        }

        changeTheme.setOnCheckedChangeListener{_: CompoundButton?, isChecked: Boolean ->
            themeViewModel.saveThemeConfig(isChecked)
            userViewModel.getUser()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                userViewModel.getUser()
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}