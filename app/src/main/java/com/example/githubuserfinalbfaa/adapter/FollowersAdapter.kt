package com.example.githubuserfinalbfaa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserfinalbfaa.R
import com.example.githubuserfinalbfaa.model.UserModel
import kotlinx.android.synthetic.main.item_user.view.*

class FollowersAdapter: RecyclerView.Adapter<FollowersAdapter.FollowerViewHolder>() {

    private val mData= ArrayList<UserModel>()

    fun setData(items: ArrayList<UserModel>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return FollowerViewHolder(mView)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: FollowerViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    class FollowerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(userModel: UserModel){
            with(itemView){
                Glide.with(itemView.context)
                    .load(userModel.avatar)
                    .into(img_item_user)
                tv_item_username.text = userModel.login
            }
        }
    }

}