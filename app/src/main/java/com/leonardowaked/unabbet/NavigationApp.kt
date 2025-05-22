package com.leonardowaked.unabbet

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationApp() {
    val myNavController = rememberNavController()

    NavHost(navController = myNavController, startDestination = "home") { // Puedes cambiar startDestination si tienes una pantalla de inicio de sesión
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

        composable("home") {
            HomeScreen(
                navController = myNavController,
                onClickAccount = {
                    myNavController.navigate("account")
                },
                onClickBetHistory = { // <-- ¡Añade esto para navegar al historial!
                    myNavController.navigate("bet_history")
                }
            )
        }
        composable("bet/{matchJson}") { backStackEntry ->
            val matchJson = backStackEntry.arguments?.getString("matchJson")
            BetScreen(navController = myNavController, matchJson = matchJson)
        }
        composable ( "account" ) {
            AccountScreen(onClickLogOut = {
                myNavController.navigate("login") {
                    popUpTo(0)
                }
            }, onClickBack = {
                myNavController.popBackStack() } )
        }
        composable("bet_history") { // <-- ¡NUEVA RUTA para el historial!
            BetHistoryScreen(navController = myNavController)
        }
        // Agrega otras rutas si las tienes (ej. login, register, etc.)
        // composable("login") { LoginScreen(navController = myNavController) }
        // composable("register") { RegisterScreen(navController = myNavController) }
    }
}