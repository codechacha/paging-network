/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jsandroid.paging.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.jsandroid.paging.model.RepoSearchResult
import com.jsandroid.paging.api.GithubService
import com.jsandroid.paging.api.searchRepos
import com.jsandroid.paging.datasource.RepoDataFactory
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Repository class that works with local and remote data sources.
 */
class GithubRepository(context: Context) {

    private val service: GithubService = GithubService.create()
    private val executor: Executor = Executors.newFixedThreadPool(5)

    /**
     * Search repositories whose names match the query.
     */
    fun search(query: String): RepoSearchResult {
        Log.d("GithubRepository", "New query: $query")


        val dataSourceFactory = RepoDataFactory(query, service)

        val pagedListConfig = PagedList.Config.Builder()
            .setPageSize(50)
            .setInitialLoadSizeHint(50) // default: page size * 3
            .setPrefetchDistance(10) // default: page size
            .setEnablePlaceholders(false) // default: true
            .build()

        val data = LivePagedListBuilder(dataSourceFactory, pagedListConfig)
            .setFetchExecutor(executor)
            .build()

        val networkErrors = Transformations.switchMap(dataSourceFactory.mutableLiveData,
                { dataSource -> dataSource.networkErrors })

        return RepoSearchResult(data, networkErrors)

    }

}