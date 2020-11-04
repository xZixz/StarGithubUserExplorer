package com.cardes.stargithubuserexplorer.components.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cardes.stargithubuserexplorer.R
import com.cardes.stargithubuserexplorer.domain.model.GithubUser
import kotlinx.android.synthetic.main.item_github_user.view.*

class GithubUsersAdapter constructor(
    private val context: Context, private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<GithubUsersAdapter.GithubUserViewHolder>() {

    private val githubUsers = mutableListOf<GithubUser>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubUserViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_github_user, parent, false)
        return GithubUserViewHolder(view)
    }

    override fun getItemCount(): Int = githubUsers.size

    override fun onBindViewHolder(holder: GithubUserViewHolder, position: Int) {
        val githubUser = githubUsers[position]
        holder.run {
            Glide.with(context)
                .load(githubUser.avatarUrl)
                .into(ivAvatar)
            tvGithubLoginName.text = githubUser.loginName
            container.setOnClickListener {
               itemClickListener.onItemClick(githubUser.loginName)
            }
        }
    }

    fun setData(githubUsers: List<GithubUser>) {
        this.githubUsers.clear()
        this.githubUsers.addAll(githubUsers)
        notifyDataSetChanged()
    }

    class GithubUserViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {
        val ivAvatar: ImageView = rootView.iv_avatar
        val tvGithubLoginName: TextView = rootView.tv_github_login_name
        val container: View = rootView.cv_user_search_container
    }

    interface OnItemClickListener {
        fun onItemClick(loginName: String)
    }
}
