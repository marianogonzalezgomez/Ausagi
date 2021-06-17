package com.example.ausagi.fragmentsconfiguracion

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

    //FUNCIONES-----------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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
            //funcion para guardar los datos editados
            //sharedViewModelProfile.guardarPictoPerson(imageUri, espacio_nombre.text.toString(), mapNivel(), sharedViewModelProfile.posicion.value!!, sharedViewModelProfile.posicionLista.value!!)
            if (imageUri != null) {
                sharedViewModelProfile.setFotoPicto(imageUri, mapNivel(), sharedViewModel.posicion.value!!, sharedViewModelProfile.posicion.value!!, sharedViewModelProfile.posicionLista.value!!)
            }
            sharedViewModelProfile.setNombrePicto(espacio_nombre.text.toString(), mapNivel(), sharedViewModel.posicion.value!!, sharedViewModelProfile.posicion.value!!, sharedViewModelProfile.posicionLista.value!!)
            Toast.makeText(requireActivity(), "Guardado", Toast.LENGTH_SHORT).show()

            //funcion de navegacion
            sharedViewModel.posicion.value = 0
            irAtras()
            sharedViewModel.atrasEditar.value = 1
            val action = EditPictoFragmentDirections.actionEditPictoFragmentToEditBoardFragment()
            findNavController().navigate(action)
        }

        //Botón para atrás
        botonAtrasEdit.setOnClickListener {
            sharedViewModel.posicion.value = 0
            irAtras()
            sharedViewModel.atrasEditar.value = 1
            requireActivity().findNavController(R.id.nav_host_fragment).navigateUp()
        }

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

    //Funcion de mapeo de nivel
    private fun mapNivel(): Int {
        if (sharedViewModelProfile.nivelBotonConfig == "Nivel 1: Pictogramas"){
            return 1
        }
        else if (sharedViewModelProfile.nivelBotonConfig == "Nivel 2: Pictogramas + Categorías"){
            return 2
        }
        else
            return 3
    }

    //Funcion para que aparezca el picto para editar
    private fun pictoAppear() {
        if (sharedViewModelProfile.nivelBotonConfig == "Nivel 1: Pictogramas"){
            espacio_picto_foto.setImageURI(sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList.filter { it.level1 }[sharedViewModel.posicion.value!!].imageResource)
            espacio_nombre.setText(sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList.filter { it.level1 }[sharedViewModel.posicion.value!!].textResource)
        }
        else if (sharedViewModelProfile.nivelBotonConfig == "Nivel 2: Pictogramas + Categorías"){
            espacio_picto_foto.setImageURI(sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList.filter { it.level2 }[sharedViewModel.posicion.value!!].imageResource)
            espacio_nombre.setText(sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList.filter { it.level2 }[sharedViewModel.posicion.value!!].textResource)
        }
        else {
            espacio_picto_foto.setImageURI(sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList.filter { it.level2 || it.level3 }[sharedViewModel.posicion.value!!].imageResource)
            espacio_nombre.setText(sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList.filter { it.level2 || it.level3 }[sharedViewModel.posicion.value!!].textResource)
        }
    }

    //Función para ir correctamente hacia atrás
    private fun irAtras() {
        if (sharedViewModelProfile.nivelBotonConfig == "Nivel 1: Pictogramas"){
            sharedViewModelProfile.posicionLista.value = sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList.filter { it.level1 }[sharedViewModel.posicion.value!!].whatCategory
        }
        else if (sharedViewModelProfile.nivelBotonConfig == "Nivel 2: Pictogramas + Categorías"){
            sharedViewModelProfile.posicionLista.value = sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList.filter { it.level2 }[sharedViewModel.posicion.value!!].whatCategory
        }
        else
            sharedViewModelProfile.posicionLista.value = sharedViewModelProfile.listaPerfiles[sharedViewModelProfile.posicion.value!!].listaN1[sharedViewModelProfile.posicionLista.value!!].pictoList.filter { it.level2 || it.level3 }[sharedViewModel.posicion.value!!].whatCategory
    }

}