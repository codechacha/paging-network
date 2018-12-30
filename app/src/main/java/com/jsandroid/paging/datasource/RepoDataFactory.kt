package com.jsandroid.paging.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.jsandroid.paging.api.GithubService
import com.jsandroid.paging.model.Repo

class RepoDataFactory(
    private val query: String,
    private val service: GithubService
) : DataSource.Factory<Int, Repo>() {

    val mutableLiveData: MutableLiveData<RepoDataSource> = MutableLiveData<RepoDataSource>()
    private var repoDataSource: RepoDataSource? = null

    override fun create(): DataSource<Int, Repo> {
        repoDataSource = RepoDataSource(query, service)
        mutableLiveData.postValue(repoDataSource)
        return repoDataSource!!
    }

}