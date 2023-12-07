package com.example.projectone

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.project_one.R
import com.example.project_one.databinding.ListItemBinding
import com.example.projectone.data.model.ResponseUserGithub

class UserAdapter(private val data: MutableList<ResponseUserGithub.Item> = mutableListOf(),
                  private val listener: (ResponseUserGithub.Item) -> Unit) :
        RecyclerView.Adapter<UserAdapter.UserViewHolder>() {



    fun setData(data: MutableList<ResponseUserGithub.Item>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    class UserViewHolder(private val v:ListItemBinding) : RecyclerView.ViewHolder(v.root) {
        fun bind(item: ResponseUserGithub.Item, ) {
            v.imageUser.load(item.avatar_url) {
                transformations(CircleCropTransformation())
            }
            v.tvUserSlice.text = item.login
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
       return UserViewHolder(ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.itemView.setOnClickListener{
            listener(item)
        }
    }
}