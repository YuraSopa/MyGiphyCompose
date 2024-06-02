package com.example.mygiphycompose.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.mygiphycompose.R
import com.example.mygiphycompose.domain.Gif
import timber.log.Timber

data class DropDownItem(
    val icon: Int,
    val text: String
)

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GifItem(
    gif: Gif,
    modifier: Modifier = Modifier,
    imageHeightMultiplier: Int = 1, //TODO:Try another lib Glide or Picasso
    onItemMenuClick: (DropDownItem) -> Unit,
    onClick: () -> Unit = {}
) {
    val itemHeight = rememberSaveable { gif.imageHeight }
    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }
    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }
    val interactionSource = remember {
        MutableInteractionSource()
    }

    val dropDownItems = listOf(
        DropDownItem(R.drawable.baseline_share_24, "Share"),
        DropDownItem(R.drawable.baseline_delete_24, "Delete"),
        DropDownItem(R.drawable.baseline_heart_24, "Like")
    )

    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .height(itemHeight.toInt().dp * imageHeightMultiplier)
            .indication(interactionSource, LocalIndication.current)
            .pointerInput(true) {
                detectTapGestures(
                    onLongPress = {
                        isContextMenuVisible = true
                        pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                        Timber.d("pressOffset: x= ${pressOffset.x}, y = ${pressOffset.y}")
                    },
                    onPress = {
                        val press = PressInteraction.Press(it)
                        interactionSource.emit(press)
                        tryAwaitRelease()
                        interactionSource.emit(PressInteraction.Release(press))
                    },
                    onTap = {
                        onClick()
                    }
                )

            }
    ) {

        GlideImage(
            model = gif.image,
            contentDescription = "gif #${gif.id}",
            modifier = modifier
                .fillMaxSize(),
            contentScale = ContentScale.FillBounds
        ){
            it.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        }


        DropdownMenu(
            expanded = isContextMenuVisible,
            onDismissRequest = { isContextMenuVisible = false },
            offset = pressOffset.copy(
                y = pressOffset.y - itemHeight.toInt().dp
            )

        ) {
            dropDownItems.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Row {
                            val imageVector = ImageVector.vectorResource(item.icon)
                            Image(imageVector = imageVector, contentDescription = item.text)
                            Text(text = item.text)
                        }
                    },
                    onClick = {
                        onItemMenuClick(item)
                        isContextMenuVisible = false
                    }
                )

            }
        }
    }
}