package com.example.ausagi.model

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ausagi.R
import com.example.ausagi.database.ListaPicto
import com.example.ausagi.database.Picto
import com.example.ausagi.database.Profile


class ProfileViewModel : ViewModel() {

    //VARIABLES-------------------------------------------------------
    val listaPerfiles = mutableListOf<Profile>() //lista de los perfiles creados
    val posicion = MutableLiveData<Int>() //posicion del perfil seleccionado en la lista de perfiles
    val posicionUltimoPerfil = MutableLiveData<Int>() //posicion del ultimo perfil de la lista de perfiles
    val posicionLista = MutableLiveData<Int>() //posicion de la lista correspondiente dentro de la listaN1

    var nivelBotonConfig: String = ""
    var nivelTempVar: String = ""
    var colorTempVar: String = ""
    var tipoTempVar: Int = 0 //0 picto, 1 categoría y 2 rutina

    var numCat: Int = 0

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

    //Funciones para guardar y eliminar perfiles
    fun guardarPerfil(fotoID: Uri?, nombre: String, comentario: String) {
        listaPerfiles.add(Profile(fotoID, nombre, nivelTempVar, comentario, colorTempVar))
        nivelTempVar = "0" //Borrar resultado para el siguiente uso
        colorTempVar= "0" //Borrar resultado para el siguiente uso
        posicionUltimoPerfil.value = listaPerfiles.size - 1
        setDefaultTablero()
    }
    fun eliminarPerfil() {
        listaPerfiles.removeAt(posicion.value!!)
    }

    //Funciones para guardar pictos, categorías y rutinas dentro de la lista de cada perfil ¡¡POR DEFECTO!!
    fun guardarPicto(fotoID: Uri?, nombre: String, nivel: Int) {
        posicionUltimoPerfil.value = listaPerfiles.size - 1
        numCat = listaPerfiles[posicionUltimoPerfil.value!!].listaN1.size - 1 //posición de la lista a la que se va a añadir el pictograma
        when (nivel) {
            1 -> listaPerfiles[posicionUltimoPerfil.value!!].listaN1[numCat].pictoList.add(Picto(fotoID, nombre, true, false, false, false, false, numCat))
            2 -> listaPerfiles[posicionUltimoPerfil.value!!].listaN1[numCat].pictoList.add(Picto(fotoID, nombre, false, true, true, false, false, numCat))
            3 -> listaPerfiles[posicionUltimoPerfil.value!!].listaN1[numCat].pictoList.add(Picto(fotoID, nombre, false, true, true, false, false, numCat))
        }
    }
    fun guardarCat(fotoID: Uri?, nombre: String) {
        posicionUltimoPerfil.value = listaPerfiles.size - 1
        listaPerfiles[posicionUltimoPerfil.value!!].listaN1.add(ListaPicto()) //Añadir nueva categoría (que es una lista de pictos)
        numCat =listaPerfiles[posicionUltimoPerfil.value!!].listaN1.size - 1 //Posicion de la categoría que se acaba de añadir
        val pos = 0 //El picto que protagoniza la categoría, se guarda en la lista inicial
        listaPerfiles[posicionUltimoPerfil.value!!].listaN1[pos].pictoList.add(Picto(fotoID, nombre, false, true, false, true, false, numCat)) //Guardar el pictograma de categoría en la lista inicial y advertir que es categoría
    }
    fun guardarRut(fotoID: Uri?, nombre: String) {
        posicionUltimoPerfil.value = listaPerfiles.size - 1
        listaPerfiles[posicionUltimoPerfil.value!!].listaN1.add(ListaPicto()) //Añadir nueva rutina (que es una lista de pictos)
        numCat =listaPerfiles[posicionUltimoPerfil.value!!].listaN1.size - 1 //Posicion de la categoría que se acaba de añadir
        val pos = 0 //El picto que protagoniza la rutina, se guarda en la lista inicial
        listaPerfiles[posicionUltimoPerfil.value!!].listaN1[pos].pictoList.add(Picto(fotoID, nombre, false, false, true, false, true, numCat)) //Guardar el pictograma de rutina en la lista inicial y advertir que es rutina
    }

