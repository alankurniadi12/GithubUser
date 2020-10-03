package com.example.githubuserfinalbfaa.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuserfinalbfaa.R
import com.example.githubuserfinalbfaa.model.UserModel
import kotlinx.android.synthetic.main.item_repo.view.*

class ListRepoAdapter: RecyclerView.Adapter<ListRepoAdapter.ListRepoViewHolder>() {

    private val mData = ArrayList<UserModel>()

    fun setData(items: ArrayList<UserModel>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListRepoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_repo, parent, false)
        return ListRepoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListRepoViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size


    class ListRepoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(userModel: UserModel) {
            with(itemView) {
                tv_project_name.text = userModel.repoName
            }
        }
    }
}