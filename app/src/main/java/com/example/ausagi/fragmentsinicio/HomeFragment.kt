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
import com.example.ausagi.model.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), Communicator {

    //VARIABLES----------------------------------------------------------------
    //Variables para el binding
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    //Variable para el viewmodel
    private val sharedViewModel: ProfileViewModel by activityViewModels()

    //FUNCIONES-----------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
        }

        (requireActivity() as AppCompatActivity).supportActionBar?.show()

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
        return sharedViewModel.listaPerfiles
    }

    override fun passData(position: Int) {
        sharedViewModel.posicion.value = position
    }


    //Funciones que no interesan en este fragment
    override fun addPictoBarra(position: Int) {
    }

    override fun passClicked(pressed: Int) {
    }

}

