package com.nphausg.crypto.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.nphausg.loom.LoomState
import com.nphausg.loom.app.R
import com.nphausg.loom.app.domain.model.CryptoPrice
import com.nphausg.loom.app.domain.vm.MainUiState
import com.nphausg.loom.app.domain.vm.MainViewModel
import com.nphausg.loom.app.ui.theme.BackgroundPreview

@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MainContent(uiState)
}

@Composable
private fun MainContent(uiState: LoomState<MainUiState>) {

    val layoutDirection = LocalLayoutDirection.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = WindowInsets.safeDrawing.asPaddingValues()
                    .calculateStartPadding(layoutDirection),
                end = WindowInsets.safeDrawing.asPaddingValues()
                    .calculateEndPadding(layoutDirection),
            ),
        floatingActionButton = {
            FloatingActionButton(onClick = { }, shape = CircleShape) {
                Icon(Icons.Filled.Share, "")
            }
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
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable {

                }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.image,
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
            val reusableModifier = Modifier.fillMaxWidth()
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    modifier = reusableModifier,
                    text = item.symbol.uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    modifier = reusableModifier,
                    text = item.name,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }
            Column(
                modifier = Modifier.padding(start = 8.dp),
                horizontalAlignment = Alignment.End
            ) {
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
                    color = if (item.priceChangePercentage24h > 0)
                        Color(0xFF4CAF50)
                    else
                        Color(0xFFF44336),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
@BackgroundPreview
private fun MainContentPreview() {
    MainContent(
        uiState = LoomState.Loaded(
            MainUiState(
                listOf(
                    CryptoPrice(
                        id = "bitcoin",
                        name = "bitcoin",
                        symbol = "btc",
                        currentPrice = 94841.0,
                        priceChangePercentage24h = 0.52841,
                        image = "https://coin-images.coingecko.com/coins/images/1/large/bitcoin.png?1696501400"
                    )
                )
            )
        )
    )
}
