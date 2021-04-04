package com.example.ausagi.model

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ausagi.R
import com.example.ausagi.database.Profile


class ProfileViewModel : ViewModel() {

    //VARIABLES-------------------------------------------------------
    val listaPerfiles = mutableListOf<Profile>()
    val posicion = MutableLiveData<Int>()

    var nivelTempVar: String = ""
    var colorTempVar: String = ""

    init {
        posicion.value = 0
        guardarPerfil(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.kid1), "Mariano", "Este es un perfil de ejemplo") //ejemplo inicial
        listaPerfiles[posicion.value!!].level= "Nivel 1: Pictogramas"
        listaPerfiles[posicion.value!!].colour= "Azul"
    }
    /*private val _fotoPerfil = MutableLiveData<Uri?>()
    val fotoPerfil: LiveData<Uri?> = _fotoPerfil
    private val _nombrePerfil = MutableLiveData<String>()
    val nombrePerfil: LiveData<String> = _nombrePerfil
    private val _nivelPerfil = MutableLiveData<String>()
    val nivelPerfil: LiveData<String> = _nivelPerfil
    private val _comentarioPerfil = MutableLiveData<String>()
    val comentarioPerfil: LiveData<String> = _comentarioPerfil
    private val _colorPerfil = MutableLiveData<String>()
    val colorPerfil: LiveData<String> = _colorPerfil
    //Variables temporales
    var nivelTemp: String = ""
    var colorTemp: String = ""

    //FUNCIONES-------------------------------------------------------
    //funciones de datos del perfil
   fun setFotoPerfil(fotoID: Uri?) {
        if (fotoID!=null){
            _fotoPerfil.value= fotoID
        }
    }
    fun setNombrePerfil(nombre: String) {
        _nombrePerfil.value = nombre
    }
    fun setNivelPerfil(nivel: String) {
        _nivelPerfil.value = nivelTemp
    }
    fun setComentarioPerfil(comentario: String) {
        _comentarioPerfil.value = comentario
    }
    fun setColorPerfil(color: String) {
        _colorPerfil.value = colorTemp
    }
    fun setNivelPerfilTemp(nivelTempOut: String) {
        nivelTemp = nivelTempOut
    }
    fun setColorPerfilTemp(colorTempOut: String) {
        colorTemp = colorTempOut

    }*/
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

        /*setFotoPerfil(fotoID)
        setNombrePerfil(nombre)
        setNivelPerfil(nivelTemp)
        setComentarioPerfil(comentario)
        setColorPerfil(colorTemp)*/
    }
    fun eliminarPerfil(){
        listaPerfiles.removeAt(posicion.value!!)
    }

}