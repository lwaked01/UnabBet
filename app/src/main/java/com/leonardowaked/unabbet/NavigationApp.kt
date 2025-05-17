package com.leonardowaked.unabbet

import  androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

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
            HomeScreen()
        }
        composable ("bet") {
            BetScreen()
        }
    }

}