package com.example.ausagi.fragmentsinicio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ausagi.databinding.FragmentInformationBinding
import com.example.ausagi.model.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_create_profile.*
import kotlinx.android.synthetic.main.fragment_information.*

class InformationFragment : Fragment() {

    //VARIABLES----------------------------------------------------------------
    //Variables para el binding
    private var _binding: FragmentInformationBinding? = null
    private lateinit var recyclerView: RecyclerView
    private val binding get() = _binding!!

    //Variable para el viewmodel
    private val sharedViewModel: ProfileViewModel by activityViewModels()

    //FUNCIONES-----------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = FragmentInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
            informationFragment = this@InformationFragment //para los clicklisteners del xml
        }

        (requireActivity() as AppCompatActivity).supportActionBar?.show()

        boton_configurar_perfil.setOnClickListener {
            val action = InformationFragmentDirections.actionInformationFragmentToConfigurationFragment()
            findNavController().navigate(action)
        }

        espacio_informacion_foto.setImageURI(sharedViewModel.listaPerfiles[sharedViewModel.posicion.value!!].imageResource)
    }


}