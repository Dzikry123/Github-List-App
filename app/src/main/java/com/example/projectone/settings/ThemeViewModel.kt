package com.example.projectone.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ThemeViewModel (private val preference: SettingPreferences) : ViewModel() {
    fun getThemeConfig(): LiveData<Boolean> {
        return preference.getThemeConfig().asLiveData()
    }

    fun saveThemeConfig(isDarkMode: Boolean) {
        viewModelScope.launch {
            preference.saveThemeConfig(isDarkMode)
        }
    }
}