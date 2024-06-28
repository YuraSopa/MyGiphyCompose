package com.example.mygiphycompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mygiphycompose.presentation.GifFullScreen
import com.example.mygiphycompose.presentation.GifViewModel
import com.example.mygiphycompose.presentation.GifsListScreen
import com.example.mygiphycompose.ui.theme.MyGiphyComposeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(FlowPreview::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyGiphyComposeTheme {
                val viewModel = hiltViewModel<GifViewModel>()
                val gifs by remember { viewModel.gifsList }

                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "gif_list_screen"
                ) {
                    composable("gif_list_screen") {
                        GifsListScreen(
                            viewModel = viewModel,
                            navController = navController,
                            paginationCallback = {
                                viewModel.fetchMore()
                            }
                        )
                    }
                    composable(
                        "gif_full_screen/{gifID}",
                        arguments = listOf(
                            navArgument("gifID") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val id = remember {
                            backStackEntry.arguments?.getString("gifID") ?: ""
                        }
                        val index = gifs.indexOfFirst {
                            it.id == id
                        }
                        GifFullScreen(viewModel= viewModel, initialIndex = index)
                    }
//                    composable("shareGif/{gifURL}") { backStackEntry ->
//                        val gifUrl = backStackEntry.arguments?.getString("gifURL") ?: ""
//                        viewModel.shareGif(navController.context, gifUrl)
//                    }
                }
            }
        }
    }
}