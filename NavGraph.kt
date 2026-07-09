package com.example.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.ProductDetailsScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RegisterScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.UploadProductScreen
import com.example.ui.viewmodel.AuthViewModel
import com.example.ui.viewmodel.FavoriteViewModel
import com.example.ui.viewmodel.ProductViewModel
import com.example.ui.viewmodel.SplashViewModel
import com.example.ui.viewmodel.UploadViewModel

@Composable
fun CampusCartNavGraph(
    viewModelFactory: ViewModelProvider.Factory,
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    // Instantiate viewmodels using our secure unified factory
    val splashViewModel: SplashViewModel = viewModel(factory = viewModelFactory)
    val authViewModel: AuthViewModel = viewModel(factory = viewModelFactory)
    val productViewModel: ProductViewModel = viewModel(factory = viewModelFactory)
    val uploadViewModel: UploadViewModel = viewModel(factory = viewModelFactory)
    val favoriteViewModel: FavoriteViewModel = viewModel(factory = viewModelFactory)

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,
        modifier = modifier.fillMaxSize()
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(
                viewModel = splashViewModel,
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = { navController.navigate(Routes.REGISTER) },
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateToLogin = { navController.navigate(Routes.LOGIN) },
                onRegisterSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                productViewModel = productViewModel,
                onNavigateToDetails = {
                    navController.navigate(Routes.DETAILS)
                },
                onNavigateToUpload = { navController.navigate(Routes.UPLOAD) },
                onNavigateToTab = { tabRoute ->
                    if (tabRoute != Routes.HOME) {
                        navController.navigate(tabRoute) {
                            popUpTo(Routes.HOME) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }

        composable(Routes.DETAILS) {
            ProductDetailsScreen(
                productViewModel = productViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Routes.UPLOAD) {
            UploadProductScreen(
                uploadViewModel = uploadViewModel,
                onNavigateBack = { navController.popBackStack() },
                onUploadSuccess = { navController.popBackStack() }
            )
        }

        composable(Routes.FAVORITES) {
            FavoritesScreen(
                favoriteViewModel = favoriteViewModel,
                productViewModel = productViewModel,
                onNavigateToDetails = {
                    navController.navigate(Routes.DETAILS)
                },
                onNavigateToTab = { tabRoute ->
                    if (tabRoute != Routes.FAVORITES) {
                        navController.navigate(tabRoute) {
                            popUpTo(Routes.HOME) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                authViewModel = authViewModel,
                productViewModel = productViewModel,
                onNavigateToDetails = {
                    navController.navigate(Routes.DETAILS)
                },
                onLogoutSuccess = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                },
                onNavigateToTab = { tabRoute ->
                    if (tabRoute != Routes.PROFILE) {
                        navController.navigate(tabRoute) {
                            popUpTo(Routes.HOME) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
