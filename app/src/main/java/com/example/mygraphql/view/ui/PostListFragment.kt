package com.example.mygraphql.view.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygraphql.R
import com.example.mygraphql.databinding.FragmentPostListBinding
import com.example.mygraphql.framework.PostListViewModel
import com.example.mygraphql.helper.PostsListAdapter
import com.example.mygraphql.presentation.PostListAction
import com.example.mygraphql.view.state.ViewState
import kotlinx.android.synthetic.main.fragment_post_list.*
import org.koin.android.viewmodel.ext.android.viewModel

class PostListFragment: Fragment(), PostListAction {
    private val postListAdapter = PostsListAdapter(arrayListOf(), this)

    private val viewModel by viewModel<PostListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState)
        val binding = DataBindingUtil.inflate<FragmentPostListBinding>(
            inflater,
            R.layout.fragment_post_list,
            container,
            false
        )
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postsListView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = postListAdapter
        }
        viewModel.getPosts()

        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.postList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ViewState.Loading -> {
                    loadingView.visibility = View.VISIBLE
                    postList_empty_text.visibility = View.VISIBLE
                }
                is ViewState.Success -> {
                    loadingView.visibility = View.GONE
                    if (response.result.posts?.data?.isEmpty() == true) {
                        postListAdapter.updatePosts(emptyList())
                        Toast.makeText(context, context?.getString(R.string.post_empty_message), Toast.LENGTH_SHORT).show()
                        postList_empty_text.visibility = View.VISIBLE
                    } else {
                        val results = response.result.posts?.data
                        postListAdapter.updatePosts(results?.toList())
                        postList_empty_text.visibility = View.GONE
                    }
                }
                is ViewState.Error -> {
                    postListAdapter.updatePosts(emptyList())
                    postList_empty_text.visibility = View.VISIBLE
                    loadingView.visibility = View.GONE
                }
            }
        }
    }


    private fun goToRecipeDetails(id: String) {
        val action: NavDirections = PostListFragmentDirections.actionGoToPostDetails(id)
        Navigation.findNavController(postsListView).navigate(action)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onClick(id: String?) {
        goToRecipeDetails(id?:"")
    }
}