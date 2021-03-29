package com.example.ausagi.model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    //VARIABLES-------------------------------------------------------
    //variables del perfil con backing property
    private val _fotoPerfil = MutableLiveData<Uri?>()
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

    }
    fun guardarPerfil(fotoID: Uri?, nombre: String,  comentario: String) {
        setFotoPerfil(fotoID)
        setNombrePerfil(nombre)
        setNivelPerfil(nivelTemp)
        setComentarioPerfil(comentario)
        setColorPerfil(colorTemp)
    }

}