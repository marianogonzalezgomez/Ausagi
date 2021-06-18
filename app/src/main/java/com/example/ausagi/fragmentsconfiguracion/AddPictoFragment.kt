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
import com.example.ausagi.databinding.FragmentAddPictoBinding
import com.example.ausagi.model.BoardViewModel
import com.example.ausagi.model.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_add_picto.*
import kotlinx.android.synthetic.main.fragment_add_picto.espacio_nombre
import kotlinx.android.synthetic.main.fragment_edit_picto.*
import kotlinx.android.synthetic.main.fragment_edit_picto.botonAtrasEdit
import kotlinx.android.synthetic.main.fragment_edit_picto.boton_picto_foto
import kotlinx.android.synthetic.main.fragment_edit_picto.boton_picto_hecho
import kotlinx.android.synthetic.main.fragment_edit_picto.espacio_picto_foto

class AddPictoFragment : Fragment() {

    //VARIABLES----------------------------------------------------------------
    //Variables para el binding
    private var _binding: FragmentAddPictoBinding? = null
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
        _binding = FragmentAddPictoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModelProfile
        }

        //Funciones de visibilidad
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        mostrarOpciones()
        
        boton_picto_foto.setOnClickListener {
            openGalleryForImage()
        }
        //funcion para guardar los datos editados
        boton_picto_hecho.setOnClickListener {
            if (imageUri != null && espacio_nombre.text!!.isNotEmpty()) {
                guardarItem()
                Toast.makeText(requireActivity(), "Guardado", Toast.LENGTH_SHORT).show()
                //funcion de navegacion
                sharedViewModel.posicion.value = 0
                irAtras()
                sharedViewModel.atrasEditar.value = 1
                val action = AddPictoFragmentDirections.actionAddPictoFragmentToEditBoardFragment()
                findNavController().navigate(action)
            }
            else{
            Toast.makeText(requireActivity(), "Ambos foto y texto necesarios", Toast.LENGTH_SHORT).show()
            }
        }

        //Botón para atrás
        botonAtrasEdit.setOnClickListener {
            sharedViewModel.posicion.value = 0
            irAtras()
            sharedViewModel.atrasEditar.value = 1
            sharedViewModelProfile.setTipoTemp(0)
            requireActivity().findNavController(R.id.nav_host_fragment).navigateUp()
        }

    }

    //Funciones intent de foto
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

    //Función para mostrar las opciones de acuerdo al nivel en el que nos encontremos
    private fun mostrarOpciones() {
        if (sharedViewModelProfile.nivelBotonConfig == "Nivel 1: Pictogramas"){
            titulo_elegir_tipo.visibility = View.GONE
            radioGroup_addPicto.visibility = View.GONE
            sharedViewModelProfile.setTipoTemp(0)
        }
        else if (sharedViewModelProfile.nivelBotonConfig == "Nivel 2: Pictogramas + Categorías"){
            if (sharedViewModel.inCategory.value == false) {
                titulo_elegir_tipo.visibility = View.VISIBLE
                radioGroup_addPicto.visibility = View.VISIBLE
                radioButtonN3.visibility = View.GONE
            }
            else {
                titulo_elegir_tipo.visibility = View.GONE
                radioGroup_addPicto.visibility = View.GONE
                sharedViewModelProfile.setTipoTemp(0)
            }
        }
        else if (sharedViewModelProfile.nivelBotonConfig == "Nivel 3: Pictogramas + Categorías + Rutinas") {
            if (sharedViewModel.inCategory.value == false) {
                titulo_elegir_tipo.visibility = View.VISIBLE
                radioGroup_addPicto.visibility = View.VISIBLE
                radioButtonN3.visibility = View.VISIBLE
            }
            else {
                titulo_elegir_tipo.visibility = View.GONE
                radioGroup_addPicto.visibility = View.GONE
                sharedViewModelProfile.setTipoTemp(0)
            }
        }
    }

    private fun guardarItem() {
        if (sharedViewModelProfile.tipoTempVar == 0) { //Picto
            sharedViewModelProfile.guardarPictoPerson(imageUri, espacio_nombre.text.toString(), mapNivel(), sharedViewModelProfile.posicion.value!!, sharedViewModelProfile.posicionLista.value!!)
        }
        else if (sharedViewModelProfile.tipoTempVar == 1) { //Categoría
            sharedViewModelProfile.guardarCatPerson(imageUri, espacio_nombre.text.toString(), mapNivel(), sharedViewModelProfile.posicion.value!!)
        }
        else if (sharedViewModelProfile.tipoTempVar == 2) { //Rutina
            sharedViewModelProfile.guardarRutPerson(imageUri, espacio_nombre.text.toString(), mapNivel(), sharedViewModelProfile.posicion.value!!)
        }
        sharedViewModelProfile.setTipoTemp(0)
    }

}