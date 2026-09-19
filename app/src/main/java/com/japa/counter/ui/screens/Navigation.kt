package com.japa.counter.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.japa.counter.ui.viewmodel.CounterViewModel

sealed class Screen(val route: String) {
    data object Counter : Screen("counter")
    data object DeityList : Screen("deity_list")
}

@Composable
fun AppNavigation(
    viewModel: CounterViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Counter.route
    ) {
        composable(Screen.Counter.route) {
            CounterScreen(
                viewModel = viewModel,
                onNavigateToDeityList = {
                    navController.navigate(Screen.DeityList.route)
                }
            )
        }
        composable(Screen.DeityList.route) {
            DeityListScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