    //Funciones para guardar pictos, categorías y rutinas dentro de la lista de cada perfil de forma ¡¡PERSONALIZADA!!
    fun guardarPictoPerson(fotoID: Uri?, nombre: String, nivel: Int, posicionPerfil: Int, categoria: Int) {
        val aux1 = listaPerfiles[posicionPerfil].listaN1[0].pictoList.filter{ it.level1 }.size
        val aux2 = listaPerfiles[posicionPerfil].listaN1[0].pictoList.filter{ it.level2 && !it.isCategory }.size
        val aux3 = listaPerfiles[posicionPerfil].listaN1[0].pictoList.filter{ (it.level2 || it.level3) && !it.isCategory && !it.isRoutine }.size
        if (categoria == 0){
                when (nivel) {
                    1 -> listaPerfiles[posicionPerfil].listaN1[categoria].pictoList.add(aux1, Picto(fotoID, nombre, true, false, false, false, false, categoria))
                    2 -> listaPerfiles[posicionPerfil].listaN1[categoria].pictoList.add(aux1+aux2, Picto(fotoID, nombre, false, true, true, false, false, categoria))
                    3 -> listaPerfiles[posicionPerfil].listaN1[categoria].pictoList.add(aux1+aux3, Picto(fotoID, nombre, false, true, true, false, false, categoria))
                }
            }
        else {
            when (nivel) {
                1 -> listaPerfiles[posicionPerfil].listaN1[categoria].pictoList.add(Picto(fotoID, nombre, true, false, false, false, false, categoria))
                2 -> listaPerfiles[posicionPerfil].listaN1[categoria].pictoList.add(Picto(fotoID, nombre, false, true, true, false, false, categoria))
                3 -> listaPerfiles[posicionPerfil].listaN1[categoria].pictoList.add(Picto(fotoID, nombre, false, true, true, false, false, categoria))
            }
        }
    }
    fun guardarCatPerson(fotoID: Uri?, nombre: String, nivel: Int, posicionPerfil: Int) {
        //Se guarda la categoría como una nueva lista
        numCat = listaPerfiles[posicionPerfil].listaN1[0].pictoList.filter{ it.isCategory && it.level2 }.size //tamaño de la lista de categorías del nivel 2
        val aux1 = listaPerfiles[posicionPerfil].listaN1[0].pictoList.filter{ it.level1 }.size
        val aux2 = listaPerfiles[posicionPerfil].listaN1[0].pictoList.filter{ it.level2 }.size
        val aux3 = listaPerfiles[posicionPerfil].listaN1[0].pictoList.filter{ (it.level2 || it.level3) && !it.isRoutine }.size
        //Se guarda el pictograma dentro de la lista principal
        when (nivel) {
            2 -> listaPerfiles[posicionPerfil].listaN1[0].pictoList.add(aux1+aux2, Picto(fotoID, nombre, false, true, false, true, false, numCat+1))
            3 -> listaPerfiles[posicionPerfil].listaN1[0].pictoList.add(aux1+aux3, Picto(fotoID, nombre, false, false, true, true, false, numCat+1))
        }
        listaPerfiles[posicionPerfil].listaN1.add(numCat+1, ListaPicto()) //Añadir nueva categoría (que es una lista de pictos)
    }
    fun guardarRutPerson(fotoID: Uri?, nombre: String, nivel: Int, posicionPerfil: Int) {
        //Se guarda la categoría como una nueva lista
        numCat = listaPerfiles[posicionPerfil].listaN1[0].pictoList.filter{ it.isRoutine || it.isCategory }.size //tamaño de la lista de rutinas y categorías
        //Se guarda el pictograma dentro de la lista principal
        when (nivel) {
            3 -> listaPerfiles[posicionPerfil].listaN1[0].pictoList.add(Picto(fotoID, nombre, false, false, true, false, true, numCat+1))
        }
        listaPerfiles[posicionPerfil].listaN1.add(numCat+1, ListaPicto()) //Añadir nueva categoría (que es una lista de pictos)
    }

