package com.example.ausagi.model

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ausagi.R
import com.example.ausagi.database.ListaPicto
import com.example.ausagi.database.Picto
import com.example.ausagi.database.Profile


class ProfileViewModel : ViewModel() {

    //VARIABLES-------------------------------------------------------
    val listaPerfiles = mutableListOf<Profile>()
    val posicion = MutableLiveData<Int>()
    val posicionUltimoPerfil = MutableLiveData<Int>()
    val posicionLista = MutableLiveData<Int>()

    var nivelTempVar: String = ""
    var colorTempVar: String = ""


    //INICIALIZACIÓN--------------------------------------------------
    init {
        posicion.value = 0
        posicionUltimoPerfil.value = 0
        posicionLista.value = 0
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
    private fun setDefaultTablero() { //lista de pictos por defecto de cada perfil
        listaPerfiles[posicionUltimoPerfil.value!!].listaN1.add(ListaPicto()) //Añadir lista de pictos inicial
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.kid1), "Yo")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.quiero), "Quiero")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.kid2), "Amigo")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.kid3), "Amiga")
        guardarCat(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.casa), "Casa") //Añadir categoría
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.comer), "Comer")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.beber), "Beber")
        guardarCat(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.hacer_caca), "Hacer caca") //Añadir categoría
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.hacer_pis), "Hacer pis")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.ir_al_colegio), "Ir al colegio")
    }

    //Funciones para guardar y eliminar perfiles
    fun guardarPerfil(fotoID: Uri?, nombre: String, comentario: String) {
        listaPerfiles.add(Profile(fotoID, nombre, nivelTempVar, comentario, colorTempVar))
        nivelTempVar = "0" //Borrar resultado para el siguiente uso
        colorTempVar="0" //Borrar resultado para el siguiente uso
        posicionUltimoPerfil.value = listaPerfiles.size - 1
        setDefaultTablero()
    }
    fun eliminarPerfil() {
        listaPerfiles.removeAt(posicion.value!!)
    }

    //Funciones para guardar pictos y categorías dentro de la lista de cada perfil ¡¡POR DEFECTO!!
    fun guardarPicto(fotoID: Uri?, nombre: String) {
        posicionUltimoPerfil.value = listaPerfiles.size - 1
        val pos: Int = listaPerfiles[posicionUltimoPerfil.value!!].listaN1.size - 1 //posición de la lista a la que se va a añadir el pictograma
        listaPerfiles[posicionUltimoPerfil.value!!].listaN1[pos].pictoList.add(Picto(fotoID, nombre, true, false, false))
    }
    fun guardarCat(fotoID: Uri?, nombre: String) {
        posicionUltimoPerfil.value = listaPerfiles.size - 1
        listaPerfiles[posicionUltimoPerfil.value!!].listaN1.add(ListaPicto()) //Añadir nueva categoría (que es una lista de pictos)
        val pos = 0 //El picto que protagoniza la categoría, se guarda en la lista inicial
        listaPerfiles[posicionUltimoPerfil.value!!].listaN1[pos].pictoList.add(Picto(fotoID, nombre, false, true, false, true)) //Guardar el pictograma de categoría en la lista inicial y advertir que es categoría
    }

}