package com.nphausg.loom.app.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nphausg.crypto.ui.CryptoPriceScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            TabNavigationBar(navController = navController)
        },
        content = { contentPadding ->
            // Navigation graph for the three tabs
            NavHost(
                navController = navController,
                startDestination = "explore",
                modifier = Modifier.fillMaxSize()) {
                composable("explore") { CryptoPriceScreen() }
                composable("me") { MeTabScreen() }
            }
        }
    )
}

@Composable
fun TabNavigationBar(navController: NavController) {
    val tabs = listOf("Explore", "Me")

    TabRow(
        selectedTabIndex = getSelectedTabIndex(navController),
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = getSelectedTabIndex(navController) == index,
                onClick = {
                    when (index) {
                        0 -> navController.navigate("explore")
                        1 -> navController.navigate("me")
                    }
                },
                text = { Text(text = title) }
            )
        }
    }
}

fun getSelectedTabIndex(navController: NavController): Int {
    return when (navController.currentDestination?.route) {
        "explore" -> 0
        "me" -> 1
        else -> 0
    }
}

@Composable
fun MeTabScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Me Tab", fontSize = 24.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MaterialTheme {
        MainScreen()
    }
}