    //Funcion que mueve los pictos correctamente
    fun moverPicto(from: Int, to: Int, context: Context) {
        var destino: Int = to
         //Copia del picto a mover
        val aux1 = listaPerfiles[posicion.value!!].listaN1[0].pictoList.filter{ it.level1 }.size
        val aux2 = listaPerfiles[posicion.value!!].listaN1[posicionLista.value!!].pictoList.filter{ it.level2 && !it.isCategory }.size
        val aux3 = listaPerfiles[posicion.value!!].listaN1[posicionLista.value!!].pictoList.filter{ (it.level2 || it.level3) && !it.isCategory }.size

        if(nivelBotonConfig == "Nivel 2: Pictogramas + Categorías" && posicionLista.value!! == 0) {
            if (!listaPerfiles[posicion.value!!].listaN1[posicionLista.value!!].pictoList.filter { it.level2 }[from].isCategory) {
                if (to >= aux2) {
                    Toast.makeText(context, "Solo entre pictogramas", Toast.LENGTH_SHORT).show()
                }
                else {
                    val pictoTemp: Picto = listaPerfiles[posicion.value!!].listaN1[posicionLista.value!!].pictoList[aux1+from]
                    destino = aux1 + to
                    listaPerfiles[posicion.value!!].listaN1[posicionLista.value!!].pictoList
                        .removeAt(aux1 + from)
                    listaPerfiles[posicion.value!!].listaN1[posicionLista.value!!].pictoList
                        .add(destino, pictoTemp)
                }
            }
            else {
                Toast.makeText(context, "Solo entre pictogramas", Toast.LENGTH_SHORT).show()
            }
        }
        else if (nivelBotonConfig == "Nivel 3: Pictogramas + Categorías + Rutinas" && posicionLista.value!! == 0) {
            if (!listaPerfiles[posicion.value!!].listaN1[posicionLista.value!!].pictoList.filter { it.level2 || it.level3 }[from].isCategory &&
                !listaPerfiles[posicion.value!!].listaN1[posicionLista.value!!].pictoList.filter { it.level2 || it.level3 }[from].isRoutine) {
                if (to >= aux3) {
                    Toast.makeText(context, "Solo entre pictogramas", Toast.LENGTH_SHORT).show()
                }
                else{
                    val pictoTemp: Picto = listaPerfiles[posicion.value!!].listaN1[posicionLista.value!!].pictoList[aux1+from]
                    destino = aux1 + to
                    listaPerfiles[posicion.value!!].listaN1[posicionLista.value!!].pictoList
                        .removeAt(aux1 + from)
                    listaPerfiles[posicion.value!!].listaN1[posicionLista.value!!].pictoList
                        .add(destino, pictoTemp)
                }
            }
            else {
                Toast.makeText(context, "Solo entre pictogramas", Toast.LENGTH_SHORT).show()
            }
        }
        else { //Para los pictos que no son de la lista principal del nivel 2 o 3
            val pictoTemp: Picto = listaPerfiles[posicion.value!!].listaN1[posicionLista.value!!].pictoList[from]
            listaPerfiles[posicion.value!!].listaN1[posicionLista.value!!].pictoList
                .removeAt(from)
            listaPerfiles[posicion.value!!].listaN1[posicionLista.value!!].pictoList
                .add(destino, pictoTemp)
        }
    }

    //Funciones para editar pictos dentro de la lista de cada perfil
    fun setFotoPicto(foto: Uri?, nivel: Int, posicionPicto: Int, posicionPerfil: Int, categoria: Int) {
        when (nivel) {
            1 -> listaPerfiles[posicionPerfil].listaN1[categoria].pictoList.filter { it.level1 }[posicionPicto].imageResource = foto
            2 -> listaPerfiles[posicionPerfil].listaN1[categoria].pictoList.filter { it.level2 }[posicionPicto].imageResource = foto
            3 -> listaPerfiles[posicionPerfil].listaN1[categoria].pictoList.filter { it.level2 || it.level3 }[posicionPicto].imageResource = foto
        }
    }
    fun setNombrePicto(nombre: String, nivel: Int, posicionPicto: Int, posicionPerfil: Int, categoria: Int) {
        when (nivel) {
            1 -> listaPerfiles[posicionPerfil].listaN1[categoria].pictoList.filter { it.level1 }[posicionPicto].textResource = nombre
            2 -> listaPerfiles[posicionPerfil].listaN1[categoria].pictoList.filter { it.level2 }[posicionPicto].textResource = nombre
            3 -> listaPerfiles[posicionPerfil].listaN1[categoria].pictoList.filter { it.level2 || it.level3 }[posicionPicto].textResource = nombre
        }
    }

    //Funciones para configurar el perfil
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

    //Funcion para conocer el nivel que se va a editar
    fun setNivelBotonConfigg(nivel: String) {
        nivelBotonConfig = nivel
    }

    //Funcion para conocer el tipo de picto que se añade (picto, cat o rut)
    fun setTipoTemp(tipoTemp: Int) {
        tipoTempVar = tipoTemp
    }

