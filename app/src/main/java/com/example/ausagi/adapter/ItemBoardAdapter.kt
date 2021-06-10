package com.example.ausagi.adapter

import android.annotation.SuppressLint
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
        private val listener: Communicator,
        private val dataset: MutableList<Picto>
) : RecyclerView.Adapter<ItemBoardAdapter.ItemViewHolder>() {

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        var pressed = 0

        holder.imageView.setImageURI(item.imageResource)
        holder.textView.text = item.textResource

        if (item.isCategory) {
            holder.imageView.setBackgroundColor(R.color.black)
        }

        holder.imageView.setOnClickListener {
            if (position != RecyclerView.NO_POSITION) {
                listener.addPictoBarra(position)
                listener.passData(position)

                pressed++
                listener.passClicked(pressed)
            } //Pasar la posicion del adapter al recyclerview para conocer qué item se ha elegido
        }

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
    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.item_board_picto_imagen)
        val textView: TextView = view.findViewById(R.id.item_board_nombre_picto)

    }
}