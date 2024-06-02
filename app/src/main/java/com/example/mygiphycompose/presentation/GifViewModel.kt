package com.example.mygiphycompose.presentation

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper
import com.bumptech.glide.signature.ObjectKey
import com.example.mygiphycompose.data.repositoryImpl.GifRepositoryImpl
import com.example.mygiphycompose.domain.Gif
import com.example.mygiphycompose.utils.Constants.Companion.PAGE_SIZE
import com.example.mygiphycompose.utils.NetworkConnection
import com.example.mygiphycompose.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class GifViewModel @Inject constructor(
    private val repository: GifRepositoryImpl,
    private val networkConnection: NetworkConnection,
    @ApplicationContext val applicationContext: Context
) : ViewModel() {

    private val hasInternet: Boolean
        get() = networkConnection.isOnline()

    var gifsList = mutableStateOf<List<Gif>>(listOf())
    private var isLoading = mutableStateOf(false)
    private var loadError = mutableStateOf("")
    val canLoadMore = mutableStateOf(false)

    private var cachedGifsList = listOf<Gif>()
    private var isSearchingStarting = true
    private var isSearching = mutableStateOf(false)
    private var offset = 0
    private var currentQuery = ""
    private val queryFlow = MutableSharedFlow<String>(0, 0)


    init {
        internalRequest("")
        viewModelScope.launch {
            if (hasInternet) {
                repository.clearCachedGifs()
            }

            queryFlow.asSharedFlow().debounce(500)
                .collect {
                    searchQuery(it)
                }
        }

    }

    private fun searchQuery(it: String) {
        offset = 0
        canLoadMore.value = true
        isSearching.value = false
        isSearchingStarting = true
        gifsList.value = emptyList()
        currentQuery = it
        internalRequest(it)
    }

    fun fetchMore() {
        if (!isLoading.value && offset != -1) {
            internalRequest(currentQuery)
        }
    }

    fun emitQuery(query: String) {
        viewModelScope.launch {
            queryFlow.emit(query)
        }
    }


    private fun internalRequest(query: String) {

        viewModelScope.launch {
            try {
                isLoading.value = true

                val response = if (hasInternet) {

                    if (query.isNotEmpty()) {
                        repository.getSearchingGifs(query, PAGE_SIZE, offset)
                    } else {
                        repository.getTrendingGifs(PAGE_SIZE, offset)
                    }
                } else {
                    null
                }

                if (response != null) {
                    when (response) {
                        is Resource.Success -> {
                            if (response.data != null) {
                                if (response.data.size == PAGE_SIZE) {
                                    canLoadMore.value = true
                                    offset += PAGE_SIZE
                                } else {
                                    canLoadMore.value = false
                                    offset = -1
                                }

                                if (isSearchingStarting) {
                                    cachedGifsList = gifsList.value
                                    isSearchingStarting = false
                                }
                                gifsList.value += response.data
                                Timber.d("List: ${gifsList.value}")
                                isSearching.value = true
                            }
                        }

                        is Resource.Error -> {
                            loadError.value = response.error.toString()
                            isLoading.value = false
                        }

                        is Resource.Loading -> {}
                        is Resource.Empty -> {}
                    }
                } else {

                    val cachedList = repository.getCachedGifs().data?.let {
                        getCachedGifUrls(
                            applicationContext,
                            it
                        )
                    }

                    offset = -1
                    if (cachedList != null) {
                        if (query.isEmpty()) {
                            gifsList.value = cachedList
                            return@launch
                        } else {
                            val result = cachedList.filter {
                                it.title.contains(query.trim(), ignoreCase = true)
                            }
                            if (isSearchingStarting) {
                                cachedGifsList = gifsList.value
                                isSearchingStarting = true
                            }
                            gifsList.value = result
                            isSearching.value = true
                        }
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            isLoading.value = false
        }
    }

    private fun getCachedGifUrls(context: Context, gifList: List<Gif>): List<Gif> {
        val cacheDir = Glide.getPhotoCacheDir(context) ?: return emptyList()
        val diskCache = DiskLruCacheWrapper.create(cacheDir, 100 * 1024 * 1024) // 100MB cache size
        val cachedGifs = mutableListOf<Gif>()

        gifList.filter { gif ->
            try {
                val safeKey = ObjectKey(gif.image)
                val snapshot = diskCache.get(safeKey)
                if (snapshot != null) {
                    cachedGifs.add(gif)
                } else false
            }catch (t: Throwable){
                false
            }
        }
        return cachedGifs
    }
}