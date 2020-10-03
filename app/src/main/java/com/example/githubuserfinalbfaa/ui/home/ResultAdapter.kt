package com.example.githubuserfinalbfaa.ui.home

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

class ResultAdapter: RecyclerView.Adapter<ResultAdapter.GitViewHolder>() {

    private var onItemClickCallback: OnitemClickCallback? = null

    fun setOnitemClickCallback(onItemClickCallback: OnitemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnitemClickCallback {
        fun onItemClicked(data: UserModel)
    }

    private val mData = ArrayList<UserModel>()

    fun setData(items: ArrayList<UserModel>){
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return GitViewHolder(mView)
    }

    override fun onBindViewHolder(holder: GitViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    inner class GitViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        @SuppressLint("ResourceType")
        fun bind(userModel: UserModel){
            with(itemView) {
                Glide.with(itemView.context)
                    .load(userModel.avatar)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
                    .error(R.drawable.ic_error)
                    .into(img_item_user)
                tv_item_username.text = userModel.login

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(userModel)}
            }
        }
    }

}