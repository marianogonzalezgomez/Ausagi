package com.example.ausagi.model

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ausagi.R
import com.example.ausagi.database.Profile


class ProfileViewModel : ViewModel(){

    //VARIABLES-------------------------------------------------------
    val listaPerfiles = mutableListOf<Profile>()
    val posicion = MutableLiveData<Int>()

    var nivelTempVar: String = ""
    var colorTempVar: String = ""


    //INICIALIZACIÓN--------------------------------------------------
    init {
        posicion.value = 0
        guardarPerfil(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.kid1), "Mariano", "Este es un perfil de ejemplo") //ejemplo inicial
        listaPerfiles[posicion.value!!].level= "Nivel 1: Pictogramas"
        listaPerfiles[posicion.value!!].colour= "Azul"
    }


    //FUNCIONES-------------------------------------------------------
    fun setFoto(foto: Uri?){
        listaPerfiles[posicion.value!!].imageResource = foto
    }
    fun setNombre(nombre: String){
        listaPerfiles[posicion.value!!].name = nombre
    }
    fun setNivel(){
        listaPerfiles[posicion.value!!].level = nivelTempVar
    }
    fun setNivelTemp(niveltemp: String){
        nivelTempVar = niveltemp
    }
    fun setComentario(comentario: String){
        listaPerfiles[posicion.value!!].comment = comentario
    }
    fun setColor(){
        listaPerfiles[posicion.value!!].colour = colorTempVar
    }
    fun setColorTemp(colortemp: String){
        colorTempVar = colortemp
    }

    fun guardarPerfil(fotoID: Uri?, nombre: String, comentario: String) {
        listaPerfiles.add(Profile(fotoID, nombre, nivelTempVar, comentario, colorTempVar))
    }
    fun eliminarPerfil(){
        listaPerfiles.removeAt(posicion.value!!)
    }

}