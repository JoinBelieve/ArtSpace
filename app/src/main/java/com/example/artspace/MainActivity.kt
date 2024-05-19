package com.example.artspace

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.example.artspace.ui.theme.ArtSpaceTheme

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArtSpaceTheme {
                ArtSpaceApp()
            }
        }
    }
}

@Composable
fun ArtSpaceApp() {
    var imageIndex by remember {
        mutableStateOf(0)
    }


    var currentImage = when (imageIndex) {
        0 -> R.drawable.bg1
        1 -> R.drawable.bg3
        2 -> R.drawable.bg2
        else -> R.drawable.bg3
    }

    val content = when (imageIndex) {
        0 -> "This is the first image."
        1 -> "This is the second image."
        2 -> "This is the third image."
        3 -> "This is the fourth image."
        else -> ""
    }

    val author = when (imageIndex) {
        0 -> "ShanQi1"
        1 -> "ShanQi2"
        2 -> "ShanQi3"
        3 -> "ShanQi4"
        else -> ""
    }

    val time = when (imageIndex) {
        0 -> "2024"
        1 -> "2025"
        2 -> "2026"
        3 -> "2027"
        else -> ""
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(
                bottom = 20.dp, top = 60.dp, start = 20.dp, end = 20.dp
            ),
    ) {
        Surface(
            shadowElevation = 2.dp, modifier = Modifier
                .width(350.dp)
                .height(430.dp)
                .fillMaxSize()
        ) {
            val options = BitmapFactory.Options().apply {
                // 仅获取图片的尺寸，而不加载整个图片到内存中
                inJustDecodeBounds = true
                BitmapFactory.decodeResource(LocalContext.current.resources, currentImage, this)
                // 计算压缩比例
                inSampleSize = calculateInSampleSize(this, 200, 200)
                // 解码整个图片
                inJustDecodeBounds = false
            }
            Image(
                bitmap = BitmapFactory.decodeResource(
                    LocalContext.current.resources,
                    currentImage,
                    options
                ).asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.padding(20.dp),
                contentScale = ContentScale.FillBounds
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
        Surface(
            border = BorderStroke(1.dp, Color.Transparent),
            shadowElevation = 2.dp,
            color = Color(0xFFDEEAF1)
        ) {
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(buildAnnotatedString {
                    append(content)
                    addStyle(
                        SpanStyle(
                            shadow = Shadow(color = Color.Black, blurRadius = 0.5f),
                            fontFamily = FontFamily.Serif,
                            fontSize = 32.sp,
                            fontStyle = FontStyle.Italic,
                        ), 0, this.length
                    )
                    addStyle(
                        ParagraphStyle(
                            lineHeight = 40.sp
                        ), 0, this.length
                    )
                })
                Spacer(modifier = Modifier.height(7.dp))
                Text(buildAnnotatedString {
                    append(author)
                    append(" ")
                    addStyle(
                        SpanStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            fontFamily = FontFamily.Monospace
                        ), 0, this.length
                    )
                    append(time)
                })
            }
        }

        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            OperationButton(text = "Previous") {
                if (imageIndex in 1..3) {
                    imageIndex--
                }
            }
            OperationButton(text = "Next") {
                if (imageIndex in 0..2) {
                    imageIndex++
                }
            }
        }
    }
}


fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    // 原始图片的宽度和高度
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1

    // 计算压缩比例
    if (height > reqHeight || width > reqWidth) {
        val halfHeight = height / 2
        val halfWidth = width / 2
        while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
            inSampleSize *= 2
        }
    }
    return inSampleSize
}


@Composable
fun OperationButton(
    text: String, modifier: Modifier = Modifier, onClick: () -> Unit
) {
    Button(onClick = onClick) {
        Text(text = text)
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ArtSpaceTheme {
        ArtSpaceApp()
    }
}