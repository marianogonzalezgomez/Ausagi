package com.example.ausagi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ausagi.databinding.FragmentInformationBinding
import kotlinx.android.synthetic.main.fragment_information.*

class InformationFragment : Fragment() {

    private var _binding: FragmentInformationBinding? = null
    private lateinit var recyclerView: RecyclerView
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {

        boton_configurar_perfil.setOnClickListener {
            val action = InformationFragmentDirections.actionInformationFragmentToConfigurationFragment()
            findNavController().navigate(action)
        }
    }


}