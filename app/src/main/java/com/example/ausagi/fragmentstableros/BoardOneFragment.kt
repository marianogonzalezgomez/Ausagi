package com.example.ausagi.fragmentstableros

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.ausagi.adapter.Communicator
import com.example.ausagi.adapter.ItemBarraAdapter
import com.example.ausagi.adapter.ItemBoardAdapter
import com.example.ausagi.database.Picto
import com.example.ausagi.databinding.FragmentBoardOneBinding
import com.example.ausagi.model.BoardViewModel
import kotlinx.android.synthetic.main.fragment_board_one.*
import kotlinx.android.synthetic.main.item_picto_view.view.*

class BoardOneFragment : Fragment(), Communicator {

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

        sharedViewModel.eliminarPictosBarra()

        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        val recyclerView = binding.cuadriculaPictosN1RecyclerView
        val recyclerView2 = binding.barraPictosN1RecyclerView

        recyclerView.apply {
            // set the custom adapter to the RecyclerView
            adapter = ItemBoardAdapter(requireContext(), this@BoardOneFragment, loadPictos())
        }

        recyclerView2.apply {
            adapter = ItemBarraAdapter(requireContext(), loadPictosBarra())
        }

        sharedViewModel.clicado.observe(viewLifecycleOwner, Observer{
            recyclerView2.adapter?.notifyDataSetChanged()
            recyclerView2.scrollToPosition(sharedViewModel.listaPictosBarra.size-1)
        }) //Se actualiza el recycler de la barra de acción cada vez que añadimos un pictograma y se mueve el scrollbar

        botonEliminar.setOnClickListener {
            sharedViewModel.eliminarPictosBarra()
            recyclerView2.adapter?.notifyDataSetChanged()
        } //se eliminan los pictos del recycler de la barra de acción

    }

    private fun loadPictos(): MutableList<Picto> {
        return sharedViewModel.listaPictos
    }

    private fun loadPictosBarra(): MutableList<Picto> {
        return sharedViewModel.listaPictosBarra
    }

    override fun passData(position: Int) {
        sharedViewModel.posicion.value = position
    }

    override fun addPictoBarra(position: Int) {
        sharedViewModel.addPicto(sharedViewModel.listaPictos[sharedViewModel.posicion.value!!])
    }

    override fun passClicked(pressed: Int) {
        sharedViewModel.clicado.value = pressed
    }

}

