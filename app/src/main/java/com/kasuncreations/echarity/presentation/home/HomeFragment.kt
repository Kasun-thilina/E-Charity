package com.kasuncreations.echarity.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.kasuncreations.echarity.R
import com.kasuncreations.echarity.data.models.Post
import com.kasuncreations.echarity.utils.BaseFragment

class HomeFragment : BaseFragment() {


    companion object {
        const val TAG = "chat"
        fun newInstance() = HomeFragment()
    }

    private lateinit var postsAdapter: PostsAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var rvPosts: RecyclerView
    private lateinit var homeViewModel: HomeViewModel
    private var liveData: LiveData<DataSnapshot?>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, null)
        rvPosts = view.findViewById(R.id.rv_posts)
        loadData()
        init()

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showProgress()

    }

    private fun init() {
        val con: ArrayList<String>? = null
        con?.add("")
        con?.add("")
        postsAdapter = PostsAdapter(con)
        mLayoutManager = LinearLayoutManager(context!!)
        rvPosts.layoutManager = mLayoutManager
        rvPosts.adapter = postsAdapter
    }

    override fun onPause() {
        super.onPause()
        //hideProgress()
    }

    override fun onResume() {
        super.onResume()
        //  hideProgress()
    }

    private fun loadData() {
        showProgress()
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        liveData = homeViewModel.getDataSnapshotLiveData()
        liveData!!.observe(viewLifecycleOwner, Observer<DataSnapshot?> {
            println(it)
            //val posts = mutableListOf<Post>()
            it!!.children.map { post ->
                //posts.add(post.value as Post)
                val posts = post.getValue(Post::class.java)
                println(posts)
            }
            hideProgress()
        })
    }

}