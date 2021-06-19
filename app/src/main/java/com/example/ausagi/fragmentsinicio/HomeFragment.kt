package com.example.ausagi.fragmentsinicio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ausagi.adapter.Communicator
import com.example.ausagi.adapter.ItemProfileAdapter
import com.example.ausagi.database.Profile
import com.example.ausagi.databinding.FragmentHomeBinding
import com.example.ausagi.model.BoardViewModel
import com.example.ausagi.model.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), Communicator {

    //VARIABLES----------------------------------------------------------------
    //Variables para el binding
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    //Variable para el viewmodel
    private val sharedViewModelProfile: ProfileViewModel by activityViewModels()
    private val sharedViewModel: BoardViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModelProfile
        }

        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        val recyclerView = binding.recyclerView
        val myDataSet = loadProfiles()

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            // set the custom adapter to the RecyclerView
            adapter = ItemProfileAdapter(requireContext(), this@HomeFragment, myDataSet)
        }

        boton_instrucciones.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToInstructionsFragment()
            findNavController().navigate(action)
        }

        boton_nuevo_perfil.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToCreateProfileFragment()
            findNavController().navigate(action)
        }

        boton_salir.setOnClickListener {
            activity?.finish()
            System.exit(0)
        }

        //El listener del boton_masinfo está en el adapter de cada perfil que se muestra en home

    }

    private fun loadProfiles(): MutableList<Profile> {
        return sharedViewModelProfile.listaPerfiles
    }

    override fun passData(position: Int) {
        sharedViewModelProfile.posicion.value = position
    }

    override fun onResume() {
        super.onResume()
        sharedViewModelProfile.posicionLista.value = 0
        sharedViewModelProfile.posicion.value = 0
        sharedViewModel.clicado.value = 0
    }


    //Funciones que no interesan en este fragment
    override fun addPictoBarra(position: Int) {
    }

    override fun passClicked(pressed: Int) {
    }

    override fun passClickedConfig(pressedConfig: Int) {
    }

    override fun passClickedElim(pressedElim: Int) {
    }

    override fun passClickedMover(pressedMover: Int) {
    }

}

