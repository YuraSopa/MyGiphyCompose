package com.example.mygiphycompose.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mygiphycompose.utils.shouldLoadMore
import kotlinx.coroutines.FlowPreview
import timber.log.Timber

@OptIn(FlowPreview::class)
@Composable
fun GifsListScreen(
    navController: NavController,
    viewModel: GifViewModel = hiltViewModel(),
    paginationCallback: () -> Unit
) {
    val gifs by remember { viewModel.gifsList }
    val isLoading by remember { viewModel.isLoading }
    val canLoadMore by remember { viewModel.canLoadMore }

    val scrollState = rememberLazyStaggeredGridState()
    val firstVisibleIndex = remember {
        mutableIntStateOf(scrollState.firstVisibleItemIndex)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            SearchBar(
                hint = "Search..",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                viewModel.emitQuery(it)
            }

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier.padding(horizontal = 8.dp),
                state = scrollState,
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 4.dp),
                content = {
                    items(gifs.size) { index ->
                        val gif = gifs[index]
                        GifItem(
                            gif = gif,
                            onClick = {
                                navController.navigate("gif_full_screen/${gif.id}")
                            },
                            onItemMenuClick = {
                                Timber.d("Clicked on ${gif.title}")
                            }
                        )

                    }

                    item {
                        if (canLoadMore) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .scale(0.2f)
                                    .align(CenterHorizontally)
                            )
                        }
                    }

                    if (scrollState.shouldLoadMore(firstVisibleIndex, 4)) {
                        paginationCallback()
                    }
                }
            )

        }

    }
}


@Composable
fun SearchBar(
    modifier: Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {}
) {
    var text by remember {
        mutableStateOf("")
    }
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

    Box(modifier = modifier) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it.trim()
                onSearch(text)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .onFocusChanged {
                    isHintDisplayed = !it.isFocused && text.isEmpty()
                }
        )
        if (isHintDisplayed) {
            Text(
                text = hint,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}


@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
) {
    Column {
        Text(text = error, color = Color.Red, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { onRetry() },
            modifier = Modifier.align(CenterHorizontally)
        ) {
            Text(text = "Retry")
        }
    }
}