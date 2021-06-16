package com.example.ausagi.fragmentsconfiguracion

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ausagi.R
import com.example.ausagi.adapter.Communicator
import com.example.ausagi.adapter.ItemConfigAdapter
import com.example.ausagi.database.Picto
import com.example.ausagi.databinding.FragmentEditBoardBinding
import com.example.ausagi.model.BoardViewModel
import com.example.ausagi.model.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_board_one.*
import kotlinx.android.synthetic.main.fragment_choose_config.*
import kotlinx.android.synthetic.main.fragment_choose_config.botonAtras
import kotlinx.android.synthetic.main.fragment_edit_board.*
import kotlinx.android.synthetic.main.item_config_view.*


class EditBoardFragment : Fragment(), Communicator {

    //VARIABLES----------------------------------------------------------------
    //variables para el binding
    private var _binding: FragmentEditBoardBinding? = null
    private lateinit var recyclerView: RecyclerView
    private val binding get() = _binding!!

    //Variables para el viewmodel
    private val sharedViewModel: BoardViewModel by activityViewModels()
    private val sharedViewModelProfile: ProfileViewModel by activityViewModels()

    //Variables para el botón de atrás
    private var contAtras: Int = 0

    //FUNCIONES-----------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBoardBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            editBoardFragment = this@EditBoardFragment //para los clicklisteners del xml
            viewModel = sharedViewModel
        }

        val recyclerView = binding.cuadriculaPictosConfiguracion

        recyclerView.apply {
            adapter = ItemConfigAdapter(requireContext(), this@EditBoardFragment, loadPictos())
        }

        sharedViewModel.clicadoConfig.observe(viewLifecycleOwner, Observer{
            if(sharedViewModel.clicadoConfig.value != 0) {
                //val action = EditBoardFragmentDirections.actionEditBoardFragmentToOptionsFragment()
                //findNavController().navigate(action)
                sharedViewModel.clicadoConfig.value = 0
            }
        })

        //BotonUp Atrás
        botonAtras.setOnClickListener {
            if (sharedViewModelProfile.posicionLista.value == 0) {
                requireActivity().findNavController(R.id.nav_host_fragment).navigateUp()
            }
            sharedViewModelProfile.posicionLista.value = 0
            sharedViewModel.atras.value = contAtras++
        }

        //Actualizar el recycler de la tabla de pictos cada vez que se retrocede en una categoría
        sharedViewModel.atras.observe(viewLifecycleOwner, Observer{
            sharedViewModelProfile.posicionLista.value = 0
            recyclerView.adapter?.notifyDataSetChanged()
            recyclerView.adapter = ItemConfigAdapter(requireContext(), this@EditBoardFragment, loadPictos())
        })

        //Función para entrar en una categoría
        sharedViewModel.clicado.observe(viewLifecycleOwner, Observer{
            if (sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList.filter{ it.level2 || it.level3 }[sharedViewModel.posicion.value!!].isCategory) {
                checkCatRout()
                recyclerView.adapter = ItemConfigAdapter(requireContext(), this@EditBoardFragment, loadPictos())
            }
        })

        //Funciones para eliminar los pictos, categorías y rutinas
        sharedViewModel.clicadoElim.observe(viewLifecycleOwner, Observer{
            if(sharedViewModel.clicadoElim.value != 0) {
            Toast.makeText(context, "Eliminado", Toast.LENGTH_SHORT).show()
                if (sharedViewModelProfile.nivelBotonConfig == "Nivel 1: Pictogramas") {
                    sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList
                        .remove(sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList.filter { it.level1 }[sharedViewModel.posicion.value!!])
                }
                else if (sharedViewModelProfile.nivelBotonConfig == "Nivel 2: Pictogramas + Categorías") {
                    sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList
                        .remove(sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList.filter { it.level2 }[sharedViewModel.posicion.value!!])
                }
                else {
                    sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList
                        .remove(sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList.filter { it.level2 || it.level3 }[sharedViewModel.posicion.value!!])
                }
            recyclerView.adapter?.notifyDataSetChanged()
            recyclerView.adapter = ItemConfigAdapter(requireContext(), this@EditBoardFragment, loadPictos())
            sharedViewModel.clicadoElim.value = 0
            }
        })

    }

    //Carga la lista de pictos que se considere dependiendo el nivel (se filtra mediante el atributo de level de cada picto)
    private fun loadPictos(): MutableList<Picto> {
        if (sharedViewModelProfile.nivelBotonConfig == "Nivel 1: Pictogramas") {
            return sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList.filter { it.level1 }
                .toMutableList()
        }
        else if (sharedViewModelProfile.nivelBotonConfig == "Nivel 2: Pictogramas + Categorías") {
            return sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList.filter { it.level2 }
                .toMutableList()
        }
        //Nivel 3: Pictogramas + Categorías + Rutinas (Cuando se pueden ver tanto las categorías como las rutinas)
        return sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList.filter { it.level2 || it.level3 }
            .toMutableList()
    }

    override fun passData(position: Int) {
        sharedViewModel.posicion.value = position
    }

    override fun passClicked(pressed: Int) {
        sharedViewModel.clicado.value = pressed
    }

    override fun addPictoBarra(position: Int) {
    }

    override fun passClickedConfig(pressedConfig: Int) {
        sharedViewModel.clicadoConfig.value = pressedConfig
    }

    override fun passClickedElim(pressedElim: Int) {
        sharedViewModel.clicadoElim.value = pressedElim
    }

    //Es necesario que al salir de la pantalla de edición, estos parámetros vuelvan a su estado inicial
    override fun onStop() {
        super.onStop()
        sharedViewModelProfile.posicionLista.value = 0
        sharedViewModel.posicion.value = 0
        sharedViewModel.clicado.value = 0
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

}