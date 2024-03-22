package com.example.kode

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kode.ui.screens.first.MainScreen
import com.example.kode.ui.screens.first.MainScreenViewModel
import com.example.kode.ui.screens.second.SecondScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun KODENavGraph(
    navController: NavHostController = rememberNavController(),
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: ROUTE.FIRST.name
    val currentItem = viewModel.currentItem.collectAsStateWithLifecycle()
    
    NavHost(navController = navController, startDestination = ROUTE.FIRST.name) {
        composable(ROUTE.FIRST.name) {
            MainScreen(
                onSecondScreenClicked = {
                    viewModel.setItem(it)
                    navController.navigate(ROUTE.SECOND.name)
                }
            )
        }
        composable(ROUTE.SECOND.name) {
            SecondScreen(backClick = { navController.navigate(ROUTE.FIRST.name)}, item = currentItem.value)
        }
    }
}

enum class ROUTE {
    FIRST,
    SECOND,
}