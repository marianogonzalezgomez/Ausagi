package com.example.ausagi.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ausagi.database.Picto

class BoardViewModel : ViewModel() {

    val listaPictosBarra = mutableListOf<Picto>()
    private val posicion = MutableLiveData<Int>()
    val clicado = MutableLiveData<Int>()
    val atras = MutableLiveData<Int>()
    private val atrasEditar = MutableLiveData<Int>()
    val clicadoConfig = MutableLiveData<Int>()
    val clicadoElim = MutableLiveData<Int>()
    private val clicadoMover = MutableLiveData<Int>()
    private val inCategory = MutableLiveData<Boolean>()

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
    fun setPosicion(valor: Int) {
        posicion.value = valor
    }
    fun getPosicion(): Int {
        return posicion.value!!
    }
    fun setClicado(valor: Int) {
        clicado.value = valor
    }
    fun getClicado(): Int {
        return clicado.value!!
    }
    fun setAtras(valor: Int) {
        atras.value = valor
    }
    fun getAtras(): Int {
        return atras.value!!
    }
    fun setAtrasEditar(valor: Int) {
        atrasEditar.value = valor
    }
    fun getAtrasEditar(): Int {
        return atrasEditar.value!!
    }
    fun setClicadoConfig(valor: Int) {
        clicadoConfig.value = valor
    }
    fun getClicadoConfig(): Int {
        return clicadoConfig.value!!
    }
    fun setClicadoElim(valor: Int) {
        clicadoElim.value = valor
    }
    fun getClicadoElim(): Int {
        return clicadoElim.value!!
    }
    fun setClicadoMover(valor: Int) {
        clicadoMover.value = valor
    }
    fun getClicadoMover(): Int {
        return clicadoMover.value!!
    }
    fun setInCategory(valor: Boolean) {
        inCategory.value = valor
    }
    fun getInCategory(): Boolean {
        return inCategory.value!!
    }
}