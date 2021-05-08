package com.example.ausagi.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ausagi.database.Picto

class BoardViewModel : ViewModel() {

    val listaPictosBarra = mutableListOf<Picto>()
    val posicion = MutableLiveData<Int>()
    val clicado = MutableLiveData<Int>()

    //INICIALIZACIÓN--------------------------------------------------
    init {
        posicion.value = 0
        clicado.value = 0
    }

    //Funciones de manejo de pictos en la barra de accion
    fun addPicto(picto: Picto) { //añade pictos a la barra de accion
        listaPictosBarra.add(picto)
    }
    fun eliminarPictosBarra() { //elimina los pictos de la barra de accion
        listaPictosBarra.clear()
        clicado.value = 0
    }
}