package com.example.ausagi.model

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ausagi.R
import com.example.ausagi.database.Picto

class BoardViewModel : ViewModel() {

    val listaPictos = mutableListOf<Picto>()
    val listaPictosBarra = mutableListOf<Picto>()
    val posicion = MutableLiveData<Int>()
    val clicado = MutableLiveData<Int>()

    //INICIALIZACIÓN--------------------------------------------------
    init {
        posicion.value = 0
        clicado.value = 0
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.kid1), "Yo")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.quiero), "Quiero")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.comer), "Comer")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.beber), "Beber")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.hacer_caca), "Hacer caca")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.hacer_pis), "Hacer pis")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.ir_al_colegio), "Ir al colegio")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.casa), "Casa")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.quiero), "Quiero")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.comer), "Comer")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.beber), "Beber")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.hacer_caca), "Hacer caca")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.hacer_pis), "Hacer pis")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.ir_al_colegio), "Ir al colegio")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.casa), "Casa")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.comer), "Comer")

    }

    fun guardarPicto(fotoID: Uri?, nombre: String) { //Solo añade pictos a lista pictos, es decir a la cuadricula
        listaPictos.add(Picto(fotoID, nombre))
    }

    fun addPicto(picto: Picto) { //esta añade pictos a la barra de accion
        listaPictosBarra.add(picto)
    }

    fun eliminarPictosBarra() {
        listaPictosBarra.clear()
        clicado.value = 0
    }
}