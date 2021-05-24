package com.example.mygraphql.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.mygraphql.PostQuery
import com.example.mygraphql.R
import com.example.mygraphql.databinding.FragmentPostDetailsBinding
import com.example.mygraphql.framework.PostDetailsViewModel
import com.example.mygraphql.view.state.ViewState
import kotlinx.android.synthetic.main.fragment_post_details.*
import kotlinx.android.synthetic.main.fragment_post_list.loadingView
import org.koin.android.viewmodel.ext.android.viewModel

class PostDetailsFragment : Fragment() {

    private val viewModel by viewModel<PostDetailsViewModel>()

    private var postId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState)
        val binding = DataBindingUtil.inflate<FragmentPostDetailsBinding>(
            inflater,
            R.layout.fragment_post_details,
            container,
            false
        )
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            postId = PostDetailsFragmentArgs.fromBundle(it).postId
        }

        postId.let {
            viewModel.getPostDetails(it)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.post.observe(viewLifecycleOwner, Observer {
                response ->
            when (response) {
                is ViewState.Loading -> {
                    loadingView.visibility = View.VISIBLE
                }
                is ViewState.Success -> {
                    loadingView.visibility = View.GONE
                    if (response.result.post?.id.isNullOrEmpty()) {
                        Toast.makeText(context, context?.getString(R.string.post_empty_message), Toast.LENGTH_SHORT).show()
                    } else {
                        val post = response.result.post
                        updatePost(post)
                    }
                }
                is ViewState.Error -> {
                    loadingView.visibility = View.GONE
                }
            }
        })
    }

    fun updatePost(post: PostQuery.Post?) {
        post_title_text.text = post?.title
        post_description_text.text = post?.body
        post_user_text.text = post?.user?.name
        post_user_name_text.text = post?.user?.username
    }
}