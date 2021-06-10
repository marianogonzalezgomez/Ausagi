package com.example.ausagi.fragmentstableros

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.ausagi.R
import com.example.ausagi.adapter.Communicator
import com.example.ausagi.adapter.ItemRoutineAdapter
import com.example.ausagi.database.Picto
import com.example.ausagi.databinding.FragmentBoardRoutineBinding
import com.example.ausagi.model.BoardViewModel
import com.example.ausagi.model.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_board_routine.*

class BoardRoutineFragment : Fragment(), Communicator {

    //VARIABLES----------------------------------------------------------------
    //Variables para el binding
    private var _binding: FragmentBoardRoutineBinding? = null
    private val binding get() = _binding!!

    //Variables para el viewmodel
    private val sharedViewModel: BoardViewModel by activityViewModels()
    private val sharedViewModelProfile: ProfileViewModel by activityViewModels()

    //Variables para el botón de atrás
    var contAtras: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBoardRoutineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
        }

        val recyclerView = binding.recyclerViewRutinas

        recyclerView.apply {
            adapter = ItemRoutineAdapter(requireContext(), loadPictos())
        }

        //Botones para ir atrás
        botonAtrasInicio.setOnClickListener {
            //Se ponen a 0 estos valores para volver a la pantalla anterior con normalidad
            sharedViewModel.clicado.value = 0
            sharedViewModel.posicion.value = 0
            sharedViewModelProfile.posicionLista.value = 0
            requireActivity().findNavController(R.id.nav_host_fragment).navigateUp()
        }
        botonAtrasFinal.setOnClickListener {
            //Se ponen a 0 estos valores para volver a la pantalla anterior con normalidad
            sharedViewModel.clicado.value = 0
            sharedViewModel.posicion.value = 0
            sharedViewModelProfile.posicionLista.value = 0
            requireActivity().findNavController(R.id.nav_host_fragment).navigateUp()
        }

    }

    private fun loadPictos(): MutableList<Picto> {
        return sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList
    }

    override fun passData(position: Int) {

    }

    override fun passClicked(pressed: Int) {

    }

    override fun addPictoBarra(position: Int) {

    }

}
