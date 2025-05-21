// NavigationApp.kt
package com.leonardowaked.unabbet

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.gson.Gson // Importar Gson
import com.leonardowaked.unabbet.data.models.MatchResponse // Importar MatchResponse

@Composable
fun NavigationApp(){
    val myNavController = rememberNavController()
    var myStartDestination: String = "Login"

    val auth = Firebase.auth
    val currentUser = auth.currentUser

    if (currentUser != null){
        myStartDestination = "home"
    }else {
        myStartDestination = "login"
    }

    NavHost(
        navController = myNavController,
        startDestination =  myStartDestination,
    ){
        composable ("login") {
            LoginScreen(onClickRegister = {
                myNavController.navigate("register")
            },  onSuccessfulLogin = {
                myNavController.navigate("home"){
                    popUpTo("login"){inclusive=true}
                }
            })
        }
        composable ("register") {
            RegisterScreen(onClickBack = {
                myNavController.popBackStack()
            }, onSuccessfulRegister = {
                myNavController.navigate("login"){
                    popUpTo(0)
                }
            })
        }
        composable ("home") {
            // Pasamos el NavController a HomeScreen
            HomeScreen(navController = myNavController)
        }
        composable (
            route = "bet/{matchJson}", // La ruta ahora incluye el argumento {matchJson}
            arguments = listOf(navArgument("matchJson") { type = NavType.StringType }) // Declarar el argumento como String
        ) { backStackEntry ->
            // Extraer el JSON del argumento
            val matchJson = backStackEntry.arguments?.getString("matchJson")
            val matchResponse = Gson().fromJson(matchJson, MatchResponse::class.java) // Convertir JSON a objeto MatchResponse

            // Pasar el objeto MatchResponse a BetScreen
            if (matchResponse != null) {
                BetScreen(navController = myNavController, match = matchResponse)
            } else {
                myNavController.popBackStack()
            }
        }
    }
}