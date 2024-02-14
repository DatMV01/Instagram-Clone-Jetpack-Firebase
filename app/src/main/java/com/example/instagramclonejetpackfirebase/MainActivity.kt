package com.example.instagramclonejetpackfirebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.instagramclonejetpackfirebase.screen.SignupScreen
import com.example.instagramclonejetpackfirebase.ui.theme.InstagramCloneJetpackFirebaseTheme
import com.example.instagramclonejetpackfirebase.viewmodel.IgViewModel

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InstagramCloneJetpackFirebaseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    InstagramApp()
                }
            }
        }
    }
}


sealed class DestinationScreen(val route: String){
    object Signup: DestinationScreen("signup")
}

@Composable
fun InstagramApp() {
    val vm = hiltViewModel<IgViewModel>()
    val navController = rememberNavController();
    
    NavHost(navController = navController, startDestination =DestinationScreen.Signup.route , builder = {
        composable(DestinationScreen.Signup.route){
            SignupScreen(navController = navController, vm = vm)
        }
    })
    

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    InstagramCloneJetpackFirebaseTheme {
        InstagramApp()
    }
}