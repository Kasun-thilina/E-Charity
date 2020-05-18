package com.kasuncreations.echarity.presentation.home

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.kasuncreations.echarity.R
import com.kasuncreations.echarity.data.models.Post
import com.kasuncreations.echarity.data.models.Vote
import com.kasuncreations.echarity.presentation.auth.Listner
import com.kasuncreations.echarity.presentation.post.PostViewModel
import com.kasuncreations.echarity.presentation.post.PostViewModelFactory
import com.kasuncreations.echarity.utils.BaseFragment
import com.kasuncreations.echarity.utils.CONSTANTS
import com.kasuncreations.echarity.utils.showToastLong
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class HomeFragment : BaseFragment(), KodeinAware, Listner {


    companion object {
        const val TAG = "chat"
        fun newInstance() = HomeFragment()
    }

    override val kodein by lazy { (context as KodeinAware).kodein }
    private val factory: PostViewModelFactory by instance()
    private lateinit var viewModel: PostViewModel
    private val sharedPreferences: SharedPreferences by instance(arg = CONSTANTS.PREF_NAME)

    private lateinit var postsAdapter: PostsAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var rvPosts: RecyclerView
    private lateinit var homeViewModel: HomeViewModel
    private var liveData: LiveData<DataSnapshot?>? = null

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, null)
        rvPosts = view.findViewById(R.id.rv_posts)
        viewModel = ViewModelProviders.of(this, factory).get(PostViewModel::class.java)
        viewModel.listner = this
        initView()
        loadData()
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showProgress()

    }
    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        showProgress()
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        liveData = homeViewModel.getDataSnapshotLiveData()
        var voteList: MutableList<Vote>? = null
        liveData!!.observe(viewLifecycleOwner, Observer {
            postsAdapter.postList.clear()
            it!!.children.map { post ->
                postsAdapter.postList.add(post.getValue(Post::class.java)!!)
            }
            postsAdapter.postList.reverse()
            postsAdapter.notifyDataSetChanged()
            hideProgress()
            liveData!!.removeObservers(viewLifecycleOwner)
        })
    }

    private fun initView() {
        postsAdapter = PostsAdapter(
            context!!,
            firebaseAuth.currentUser!!.uid,
            mutableListOf()
        ) { count, ID, vote ->
            updateVote(count, ID, vote)
        }
        mLayoutManager = LinearLayoutManager(context!!)
        rvPosts.layoutManager = mLayoutManager
        rvPosts.adapter = postsAdapter


    }

    private fun updateVote(count: Int, ID: Long, vote: Vote) {
        println("count: $count,$ID")
        viewModel.updatePost(count, ID, vote)
    }

    override fun onStarted() {

    }

    override fun onSuccess() {

    }

    override fun onError(msg: String) {
        context.showToastLong("Vote Casting Failed:$msg")
    }


}