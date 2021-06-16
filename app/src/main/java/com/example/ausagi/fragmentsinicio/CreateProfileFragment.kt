package com.example.ausagi.fragmentsinicio

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
import androidx.recyclerview.widget.RecyclerView
import com.example.ausagi.R
import com.example.ausagi.databinding.FragmentCreateProfileBinding
import com.example.ausagi.model.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_create_profile.*

class CreateProfileFragment : Fragment() {

    //VARIABLES----------------------------------------------------------------
    //Variables para el binding
    private var _binding: FragmentCreateProfileBinding? = null
    private lateinit var recyclerView: RecyclerView
    private val binding get() = _binding!!

    //Variable para el viewmodel
    private val sharedViewModel: ProfileViewModel by activityViewModels()

    //Variable para la imagen de perfil
    private var imageUri: Uri? = null
    val REQUEST_CODE = 100

    //FUNCIONES-----------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCreateProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
            createProfileFragment = this@CreateProfileFragment //para los clicklisteners del xml
        }

        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        //Valores por defecto
        sharedViewModel.setNivelTemp("Nivel 1: Pictogramas")
        sharedViewModel.setColorTemp("Azul")

        boton_nuevo_perfil_foto.setOnClickListener {
            openGalleryForImage()
        }

        boton_perfil_creado.setOnClickListener {
            //funcion para guardar los datos
            sharedViewModel.guardarPerfil(imageUri,
                    espacio_nombre.text.toString(),
                    espacio_comentario.text.toString())
            Toast.makeText(requireActivity(), "Perfil creado", Toast.LENGTH_SHORT).show()

            //funcion de navegacion
            val action = CreateProfileFragmentDirections.actionCreateProfileFragmentToHomeFragment()
            findNavController().navigate(action)
        }

        //Botón para atrás
        botonAtrasCreate.setOnClickListener {
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
            espacio_nuevo_perfil_foto.setImageURI(imageUri) // handle chosen image
        }
    }

}