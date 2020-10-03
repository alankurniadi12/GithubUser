package com.example.githubuserfinalbfaa.ui.detail.followers

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuserfinalbfaa.R
import com.example.githubuserfinalbfaa.model.UserModel
import kotlinx.android.synthetic.main.item_user.view.*

class FollowingAdapter: RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder>() {

    private val mData= ArrayList<UserModel>()

    fun setData(items: ArrayList<UserModel>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return FollowingViewHolder(mView)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: FollowingViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    class FollowingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        @SuppressLint("ResourceType")
        fun bind(userModel: UserModel){
            with(itemView){
                Glide.with(itemView.context)
                    .load(userModel.avatar)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
                    .error(R.drawable.ic_error)
                    .into(img_item_user)
                tv_item_username.text = userModel.login
            }
        }
    }
}