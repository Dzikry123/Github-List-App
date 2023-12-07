package com.example.projectone.userdetail.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_one.R
import com.example.project_one.databinding.FragmentUserFollowerBinding
import com.example.projectone.UserAdapter
import com.example.projectone.data.model.ResponseUserGithub
import com.example.projectone.settings.SettingPreferences
import com.example.projectone.settings.ThemeViewModel
import com.example.projectone.settings.ViewModelFactory
import com.example.projectone.settings.dataStore
import com.example.projectone.userdetail.DetailUserViewModel
import com.example.projectone.utils.ResultViewModel

class UserFollower : Fragment() {
    private var binding : FragmentUserFollowerBinding? = null
    private val adapter by lazy {
        UserAdapter{

        }
    }

    private val viewModel by activityViewModels<DetailUserViewModel>()
    var type = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserFollowerBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.userFollower?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = this@UserFollower.adapter
        }

        when(type) {
            FOLLOWERS -> {
                viewModel.resultFollowersUser.observe(viewLifecycleOwner, this::manageFollowerUser)
            }

            FOLLOWING -> {
                viewModel.resultFollowingUser.observe(viewLifecycleOwner, this::manageFollowerUser)
            }
        }

        val preference = SettingPreferences.getInstansiasi(requireContext().dataStore)
        val themeViewModel = ViewModelProvider(this, ViewModelFactory(preference)).get(
            ThemeViewModel::class.java
        )


        themeViewModel.getThemeConfig().observe(viewLifecycleOwner) { isDarkMode: Boolean ->
            // Perbarui warna teks dalam adapter sesuai tema yang aktif
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

    }

    private fun manageFollowerUser(state: ResultViewModel) {
        when(state) {
            is ResultViewModel.Success<*> -> {
                adapter.setData(state.data as MutableList<ResponseUserGithub.Item>)
            }
            is ResultViewModel.Error -> {
                Toast.makeText(requireActivity(), state.exception.message.toString(), Toast.LENGTH_SHORT).show()
            }
            is ResultViewModel.Loading -> {
                binding?.progressBar?.isVisible = state.isLoading
            }
        }
    }

    companion object {
        const val FOLLOWERS = 100
        const val FOLLOWING = 101
        fun newInstance(type: Int) = UserFollower()
            .apply {
                this.type = type
            }
    }
}