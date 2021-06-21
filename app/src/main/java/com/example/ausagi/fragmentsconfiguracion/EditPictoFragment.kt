package com.example.ausagi.fragmentsconfiguracion

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.ausagi.R
import com.example.ausagi.databinding.FragmentEditPictoBinding
import com.example.ausagi.model.BoardViewModel
import com.example.ausagi.model.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_edit_picto.*

class EditPictoFragment : Fragment() {

    //VARIABLES----------------------------------------------------------------
    //Variables para el binding
    private var _binding: FragmentEditPictoBinding? = null
    private val binding get() = _binding!!

    //Variable para el viewmodel
    private val sharedViewModelProfile: ProfileViewModel by activityViewModels()
    private val sharedViewModel: BoardViewModel by activityViewModels()

    //Variable para la imagen de perfil
    private var imageUri: Uri? = null
    val REQUEST_CODE = 100

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditPictoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModelProfile
        }

        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        pictoAppear()

        boton_picto_foto.setOnClickListener {
            openGalleryForImage()
        }

        boton_picto_hecho.setOnClickListener {
            editItem()
            val action = EditPictoFragmentDirections.actionEditPictoFragmentToEditBoardFragment()
            findNavController().navigate(action)
        }

        //Botón para atrás
        botonAtrasEdit.setOnClickListener {
            sharedViewModel.setPosicion(0)
            sharedViewModelProfile.irAtrasEdit(sharedViewModel.getPosicion())
            sharedViewModel.setAtrasEditar(1)
            requireActivity().findNavController(R.id.nav_host_fragment).navigateUp()
        }

    }



    //FUNCIONES---------------------------------------------

    private fun editItem() {
        //funcion para guardar los datos editados
        sharedViewModelProfile.editItem(imageUri, sharedViewModel.getPosicion(), espacio_nombre.text.toString(), context)
        //funcion de navegacion
        sharedViewModel.setPosicion(0)
        sharedViewModelProfile.irAtrasEdit(sharedViewModel.getPosicion())
        sharedViewModel.setAtrasEditar(1)
    }

    //funciones intent de foto
    //Abre la galería con un Intent
    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    //Una vez abre la galería, se elige una imagen
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            imageUri = data?.data
            espacio_picto_foto.setImageURI(imageUri) // handle chosen image
        }
    }

    //Funcion para que aparezca el picto para editar
    private fun pictoAppear() {
        if (sharedViewModelProfile.getNivelBotonConfigg() == "Nivel 1: Pictogramas") {
            espacio_picto_foto.setImageURI(sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.getPosicionPer()].listaN1[sharedViewModelProfile.getPosicionLis()].pictoList.filter { it.level1 }[sharedViewModel.getPosicion()].imageResource)
            espacio_nombre.setText(sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.getPosicionPer()].listaN1[sharedViewModelProfile.getPosicionLis()].pictoList.filter { it.level1 }[sharedViewModel.getPosicion()].textResource)
        } else if (sharedViewModelProfile.getNivelBotonConfigg() == "Nivel 2: Pictogramas + Categorías") {
            espacio_picto_foto.setImageURI(sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.getPosicionPer()].listaN1[sharedViewModelProfile.getPosicionLis()].pictoList.filter { it.level2 }[sharedViewModel.getPosicion()].imageResource)
            espacio_nombre.setText(sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.getPosicionPer()].listaN1[sharedViewModelProfile.getPosicionLis()].pictoList.filter { it.level2 }[sharedViewModel.getPosicion()].textResource)
        } else {
            espacio_picto_foto.setImageURI(sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.getPosicionPer()].listaN1[sharedViewModelProfile.getPosicionLis()].pictoList.filter { it.level2 || it.level3 }[sharedViewModel.getPosicion()].imageResource)
            espacio_nombre.setText(sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.getPosicionPer()].listaN1[sharedViewModelProfile.getPosicionLis()].pictoList.filter { it.level2 || it.level3 }[sharedViewModel.getPosicion()].textResource)
        }
    }

}