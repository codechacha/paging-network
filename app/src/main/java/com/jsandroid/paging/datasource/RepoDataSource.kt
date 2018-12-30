package com.jsandroid.paging.datasource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.jsandroid.paging.api.GithubService
import com.jsandroid.paging.api.searchRepos
import com.jsandroid.paging.model.Repo

class RepoDataSource(
    private val query: String,
    private val service: GithubService
) : PageKeyedDataSource<Int, Repo>() {

    companion object {
        const val TAG = "RepoDataSource"
    }
    val networkErrors: MutableLiveData<String> = MutableLiveData()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Repo>) {
        Log.i(TAG, "Initial Loading, count: ${params.requestedLoadSize}")
        val curPage = 1
        val nextPage = curPage + 1
        searchRepos(service, query, curPage, params.requestedLoadSize, { repos ->
            callback.onResult(repos, null, nextPage)
        }, { error ->
            networkErrors.postValue(error)
        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Repo>) {
        Log.i(TAG, "Loading key: ${params.key}, count: ${params.requestedLoadSize}")
        searchRepos(service, query, params.key, params.requestedLoadSize, { repos ->
            val nextKey = params.key + 1
            callback.onResult(repos, nextKey)
        }, { error ->
            networkErrors.postValue(error)
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Repo>) {
    }

}

