package com.example.ausagi

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ausagi.databinding.FragmentConfigurationBinding
import com.example.ausagi.model.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_configuration.*
import kotlinx.android.synthetic.main.fragment_create_profile.*


class ConfigurationFragment : Fragment() {

    //VARIABLES----------------------------------------------------------------
    //variables para el binding
    private var _binding: FragmentConfigurationBinding? = null
    private lateinit var recyclerView: RecyclerView
    private val binding get() = _binding!!
    //Variable para el viewmodel
    private val sharedViewModel: ProfileViewModel by activityViewModels()
    //Variables para la imagen de perfil
    private var imageUri: Uri? = null
    val REQUEST_CODE = 100

    //FUNCIONES-----------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = FragmentConfigurationBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
            configurationFragment = this@ConfigurationFragment //para los clicklisteners del xml
        }

        (requireActivity() as AppCompatActivity).supportActionBar?.show()

        boton_modificar_foto.setOnClickListener {
            openGalleryForImage()
        }

        boton_perfil_modificado.setOnClickListener {
            if(imageUri!=null){ sharedViewModel.setFoto(imageUri) }
            sharedViewModel.setNombre(espacio_nombre_config.text.toString())
            sharedViewModel.setNivel()
            sharedViewModel.setComentario(espacio_comentario_config.text.toString())
            sharedViewModel.setColor()
            val action = ConfigurationFragmentDirections.actionConfigurationFragmentToInformationFragment()
            findNavController().navigate(action)
        }

        boton_eliminar_perfil.setOnLongClickListener{
            sharedViewModel.eliminarPerfil()
            Toast.makeText(requireActivity(),"Perfil eliminado",Toast.LENGTH_SHORT).show()
            val action = ConfigurationFragmentDirections.actionConfigurationFragmentToHomeFragment()
            findNavController().navigate(action)
            true
        }
    }

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
            espacio_modificar_perfil_foto.setImageURI(imageUri) // handle chosen image
        }
    }

}