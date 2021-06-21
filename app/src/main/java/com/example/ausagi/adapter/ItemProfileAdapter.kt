package com.example.ausagi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ausagi.R
import com.example.ausagi.database.Profile
import com.example.ausagi.fragmentsinicio.HomeFragmentDirections

class ItemProfileAdapter(
        private val context: Context,
        private val listener: Communicator,
        private val dataset: MutableList<Profile>
) : RecyclerView.Adapter<ItemProfileAdapter.ItemViewHolder>() {

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]

        //Poner los datos guardados donde corresponde en el holder
        holder.imageView.setImageURI(item.imageResource)

        //pasa la posicion del adapter al recyclerview para conocer que item se ha elegido y acciona la navegación
        holder.imageButton.setOnClickListener {
            if (position != RecyclerView.NO_POSITION) {
                listener.passData(position)
            }
            val action = HomeFragmentDirections.actionHomeFragmentToInformationFragment()
            holder.itemView.findNavController().navigate(action)
        }
        holder.imageView.setOnClickListener {
            if (position != RecyclerView.NO_POSITION) {
                listener.passData(position)
            } //pasa la posicion del adapter al recyclerview para conocer que item se ha elegido
            val action = HomeFragmentDirections.actionHomeFragmentToBoardOneFragment()
            holder.itemView.findNavController().navigate(action)
        }
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.item_profile_view, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     */
    override fun getItemCount(): Int {
        return dataset.size
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a Profile object.
    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageViewFace)
        val imageButton: ImageButton = view.findViewById(R.id.boton_masinfo)

    }
}