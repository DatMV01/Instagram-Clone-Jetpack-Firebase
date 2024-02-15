package com.example.instagramclonejetpackfirebase.navigation

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.instagramclonejetpackfirebase.screen.FeedScreen
import com.example.instagramclonejetpackfirebase.screen.LoginScreen
import com.example.instagramclonejetpackfirebase.screen.SignupScreen
import com.example.instagramclonejetpackfirebase.viewmodel.IgViewModel


@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    val vm = hiltViewModel<IgViewModel>()
    NavHost(
        navController = navController,
        startDestination = Screen.LoginScreen.route
    ) {
        composable(Screen.LoginScreen.route) {
            LoginScreen(navController = navController, vm = vm)
        }
        composable(Screen.Signup.route) {
            SignupScreen(navController = navController, vm = vm)
        }

        composable(Screen.FeedScreen.route) {
            FeedScreen(navController = navController, vm = vm)
        }
    }
}

sealed class Screen(val route: String) {
    object Signup : Screen("Signup")

    object LoginScreen : Screen("LoginScreen")

    object FeedScreen : Screen("FeedScreen")


}

data class NavParam(
    val name: String,
    val value: Parcelable
)


fun navigateTo(navController: NavController, dest: Screen, vararg params: NavParam) {
    for (param in params) {
        navController.currentBackStackEntry?.arguments?.putParcelable(param.name, param.value)
    }
    navController.navigate(dest.route) {
        popUpTo(dest.route)
        launchSingleTop = true
    }
}