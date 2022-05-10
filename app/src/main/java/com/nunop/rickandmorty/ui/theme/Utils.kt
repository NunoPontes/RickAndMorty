package com.nunop.rickandmorty.ui.theme

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nunop.rickandmorty.R

@Composable
fun ErrorView() {
    //Error
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.generic_error))
    val progress by animateLottieCompositionAsState(composition)
    LottieAnimation(
        composition,
        progress,
    )
}

@Composable
fun LoadingView() {
    //Loading
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.morty_loading))
    val progress by animateLottieCompositionAsState(composition)
    LottieAnimation(
        composition,
        progress,
    )
}

@Composable
fun TextWithSpacer(it1: String?) {
    it1?.let {
        Text(
            text = it,
            color = MaterialTheme.colors.secondaryVariant,
            style = MaterialTheme.typography.subtitle2
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
}