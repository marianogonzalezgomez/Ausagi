package com.example.ausagi.model

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ausagi.R
import com.example.ausagi.database.Picto
import com.example.ausagi.database.Profile


class ProfileViewModel : ViewModel() {

    //VARIABLES-------------------------------------------------------
    val listaPerfiles = mutableListOf<Profile>()
    val posicion = MutableLiveData<Int>()
    val posicionUltimoPerfil = MutableLiveData<Int>()

    var nivelTempVar: String = ""
    var colorTempVar: String = ""


    //INICIALIZACIÓN--------------------------------------------------
    init {
        posicion.value = 0
        posicionUltimoPerfil.value = 0
        guardarPerfil(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.kid1), "Mariano", "Este es un perfil de ejemplo") //ejemplo inicial
        listaPerfiles[posicion.value!!].level = "Nivel 1: Pictogramas"
        listaPerfiles[posicion.value!!].colour = "Azul"
    }

    //FUNCIONES-------------------------------------------------------
    //funciones para configurar el perfil
    fun setFoto(foto: Uri?) {
        listaPerfiles[posicion.value!!].imageResource = foto
    }
    fun setNombre(nombre: String) {
        listaPerfiles[posicion.value!!].name = nombre
    }
    fun setNivel() {
        if(nivelTempVar!="0"){
            listaPerfiles[posicion.value!!].level = nivelTempVar
        }
        nivelTempVar = "0" //Borrar resultado para el siguiente uso
    }
    fun setNivelTemp(niveltemp: String) {
        nivelTempVar = niveltemp
    }
    fun setComentario(comentario: String) {
        listaPerfiles[posicion.value!!].comment = comentario
    }
    fun setColor() {
        if(colorTempVar!="0"){
            listaPerfiles[posicion.value!!].colour = colorTempVar
        }
        colorTempVar = "0" //Borrar resultado para el siguiente uso
    }
    fun setColorTemp(colortemp: String) {
        colorTempVar = colortemp
    }
    private fun setDefaultTableroN1() { //lista de pictos por defecto de cada perfil
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.kid1), "Yo")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.quiero), "Quiero")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.comer), "Comer")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.beber), "Beber")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.hacer_caca), "Hacer caca")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.hacer_pis), "Hacer pis")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.ir_al_colegio), "Ir al colegio")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.casa), "Casa")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.kid2), "Amigo")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.kid3), "Amiga")
    }

    //Funciones para guardar y eliminar perfiles
    fun guardarPerfil(fotoID: Uri?, nombre: String, comentario: String) {
        listaPerfiles.add(Profile(fotoID, nombre, nivelTempVar, comentario, colorTempVar))
        nivelTempVar = "0" //Borrar resultado para el siguiente uso
        colorTempVar="0" //Borrar resultado para el siguiente uso
        setDefaultTableroN1()
    }
    fun eliminarPerfil() {
        listaPerfiles.removeAt(posicion.value!!)
    }

    //Funcion para guardar pictos dentro de la lista de cada perfil por defecto
    fun guardarPicto(fotoID: Uri?, nombre: String) {
        posicionUltimoPerfil.value = listaPerfiles.size - 1
        listaPerfiles[posicionUltimoPerfil.value!!].listaN1.add(Picto(fotoID, nombre))
    }

}