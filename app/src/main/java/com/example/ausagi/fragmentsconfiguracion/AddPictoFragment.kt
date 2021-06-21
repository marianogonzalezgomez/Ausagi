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

        //Ejecuta la función del intent de abrir galería al presionar el boton correspondiente
        boton_picto_foto.setOnClickListener {
            openGalleryForImage()
        }

        //funcion para guardar los datos editados
        boton_picto_hecho.setOnClickListener {
            if (imageUri != null && espacio_nombre.text!!.isNotEmpty()) {
                guardarItem()
                val action = AddPictoFragmentDirections.actionAddPictoFragmentToEditBoardFragment()
                findNavController().navigate(action)
            }
            else{
            Toast.makeText(requireActivity(), "Ambos foto y texto necesarios", Toast.LENGTH_SHORT).show()
            }
        }

        //Botón para atrás
        botonAtrasEdit.setOnClickListener {
            sharedViewModel.setPosicion(0)
            sharedViewModelProfile.irAtrasEdit(sharedViewModel.getPosicion())
            sharedViewModel.setAtrasEditar(1)
            sharedViewModelProfile.setTipoTemp(0)
            requireActivity().findNavController(R.id.nav_host_fragment).navigateUp()
        }

    }



    //FUNCIONES-----------------------------------------------------------------------

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

    //Función para mostrar las opciones de acuerdo al nivel en el que nos encontremos
    private fun mostrarOpciones() {
        if (sharedViewModelProfile.getNivelBotonConfigg() == "Nivel 1: Pictogramas"){
            titulo_elegir_tipo.visibility = View.GONE
            radioGroup_addPicto.visibility = View.GONE
            sharedViewModelProfile.setTipoTemp(0)
        }
        else if (sharedViewModelProfile.getNivelBotonConfigg() == "Nivel 2: Pictogramas + Categorías"){
            if (!sharedViewModel.getInCategory()) {
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
        else if (sharedViewModelProfile.getNivelBotonConfigg() == "Nivel 3: Pictogramas + Categorías + Rutinas") {
            if (!sharedViewModel.getInCategory()) {
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

    //Funcion guardar item
    private fun guardarItem(){
        sharedViewModelProfile.guardarItem(imageUri, espacio_nombre.text.toString(), sharedViewModelProfile.mapNivel())
        Toast.makeText(requireActivity(), "Guardado", Toast.LENGTH_SHORT).show()
        //funcion de navegacion
        sharedViewModel.setPosicion(0)
        sharedViewModelProfile.irAtrasEdit(sharedViewModel.getPosicion())
        sharedViewModel.setAtrasEditar(1)
    }

}