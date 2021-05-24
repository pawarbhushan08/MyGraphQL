package com.example.mygraphql.helper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mygraphql.PostListQuery
import com.example.mygraphql.R
import com.example.mygraphql.presentation.PostListAction
import kotlinx.android.synthetic.main.item_post.view.*

class PostsListAdapter(var posts: ArrayList<PostListQuery.Data1?>, val actions: PostListAction) :
    RecyclerView.Adapter<PostsListAdapter.RecipeViewHolder>() {

    fun updatePosts(newPost: List<PostListQuery.Data1?>?) {
        posts.clear()
        newPost?.let { posts.addAll(it) }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RecipeViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_post, parent, false
        )
    )

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount() = posts.size

    inner class RecipeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val layout = view.postLayout
        private val recipeTitle = view.title
        private val recipeDescription = view.description
        fun bind(post: PostListQuery.Data1?) {
            recipeTitle.text = post?.title
            recipeDescription.text = post?.body
            layout.setOnClickListener { actions.onClick(post?.id) }
        }
    }
}