    //Funcion que configura el tablero por defecto
    private fun setDefaultTablero() { //lista de pictos por defecto de cada perfil
        listaPerfiles[posicionUltimoPerfil.value!!].listaN1.add(ListaPicto()) //Añadir lista de pictos inicial

        //Nivel pictogramas
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.yo), "Yo", 1)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.quiero), "Quiero", 1)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.no), "No", 1)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.bien), "Bien", 1)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.mal), "Mal", 1)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.casa), "Casa", 1)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.autob_s), "Autobús", 1)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.cepillar_los_dientes), "Lavar los dientes", 1)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.coger_el_tenedor), "A la mesa", 1)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.comer), "Comer", 1)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.beber), "Beber", 1)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.dolor_de_est_mago), "Dolor de tripa", 1)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.picar), "Me pica", 1)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.hacer_pis), "Hacer pis", 1)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.hacer_caca), "Hacer caca", 1)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.jugar), "Jugar", 1)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.gafas), "Gafas", 1)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.aud_fono), "Audífono", 1)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.mordedor), "Mordedor", 1)

        //Nivel categorías
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.yo), "Yo", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.quiero), "Quiero", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.no), "No", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.bien), "Bien", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.mal), "Mal", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.gafas), "Gafas", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.aud_fono), "Audífono", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.mordedor), "Mordedor", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.picar), "Me pica", 2)
        //Categoría de casa
        guardarCat(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.casa), "Casa")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.casa), "Casa", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.beber), "Beber", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.comer), "Comer", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.coger_el_tenedor), "A la mesa", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.cama), "Cama", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.dormir), "Dormir", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.jugar_con_el_tablet), "Tablet", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.recoger), "Recoger", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.jugar), "Jugar", 2)
        //Categoría de baño
        guardarCat(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.ba_o), "Baño")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.ba_o), "Baño", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.cepillar_los_dientes), "Lavar los dientes", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.cepillo_y_pasta_de_dientes), "Cepillo y pasta de dientes", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.crema), "Crema", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.duchar), "Duchar", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.esponja), "Esponja", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.jab_n), "Jabón", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.lavar_las_manos), "Lavar las manos", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.hacer_pis), "Hacer pis", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.hacer_caca), "Hacer caca", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.limpiar_el_culo), "Limpiarse", 2)
        //Categoría de colegio
        guardarCat(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.colegio), "Colegio")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.colegio), "Colegio", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.autob_s), "Autobús", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.compa_eros), "Compañeros", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.comer), "Comer", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.beber), "Beber", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.coger_el_tenedor), "A la mesa", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.jugar), "Jugar", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.recoger_la_silla), "Recoger la silla", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.recoger), "Recoger", 2)
        //Categoría de Médico
        guardarCat(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.hospital), "Médico")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.hospital), "Médico", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.dolor_de_est_mago), "Dolor de tripa", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.dolor_de_brazo), "Dolor de brazo", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.dolor_de_cabeza), "Dolor de cabeza", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.dolor_de_culo), "Dolor de culo", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.dolor_de_espalda), "Dolor de espalda", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.dolor_de_garganta), "Dolor de garganta", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.dolor_de_muela), "Dolor de muela", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.dolor_de_o_do), "Dolor de oído", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.dolor_de_pecho), "Dolor de pecho", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.dolor_de_pierna), "Dolor de pierna", 2)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.dolor_de_pie), "Dolor de pie", 2)

        //Nivel rutinas
        //Rutina de lavarse los dientes
        guardarRut(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.cepillar_los_dientes), "Lavar los dientes")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.___cepillo_de_dientes), "Cepillo de dientes", 3)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.___echar_pasta_de_dientes), "Pasta de dientes", 3)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.cepillo_y_pasta_de_dientes), "Echar pasta de dientes", 3)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.cepillar_los_dientes), "Lavar los dientes", 3)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.___abrir_el_grifo), "Abrir el grifo", 3)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.___vaso_de_agua), "Llenar vaso de agua", 3)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.___g_rgaras), "Hacer gárgaras", 3)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.___enjuagar), "Enjuagar y escupir el agua", 3)
        //Rutina de preparar la mochila
        guardarRut(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.___mochila), "Preparar la mochila")
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.___meter), "Meter en la mochila", 3)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.___estuche), "Estuche", 3)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.___libro), "Libros", 3)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.___agua), "Botella de agua", 3)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.___inhalador), "Inhalador", 3)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.___pa_al), "Pañal", 3)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.___ropa), "Ropa", 3)
        guardarPicto(Uri.parse("android.resource://com.example.ausagi/" + R.drawable.___cerrar_la_cremallera), "Cerrar la mochila", 3)

    }


}