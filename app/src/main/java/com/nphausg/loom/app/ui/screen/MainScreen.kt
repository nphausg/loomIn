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
                startDestination = "main",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                composable("main") { MainTabScreen() }
                composable("explore") { TipCalcScreen() }
                composable("me") { MeTabScreen() }
            }
        }
    )
}

@Composable
fun TabNavigationBar(navController: NavController) {
    val tabs = listOf("Main", "Explore", "Me")

    TabRow(
        selectedTabIndex = getSelectedTabIndex(navController),
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = getSelectedTabIndex(navController) == index,
                onClick = {
                    when (index) {
                        0 -> navController.navigate("main")
                        1 -> navController.navigate("explore")
                        2 -> navController.navigate("me")
                    }
                },
                text = { Text(text = title) }
            )
        }
    }
}

fun getSelectedTabIndex(navController: NavController): Int {
    return when (navController.currentDestination?.route) {
        "main" -> 0
        "explore" -> 1
        "me" -> 2
        else -> 0
    }
}

@Composable
fun MainTabScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Main Tab", fontSize = 24.sp)
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