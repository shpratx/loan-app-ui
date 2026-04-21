package com.tasheel.finance.ui.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object ProductList : Screen("products")
    data object ProductDetail : Screen("products/{productId}") {
        fun createRoute(productId: String) = "products/$productId"
    }
    data object ApplicationForm : Screen("application/{productId}") {
        fun createRoute(productId: String) = "application/$productId"
    }
    data object CardCollection : Screen("card-collection/{applicationId}") {
        fun createRoute(applicationId: String) = "card-collection/$applicationId"
    }
    data object Offer : Screen("offer/{applicationId}") {
        fun createRoute(applicationId: String) = "offer/$applicationId"
    }
    data object Dashboard : Screen("dashboard")
}
