package com.example.ausagi.fragmentsconfiguracion

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.ausagi.R
import com.example.ausagi.adapter.Communicator
import com.example.ausagi.adapter.ItemConfigAdapter
import com.example.ausagi.databinding.FragmentEditBoardBinding
import com.example.ausagi.model.BoardViewModel
import com.example.ausagi.model.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_board_one.*
import kotlinx.android.synthetic.main.fragment_choose_config.*
import kotlinx.android.synthetic.main.fragment_configuration.*
import kotlinx.android.synthetic.main.fragment_edit_board.*
import kotlinx.android.synthetic.main.item_config_view.*
import java.util.*


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

    //Variable para mover los pictogramas
    private val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END, 0) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {

            val adapter = recyclerView.adapter
            val from = viewHolder.adapterPosition
            val to = target.adapterPosition

            //Keep up-to-date the underlying data set
            Collections.swap(sharedViewModelProfile.loadPictosConfig(), from, to)
            adapter?.notifyItemMoved(from, to)

            //Hace permanentes los cambios
            sharedViewModelProfile.moverPicto(from, to, context)

            return true
        }
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            // Nada
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            recyclerView.adapter = ItemConfigAdapter(requireContext(), this@EditBoardFragment, sharedViewModelProfile.loadPictosConfig())
            super.clearView(recyclerView, viewHolder)
        }

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
            adapter = ItemConfigAdapter(requireContext(), this@EditBoardFragment, sharedViewModelProfile.loadPictosConfig())
        }

        //BotonUp Atrás
        botonAtrasEditBoard.setOnClickListener {
            if (sharedViewModelProfile.getPosicionLis() == 0) {
                requireActivity().findNavController(R.id.nav_host_fragment).navigateUp()
            }
            sharedViewModelProfile.setPosicionLis(0)
            sharedViewModel.setAtras(contAtras++)
        }
        //Actualizar el recycler de la tabla de pictos cada vez que se retrocede en una categoría
        sharedViewModel.atras.observe(viewLifecycleOwner, Observer{
            if(sharedViewModel.getAtrasEditar() == 0) {
                sharedViewModelProfile.setPosicionLis(0)
                sharedViewModel.setInCategory(false)
            }
            recyclerView.adapter?.notifyDataSetChanged()
            recyclerView.adapter = ItemConfigAdapter(requireContext(), this@EditBoardFragment, sharedViewModelProfile.loadPictosConfig())
            sharedViewModel.setAtrasEditar(0)
        })

        //Funcion para añadir un pictograma
        boton_añadir.setOnClickListener {
            val action = EditBoardFragmentDirections.actionEditBoardFragmentToAddPictoFragment()
            findNavController().navigate(action)
        }

        //Función para entrar en una categoría o una rutina
        sharedViewModel.clicado.observe(viewLifecycleOwner, Observer{
            if(sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.getPosicionPer()].listaN1[0].pictoList.filter{ it.level2 || it.level3 }.isNotEmpty()) {
                if (sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.getPosicionPer()].listaN1[0].pictoList.filter { it.level2 || it.level3 }[sharedViewModel.getPosicion()].isCategory ||
                    sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.getPosicionPer()].listaN1[0].pictoList.filter { it.level2 || it.level3 }[sharedViewModel.getPosicion()].isRoutine) {
                    sharedViewModelProfile.checkCatRout(sharedViewModel.getPosicion())
                    recyclerView.adapter = ItemConfigAdapter(requireContext(), this@EditBoardFragment, sharedViewModelProfile.loadPictosConfig())
                    sharedViewModel.setInCategory(true)
                }
            }
        })

        //Función para mover un pictograma
        ItemTouchHelper(simpleItemTouchCallback).attachToRecyclerView(recyclerView)

        //Funciones para editar un pictograma
        sharedViewModel.clicadoConfig.observe(viewLifecycleOwner, Observer{
            if(sharedViewModel.getClicadoConfig() != 0) {
                val action = EditBoardFragmentDirections.actionEditBoardFragmentToEditPictoFragment()
                findNavController().navigate(action)
                sharedViewModel.setClicadoConfig(0)
            }
        })

        //Funciones para eliminar los pictos, categorías y rutinas
        sharedViewModel.clicadoElim.observe(viewLifecycleOwner, Observer{
            if(sharedViewModel.getClicadoElim() != 0) {
                sharedViewModelProfile.eliminarItem(context, sharedViewModel.getPosicion())
                recyclerView.adapter?.notifyDataSetChanged()
                recyclerView.adapter = ItemConfigAdapter(requireContext(), this@EditBoardFragment, sharedViewModelProfile.loadPictosConfig())
                sharedViewModel.setClicadoElim(0)
                sharedViewModel.setPosicion(0)
            }
        })
    }


    //FUNCIONES----------------------------------------------------

    override fun passData(position: Int) {
        sharedViewModel.setPosicion(position)
    }

    override fun passClicked(pressed: Int) {
        sharedViewModel.setClicado(pressed)
    }

    override fun passClickedConfig(pressedConfig: Int) {
        sharedViewModel.setClicadoConfig(pressedConfig)
    }

    override fun passClickedElim(pressedElim: Int) {
        sharedViewModel.setClicadoElim(pressedElim)
    }

    override fun passClickedMover(pressedMover: Int) {
        sharedViewModel.setClicadoMover(pressedMover)
    }

    override fun addPictoBarra(position: Int) {
    }

}