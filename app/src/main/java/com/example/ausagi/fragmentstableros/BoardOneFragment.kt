package com.example.ausagi.fragmentstableros

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.ausagi.adapter.ItemBoardAdapter
import com.example.ausagi.database.Picto
import com.example.ausagi.databinding.FragmentBoardOneBinding
import com.example.ausagi.model.BoardViewModel

class BoardOneFragment : Fragment() {

    //VARIABLES----------------------------------------------------------------
    //Variables para el binding
    private var _binding: FragmentBoardOneBinding? = null
    private val binding get() = _binding!!
    //Variable para el viewmodel
    private val sharedViewModel: BoardViewModel by activityViewModels()

    //FUNCIONES-----------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
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

        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        val recyclerView = binding.cuadriculaPictosN1RecyclerView
        val myDataSet = loadPictos()

        recyclerView.apply {
            //layoutManager = LinearLayoutManager(requireContext()) //APARENTEMENTE NO SE DEBERIA PONER (?)
            // set the custom adapter to the RecyclerView
            adapter = ItemBoardAdapter(requireContext(), myDataSet)
        }

    }

    private fun loadPictos(): MutableList<Picto> {
        return sharedViewModel.listaPictos
    }

    /*override fun passData(position: Int) {
        sharedViewModel.posicion.value = position
    }*/
}

