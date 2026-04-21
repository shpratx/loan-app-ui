package com.tasheel.finance.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tasheel.finance.ui.application.ApplicationFormScreen
import com.tasheel.finance.ui.application.CardCollectionScreen
import com.tasheel.finance.ui.auth.LoginScreen
import com.tasheel.finance.ui.dashboard.DashboardScreen
import com.tasheel.finance.ui.offer.OfferScreen
import com.tasheel.finance.ui.products.ProductDetailScreen
import com.tasheel.finance.ui.products.ProductListScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(onLoginSuccess = {
                navController.navigate(Screen.ProductList.route) { popUpTo(Screen.Login.route) { inclusive = true } }
            })
        }
        composable(Screen.ProductList.route) {
            ProductListScreen(
                onProductClick = { navController.navigate(Screen.ProductDetail.createRoute(it)) },
                onDashboardClick = { navController.navigate(Screen.Dashboard.route) },
            )
        }
        composable(Screen.ProductDetail.route, arguments = listOf(navArgument("productId") { type = NavType.StringType })) {
            ProductDetailScreen(
                productId = it.arguments?.getString("productId") ?: "",
                onApply = { productId -> navController.navigate(Screen.ApplicationForm.createRoute(productId)) },
                onBack = { navController.popBackStack() },
            )
        }
        composable(Screen.ApplicationForm.route, arguments = listOf(navArgument("productId") { type = NavType.StringType })) {
            ApplicationFormScreen(
                productId = it.arguments?.getString("productId") ?: "",
                onApplicationCreated = { appId -> navController.navigate(Screen.CardCollection.createRoute(appId)) },
                onBack = { navController.popBackStack() },
            )
        }
        composable(Screen.CardCollection.route, arguments = listOf(navArgument("applicationId") { type = NavType.StringType })) {
            CardCollectionScreen(
                applicationId = it.arguments?.getString("applicationId") ?: "",
                onCardRegistered = { appId -> navController.navigate(Screen.Offer.createRoute(appId)) },
                onBack = { navController.popBackStack() },
            )
        }
        composable(Screen.Offer.route, arguments = listOf(navArgument("applicationId") { type = NavType.StringType })) {
            OfferScreen(
                applicationId = it.arguments?.getString("applicationId") ?: "",
                onComplete = { navController.navigate(Screen.Dashboard.route) { popUpTo(Screen.ProductList.route) } },
                onBack = { navController.popBackStack() },
            )
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(onBack = { navController.popBackStack() })
        }
    }
}
