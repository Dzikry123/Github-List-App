package com.example.projectone.userdetail

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class DetailUserAdapter(fragmentBase: DetailUserActivity, private val fragmentContent: MutableList<Fragment>)
    : FragmentStateAdapter(fragmentBase) {
    override fun getItemCount(): Int = fragmentContent.size

    override fun createFragment(position: Int): Fragment = fragmentContent[position]

}