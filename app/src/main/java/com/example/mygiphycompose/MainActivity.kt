package com.example.mygiphycompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mygiphycompose.presentation.GifViewModel
import com.example.mygiphycompose.presentation.GifsListScreen
import com.example.mygiphycompose.ui.theme.MyGiphyComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyGiphyComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel = hiltViewModel<GifViewModel>()
                    val gifs by remember { viewModel.gifsList }

                    GifsListScreen(
                        gifs = gifs,
                        paginationCallback = {
                            viewModel.fetchMore()
                        }
                    )
                }
            }
        }
    }
}