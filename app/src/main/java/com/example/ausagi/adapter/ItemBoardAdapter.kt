package com.example.ausagi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ausagi.R
import com.example.ausagi.database.Picto

class ItemBoardAdapter(
    private val context: Context,
    private val dataset: MutableList<Picto>
) : RecyclerView.Adapter<ItemBoardAdapter.ItemViewHolder>() {

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]

        holder.imageView.setImageURI(item.imageResource)
        holder.textView.text = item.textResource

        /*holder.imageButton.setOnClickListener{
            if (position != RecyclerView.NO_POSITION) { listener.passData(position) } //pasa la posicion del adapter al recyclerview para conocer que item se ha elegido
            val action = HomeFragmentDirections.actionHomeFragmentToInformationFragment()
            holder.itemView.findNavController().navigate(action)
        }*/

    }

    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.item_picto_view, parent, false)

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
    inner class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.item_board_picto_imagen)
        val textView: TextView = view.findViewById(R.id.item_board_nombre_picto)

    }
}