package com.leonardowaked.unabbet

import android.util.Patterns
import java.io.PipedReader

//returnar un true si es valido y un false si noes valido
//tambien retorne una cadena que diga que paso si no es valido


fun validateEmail (email:String): Pair<Boolean,String>{
    return  when{
        email.isEmpty()-> Pair(false, "El correo es obligatorio")
        !Patterns.EMAIL_ADDRESS.matcher(email).matches()-> Pair(false, "El correo es invalido")
        !email.endsWith("@unab.edu.co") -> Pair(false, "El correo debe ser de la unab")

        else -> Pair(true, "")
    }
}
fun validatePassword (password:String): Pair<Boolean,String>{
    return  when{
        password.isEmpty()-> Pair(false, "La contraseña es obligatorio")
        password.length< 6 -> Pair(false, "La contrasea debe tener al menos 6 caracteres")
        !password.any{ it.isDigit() } -> Pair(false, "La contrasea debe tener al menos un numero ")

        else -> Pair(true, "")
    }
}
fun validateName (name:String): Pair<Boolean,String>{
    return  when{
        name.isEmpty() -> Pair(false, "EL nombre es requerido")
        name.length < 3 -> Pair(false, "El nombre debe tener al menos 3 caracteres")

        else -> Pair(true, "")
    }
}

fun validateConfirmPassword (password: String, confirmPassword: String): Pair <Boolean, String>{
    return when {
        confirmPassword.isEmpty() -> Pair(false, "La contraseña es requerida")
        confirmPassword != password -> Pair(false, "Las contraseñas no coinciden")
        else -> Pair(true, "")
    }
}