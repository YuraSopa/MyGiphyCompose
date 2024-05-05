package com.example.mygiphycompose.utils

import android.os.Build
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder

class Constants {
    companion object {
        const val PAGE_SIZE = 20

       val decoder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoderDecoder.Factory()
            } else {
                GifDecoder.Factory()
            }
    }
}