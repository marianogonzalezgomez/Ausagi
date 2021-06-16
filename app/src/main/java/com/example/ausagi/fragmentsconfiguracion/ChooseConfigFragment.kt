package com.example.ausagi.fragmentsconfiguracion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ausagi.R
import com.example.ausagi.databinding.FragmentChooseConfigBinding
import com.example.ausagi.model.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_choose_config.*

class ChooseConfigFragment : Fragment() {

    //VARIABLES----------------------------------------------------------------
    //variables para el binding
    private var _binding: FragmentChooseConfigBinding? = null
    private lateinit var recyclerView: RecyclerView
    private val binding get() = _binding!!

    //Variables para el viewmodel
    private val sharedViewModel: ProfileViewModel by activityViewModels()

    //FUNCIONES-----------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChooseConfigBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            chooseConfigFragment = this@ChooseConfigFragment //para los clicklisteners del xml
        }


        //Botones de elección de tablero a editar
       boton_Tablero_1.setOnClickListener {
            val action = ChooseConfigFragmentDirections.actionChooseConfigFragmentToEditBoardFragment()
            findNavController().navigate(action)
           sharedViewModel.setNivelBotonConfigg("Nivel 1: Pictogramas")
       }
       boton_Tablero_2.setOnClickListener {
           val action = ChooseConfigFragmentDirections.actionChooseConfigFragmentToEditBoardFragment()
           findNavController().navigate(action)
           sharedViewModel.setNivelBotonConfigg("Nivel 2: Pictogramas + Categorías")
       }
       boton_Tablero_3.setOnClickListener {
           val action = ChooseConfigFragmentDirections.actionChooseConfigFragmentToEditBoardFragment()
           findNavController().navigate(action)
           sharedViewModel.setNivelBotonConfigg("Nivel 3: Pictogramas + Categorías + Rutinas")
       }

        //Botón para atrás
        botonAtras.setOnClickListener {
            requireActivity().findNavController(R.id.nav_host_fragment).navigateUp()
        }

    }

}