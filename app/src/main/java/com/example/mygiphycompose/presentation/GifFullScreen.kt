package com.example.mygiphycompose.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.mygiphycompose.domain.Gif
import kotlinx.coroutines.FlowPreview
import timber.log.Timber

@OptIn(ExperimentalFoundationApi::class, FlowPreview::class)
@Composable
fun GifFullScreen(
    gifs: List<Gif>,
    initialIndex: Int
) {
    val pagerState = rememberPagerState(
        initialPage = initialIndex,
        pageCount = {
            gifs.size
        })


    Box(
        modifier = Modifier,
        contentAlignment = Alignment.Center,
    ) {
        HorizontalPager(
            state = pagerState,
            key = { gifs[it] },
            modifier = Modifier.fillMaxSize(),
            pageSize = PageSize.Fill
        ) { index ->
            GifItem(
                gif = gifs[index],
                modifier = Modifier.fillMaxSize(),
                imageHeightMultiplier = 2,
                onItemMenuClick = {
                    Timber.d("Clicked on FULL SCREEN ${gifs[index].title}")
                }
            )
        }
    }
}
