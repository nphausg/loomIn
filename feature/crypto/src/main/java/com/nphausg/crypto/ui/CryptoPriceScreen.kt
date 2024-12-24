package com.nphausg.crypto.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Card
import com.nphausg.crypto.domain.model.CryptoPrice
import com.nphausg.crypto.domain.vm.CryptoViewModel

@Composable
fun CryptoPriceScreen(viewModel: CryptoViewModel) {
    val cryptoPrices = viewModel.cryptoPrices.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Cryptocurrency Prices", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        cryptoPrices.forEach { price ->
            CryptoPriceCard(price = price)
        }
    }
}

@Composable
fun CryptoPriceCard(price: CryptoPrice) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = price.name, fontSize = 20.sp)
            Text(text = "${price.symbol} Price: \$${price.currentPrice}", fontSize = 16.sp)
        }
    }
}

@Preview
@Composable
private fun CryptoPriceScreenPreview() {

}