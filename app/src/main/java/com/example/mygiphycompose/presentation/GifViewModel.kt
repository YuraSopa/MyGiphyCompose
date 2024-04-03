package com.example.mygiphycompose.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygiphycompose.data.repositoryImpl.GifRepositoryImpl
import com.example.mygiphycompose.domain.Gif
import com.example.mygiphycompose.utils.Constants.Companion.PAGE_SIZE
import com.example.mygiphycompose.utils.NetworkConnection
import com.example.mygiphycompose.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GifViewModel @Inject constructor(
    private val repository: GifRepositoryImpl,
    private val networkConnection: NetworkConnection
) : ViewModel() {

    private val hasInternet: Boolean
        get() = networkConnection.isOnline()

    var gifsList = mutableStateOf<List<Gif>>(listOf())
    var isLoading = mutableStateOf(false)
    var loadError = mutableStateOf("")
    val canLoadMore = mutableStateOf(false)

    private var cachedGifsList = listOf<Gif>()
    private var isSearchingStarting = true
    var isSearching = mutableStateOf(false)
    private var offset = 0

    init {
        loadGifs()
    }

    fun fetchMore() {
        if (!isLoading.value && offset != -1) {
            loadGifs()
        }
    }

    private fun loadGifs() {

        viewModelScope.launch {
            try {
                isLoading.value = true

                val response = if (hasInternet) {
                    repository.getTrendingGifs(PAGE_SIZE, offset)
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
                                gifsList.value += response.data
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
                    val cachedList = repository.getCachedGifs().data
                    offset = -1
                    if (cachedList != null) {
                        gifsList.value += cachedList
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            isLoading.value = false
        }
    }

    fun searchQuery(query: String) {
        val listSearch = if (isSearchingStarting) {
            gifsList.value
        } else {
            cachedGifsList
        }

        viewModelScope.launch {
            try {
                if (query.isEmpty()){
                    gifsList.value = cachedGifsList
                    isSearching.value = false
                    isSearchingStarting = true
                    offset = 0
                    return@launch
                }
                isLoading.value = true

                val response = if (hasInternet) {
                    repository.getSearchingGifs(query, PAGE_SIZE, offset)
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

                                if (isSearchingStarting){
                                    cachedGifsList = gifsList.value
                                    isSearchingStarting = false
                                }
                                gifsList.value = response.data
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
                    val cachedList = repository.getCachedGifs().data
                    offset = -1
                    if (cachedList != null) {
                        gifsList.value += cachedList
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            isLoading.value = false
        }
    }
}