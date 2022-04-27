package com.nunop.rickandmorty.ui.location.locationDetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nunop.rickandmorty.ui.theme.ErrorView
import com.nunop.rickandmorty.ui.theme.LoadingView
import com.nunop.rickandmorty.ui.theme.TextWithSpacer

@Composable
fun LocationDetailsScreen(locationDetailsViewModel: LocationDetailsViewModel = hiltViewModel()) {

    //TODO: remove logic from UI
    val state = locationDetailsViewModel.state
    if (state.error == null) {
        if (state.isLoading) {
            LoadingView()
        } else {
            state.location?.let {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    TextWithSpacer(it.name)
                    TextWithSpacer(it.type)
                    TextWithSpacer(it.created)
                    TextWithSpacer(it.dimension)
                }
            }
        }
    } else {
        ErrorView()
    }
}

