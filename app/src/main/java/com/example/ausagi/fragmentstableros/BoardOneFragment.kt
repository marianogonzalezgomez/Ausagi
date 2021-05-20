package com.example.ausagi.fragmentstableros

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.ausagi.R
import com.example.ausagi.R.*
import com.example.ausagi.adapter.Communicator
import com.example.ausagi.adapter.ItemBarraAdapter
import com.example.ausagi.adapter.ItemBoardAdapter
import com.example.ausagi.database.Picto
import com.example.ausagi.databinding.FragmentBoardOneBinding
import com.example.ausagi.model.BoardViewModel
import com.example.ausagi.model.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_board_one.*
import kotlinx.android.synthetic.main.item_picto_view.view.*
import java.util.*


class BoardOneFragment : Fragment(), Communicator {

    //VARIABLES----------------------------------------------------------------
    //Variables para el binding
    private var _binding: FragmentBoardOneBinding? = null
    private val binding get() = _binding!!

    //Variables para el viewmodel
    private val sharedViewModel: BoardViewModel by activityViewModels()
    private val sharedViewModelProfile: ProfileViewModel by activityViewModels()

    //Variables para el TTS (text to speech)
    var ttsObject: TextToSpeech? = null
    var result: Int? = null

    //FUNCIONES-----------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //override del metodo del botón de atrás
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            if(sharedViewModelProfile.posicionLista.value == 0) {
                requireActivity().findNavController(R.id.nav_host_fragment).navigateUp()
            }
            sharedViewModelProfile.posicionLista.value = 0
            sharedViewModel.atras.value = 1
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBoardOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
        }

        sharedViewModel.eliminarPictosBarra()

        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        val recyclerView = binding.cuadriculaPictosN1RecyclerView
        val recyclerView2 = binding.barraPictosN1RecyclerView

        recyclerView.apply {
            adapter = ItemBoardAdapter(requireContext(), this@BoardOneFragment, loadPictos())
        }

        recyclerView2.apply {
            adapter = ItemBarraAdapter(requireContext(), loadPictosBarra())
        }

        //Actualizar el recycler de la barra de acción cada vez que se añade un pictograma y se mueve el scrollbar automáticamente
        //Comprobar si es categoría. Si es categoría, se cargan de nuevo los pictogramas en el adapter
        sharedViewModel.clicado.observe(viewLifecycleOwner, Observer{
            if(sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList[sharedViewModel.posicion.value!!].isCategory) {
                checkCategory()
                recyclerView.adapter = ItemBoardAdapter(requireContext(), this@BoardOneFragment, loadPictos())
            }
            else {
                recyclerView2.adapter?.notifyDataSetChanged()
                recyclerView2.scrollToPosition(sharedViewModel.listaPictosBarra.size - 1)
            }
        })

        //Actualizar el recycler de la tabla de pictos cada vez que se retrocede en una categoría
        sharedViewModel.atras.observe(viewLifecycleOwner, Observer{
            sharedViewModelProfile.posicionLista.value = 0
            recyclerView.adapter = ItemBoardAdapter(requireContext(), this@BoardOneFragment, loadPictos())
        })

        //Eliminar Pictos de la barra de Acción con el botón eliminar
        botonEliminar.setOnClickListener {
            sharedViewModel.eliminarPictosBarra()
            recyclerView2.adapter?.notifyDataSetChanged()
        }

        //Crear objeto para llevar a cabo el TTS
        ttsObject = TextToSpeech(activity) { status ->
            if (status == TextToSpeech.SUCCESS) {
                result = ttsObject!!.setLanguage(Locale("spa","spa"))
                ttsObject!!.setPitch(0.75F)
            } else {
                Toast.makeText(requireActivity(), "Opción no disponible en su dispositivo", Toast.LENGTH_SHORT).show()
            }
        }

        //Reproducir la voz del TTS con el botón play
        botonPlay.setOnClickListener { v ->
            when (v.id) {
                botonPlay.id -> if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA) {
                    Toast.makeText(requireActivity(), "Opción no disponible en su dispositivo", Toast.LENGTH_SHORT).show()
                } else {
                    var stringTemp = ""
                    sharedViewModel.listaPictosBarra.forEach {
                        stringTemp += it.textResource + " "
                    }
                    ttsObject!!.speak(stringTemp, TextToSpeech.QUEUE_FLUSH, null)
                }
            }
        }

    }

    private fun loadPictos(): MutableList<Picto> {

        if (sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].level == "Nivel 1: Pictogramas") {
            val listaFiltroTemporal: MutableList<Picto> =
                sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList.filter { !it.isCategory } //&& !it.isRoutine
                    .toMutableList()
            return listaFiltroTemporal
        }
        else if (sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].level == "Nivel 2: Pictogramas + Categorías") {
            val listaFiltroTemporal: MutableList<Picto> =
            sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList.filter { !it.isRoutine }
                .toMutableList()
            return listaFiltroTemporal
        }
        //Nivel 3: Pictogramas + Categorías + Rutinas (Cuando se pueden ver tanto las categorías como las rutinas)
        return sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList

    }

    private fun loadPictosBarra(): MutableList<Picto> {
        return sharedViewModel.listaPictosBarra
    }

    override fun addPictoBarra(position: Int) {
        if(sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList[position].isCategory == false) {
            sharedViewModel.addPicto(sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList[position])
        }
    }

    override fun passData(position: Int) {
        sharedViewModel.posicion.value = position
    }

    override fun passClicked(pressed: Int) {
        sharedViewModel.clicado.value = pressed
    }

    //La funcion que se encarga de averiguar la categoría que se ha presionado
    fun checkCategory() {
        val limit: Int = sharedViewModel.posicion.value!!
        var i = 0
        var numCat = 0

        while(i <= limit) {
            if(sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList[i].isCategory){
                numCat++
            }
            i++
        }
        sharedViewModelProfile.posicionLista.value = numCat
    }

    //Destruir el objeto TTS que se ha creado al inicio del fragmento
    override fun onDestroy() {
        super.onDestroy()
        if (ttsObject != null) {
            ttsObject!!.stop()
            ttsObject!!.shutdown()
        }
    }

}

