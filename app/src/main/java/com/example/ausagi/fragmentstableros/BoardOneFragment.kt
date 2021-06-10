package com.example.ausagi.fragmentstableros

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
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
import kotlinx.android.synthetic.main.item_picto_view.*
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
    private var ttsObject: TextToSpeech? = null
    private var result: Int? = null

    //Variables para el botón de atrás
    private var contAtras: Int = 0

    //FUNCIONES-----------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        when (sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].colour) {
            "Azul" -> activity?.setTheme(style.Theme_Ausagi_Blue)
            "Marrón" -> activity?.setTheme(style.Theme_Ausagi_Brown)
            "Verde" -> activity?.setTheme(style.Theme_Ausagi_Green)
        }
        super.onCreate(savedInstanceState)
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

            if (sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].level == "Nivel 1: Pictogramas") {
                recyclerView2.adapter?.notifyDataSetChanged()
                recyclerView2.scrollToPosition(sharedViewModel.listaPictosBarra.size - 1)

            }
            else if (sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].level == "Nivel 2: Pictogramas + Categorías") {
                if (sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList.filter{ it.level2 }[sharedViewModel.posicion.value!!].isCategory) {
                    checkCatRout()
                    recyclerView.adapter = ItemBoardAdapter(requireContext(), this@BoardOneFragment, loadPictos())
                } else {
                    recyclerView2.adapter?.notifyDataSetChanged()
                    recyclerView2.scrollToPosition(sharedViewModel.listaPictosBarra.size - 1)
                }
            }
            else { //Nivel 3
                //Si es una rutina
                if (sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList.filter{ it.level2 || it.level3 }[sharedViewModel.posicion.value!!].isRoutine) {
                    checkCatRout()
                    val action =
                        BoardOneFragmentDirections.actionBoardOneFragmentToBoardRoutineFragment()
                    findNavController().navigate(action)
                }
                //si es una categoría
                else if (sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList.filter{ it.level2 || it.level3 }[sharedViewModel.posicion.value!!].isCategory) {
                    checkCatRout()
                    recyclerView.adapter = ItemBoardAdapter(requireContext(), this@BoardOneFragment, loadPictos())
                }
                //si es un pictograma
                else {
                    recyclerView2.adapter?.notifyDataSetChanged()
                    recyclerView2.scrollToPosition(sharedViewModel.listaPictosBarra.size - 1)
                }
            }
        })

        //Actualizar el recycler de la tabla de pictos cada vez que se retrocede en una categoría
        sharedViewModel.atras.observe(viewLifecycleOwner, Observer{
            sharedViewModelProfile.posicionLista.value = 0
            recyclerView.adapter?.notifyDataSetChanged()
            recyclerView.adapter = ItemBoardAdapter(requireContext(), this@BoardOneFragment, loadPictos())
        })

        //Eliminar Pictos de la barra de Acción con el botón eliminar
        botonEliminar.setOnClickListener {
            sharedViewModel.eliminarPictosBarra()
            recyclerView2.adapter?.notifyDataSetChanged()
        }

        //BotonUp Atrás
        botonAtras.setOnClickListener {
            if (sharedViewModelProfile.posicionLista.value == 0) {
                requireActivity().findNavController(R.id.nav_host_fragment).navigateUp()
                activity?.setTheme(style.Theme_Ausagi_White)
            }
            sharedViewModelProfile.posicionLista.value = 0
            sharedViewModel.atras.value = contAtras++
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

    //Carga la lista de pictos que se considere dependiendo el nivel (se filtra mediante el atributo de level de cada picto
    private fun loadPictos(): MutableList<Picto> {
        if (sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].level == "Nivel 1: Pictogramas") {
            return sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList.filter { it.level1 }
                .toMutableList()
        }
        else if (sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].level == "Nivel 2: Pictogramas + Categorías") {
            return sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList.filter { it.level2 }
                .toMutableList()
        }
        //Nivel 3: Pictogramas + Categorías + Rutinas (Cuando se pueden ver tanto las categorías como las rutinas)
        return sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList.filter { it.level2 || it.level3 }
            .toMutableList()
    }

    private fun loadPictosBarra(): MutableList<Picto> {
        return sharedViewModel.listaPictosBarra
    }

    //Añade a la barra (adapter) el pictograma de la posición que se le indica dependiendo del nivel al que pertenezca
    override fun addPictoBarra(position: Int) {
        if (sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].level == "Nivel 1: Pictogramas") {
            sharedViewModel.addPicto(sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList.filter { it.level1 }[position])
        }
        else if (!sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList.filter { it.level2 || it.level3 }[position].isCategory) {
            sharedViewModel.addPicto(sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList.filter { it.level2 || it.level3 }[position])
        }
    }

    override fun passData(position: Int) {
        sharedViewModel.posicion.value = position
    }

    override fun passClicked(pressed: Int) {
        sharedViewModel.clicado.value = pressed
    }

    //La funcion que se encarga de averiguar la categoría o la rutina que se ha presionado
    private fun checkCatRout() {
        val limit: Int = sharedViewModel.posicion.value!!
        var i = 0
        var num = 0

        while(i <= limit) {
            if(sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList.filter{ it.level2 || it.level3 }[i].isCategory){
                num++
            }
            else if(sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList.filter{ it.level2 || it.level3 }[i].isRoutine){
                num++
            }
            i++
        }
        sharedViewModelProfile.posicionLista.value = num
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

