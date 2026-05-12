package com.example.virasatnamma.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.virasatnamma.ui.screens.chat.ChatScreen
import com.example.virasatnamma.ui.screens.detail.DetailScreen
import com.example.virasatnamma.ui.screens.expense.ExpenseScreen   // ✅ ADDED
import com.example.virasatnamma.ui.screens.home.HomeScreen
import com.example.virasatnamma.ui.screens.passport.PassportScreen
import com.example.virasatnamma.ui.screens.scanner.ScannerScreen
import com.example.virasatnamma.ui.screens.splash.SplashScreen
import com.example.virasatnamma.viewmodel.HeritageViewModel

sealed class Screen(val route: String) {
    object Splash   : Screen("splash")
    object Home     : Screen("home")

    object Detail   : Screen("detail/{siteId}") {
        fun createRoute(siteId: String) = "detail/$siteId"
    }

    object Scanner  : Screen("scanner")
    object Passport : Screen("passport")

    object Chat     : Screen("chat/{siteId}") {
        fun createRoute(siteId: String) = "chat/$siteId"
    }

    // ✅ NEW: Expense screen
    object Expense  : Screen("expense/{siteId}") {
        fun createRoute(siteId: String) = "expense/$siteId"
    }
}

data class BottomNavItem(val label: String, val icon: ImageVector, val route: String)

val bottomNavItems = listOf(
    BottomNavItem("Home",    Icons.Default.Home,           Screen.Home.route),
    BottomNavItem("Scan",    Icons.Default.QrCodeScanner,  Screen.Scanner.route),
    BottomNavItem("Passport",Icons.Default.MenuBook,       Screen.Passport.route)
)

@Composable
fun VirasatNavGraph(viewModel: HeritageViewModel) {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = currentDestination?.route in listOf(
        Screen.Home.route,
        Screen.Scanner.route,
        Screen.Passport.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    bottomNavItems.forEach { item ->
                        val selected =
                            currentDestination?.hierarchy?.any { it.route == item.route } == true

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.secondary,
                                selectedTextColor = MaterialTheme.colorScheme.secondary,
                                unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                                unselectedTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {

            // Splash
            composable(Screen.Splash.route) {
                SplashScreen(onFinished = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                })
            }

            // Home
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onSiteClick = { siteId ->
                        navController.navigate(Screen.Detail.createRoute(siteId))
                    }
                )
            }

            // Detail (UPDATED)
            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("siteId") { type = NavType.StringType })
            ) { backStack ->

                val siteId = backStack.arguments?.getString("siteId") ?: return@composable

                DetailScreen(
                    siteId = siteId,
                    viewModel = viewModel,

                    onAskAI = {
                        navController.navigate(Screen.Chat.createRoute(siteId))
                    },

                    // ✅ NEW: Expense navigation
                    onOpenExpense = {
                        navController.navigate(Screen.Expense.createRoute(siteId))
                    },

                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            // Scanner
            composable(Screen.Scanner.route) {
                ScannerScreen(
                    onSiteFound = { siteId ->
                        navController.navigate(Screen.Detail.createRoute(siteId))
                    }
                )
            }

            // Passport
            composable(Screen.Passport.route) {
                PassportScreen(viewModel = viewModel)
            }

            // Chat
            composable(
                route = Screen.Chat.route,
                arguments = listOf(navArgument("siteId") { type = NavType.StringType })
            ) { backStack ->

                val siteId = backStack.arguments?.getString("siteId") ?: return@composable

                ChatScreen(
                    siteId = siteId,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            // ✅ NEW: Expense Screen
            composable(
                route = Screen.Expense.route,
                arguments = listOf(navArgument("siteId") { type = NavType.StringType })
            ) { backStack ->

                val siteId = backStack.arguments?.getString("siteId") ?: return@composable

                ExpenseScreen(
                    viewModel = viewModel,
                    siteId = siteId
                )
            }
        }
    }
}