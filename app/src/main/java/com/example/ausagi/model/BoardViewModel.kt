package com.example.ausagi.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ausagi.database.Picto

class BoardViewModel : ViewModel() {

    val listaPictosBarra = mutableListOf<Picto>()
    val posicion = MutableLiveData<Int>()
    val clicado = MutableLiveData<Int>()
    val atras = MutableLiveData<Int>()
    val atrasEditar = MutableLiveData<Int>()
    val clicadoConfig = MutableLiveData<Int>()
    val clicadoElim = MutableLiveData<Int>()
    val clicadoMover = MutableLiveData<Int>()
    val inCategory = MutableLiveData<Boolean>()

    //INICIALIZACIÓN--------------------------------------------------
    init {
        posicion.value = 0
        clicado.value = 0
        clicadoConfig.value = 0
        clicadoElim.value = 0
        clicadoMover.value = 0
        atras.value = 0
        atrasEditar.value = 0
        inCategory.value = false
    }

    //Funciones de manejo de pictos en la barra de accion
    fun addPicto(picto: Picto) { //añade pictos a la barra de accion
        listaPictosBarra.add(picto)
    }
    fun eliminarPictosBarra() { //elimina los pictos de la barra de accion
        listaPictosBarra.clear()
    }

}