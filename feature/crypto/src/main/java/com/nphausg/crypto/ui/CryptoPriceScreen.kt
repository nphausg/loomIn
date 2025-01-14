package com.nphausg.crypto.ui

import android.graphics.drawable.Icon
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.nphausg.app.foundation.ui.R
import com.nphausg.app.foundation.ui.utils.BackgroundPreview
import com.nphausg.crypto.domain.model.CryptoPrice
import com.nphausg.crypto.domain.vm.CryptoViewModel
import com.nphausg.loom.LoomState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoPriceScreen(viewModel: CryptoViewModel = hiltViewModel()) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Localized description"
                        )
                    }
                })
        },
        content = { paddingValues ->
            Crossfade(targetState = uiState) { state ->
                when (state) {
                    is LoomState.Error -> {
                        Text(text = "Error: ${state.throwable?.message ?: "Unknown error"}")
                    }

                    is LoomState.Loaded -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                                .animateContentSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                itemsIndexed(state.data.items) { index, item ->
                                    AnimatedVisibility(
                                        visible = true,
                                        enter = fadeIn() + expandVertically(),
                                        exit = fadeOut() + shrinkVertically()
                                    ) {
                                        CryptoPriceCard(item = item)
                                    }
                                }
                            }
                        }
                    }

                    is LoomState.Loading -> FullScreenLoading()
                    else -> Unit
                }
            }
        })
}

@Composable
private fun FullScreenLoading() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxSize(),
            text = "Loading..."
        )
    }
}

@Composable
private fun CryptoPriceCard(modifier: Modifier = Modifier, item: CryptoPrice) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp)
            .then(modifier),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(item.image)
                    .size(56)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img),
                contentScale = ContentScale.Fit,
                contentDescription = "",
                modifier = Modifier
                    .requiredSize(48.dp)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .aspectRatio(1f)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.symbol.uppercase(), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$${item.currentPrice}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = if (item.priceChangePercentage24h > 0)
                        "+${String.format("%.2f", item.priceChangePercentage24h)}%"
                    else
                        "${String.format("%.2f", item.priceChangePercentage24h)}%",
                    color = if (item.priceChangePercentage24h > 0) Color(0xFF4CAF50) else Color(
                        0xFFF44336
                    ),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
@BackgroundPreview
private fun CryptoPriceScreenPreview() {

}