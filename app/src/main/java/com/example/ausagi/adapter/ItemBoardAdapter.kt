package com.example.ausagi.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
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
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        var pressed = 0

        //Poner los datos guardados donde corresponde en el holder
        holder.imageView.setImageURI(item.imageResource)
        holder.textView.text = item.textResource

        //Cambiar los colores de las tarjetas según lo que sean
        if (!item.isCategory && !item.isRoutine) {
            holder.imageView.setBackgroundColor(Color.WHITE)
        }
        if (item.isCategory) {
            holder.recubrimiento.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP)
            holder.imageView.setBackgroundColor(Color.WHITE)
        }
        if (item.isRoutine) {
            holder.recubrimiento.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP)
            holder.imageView.setBackgroundColor(Color.WHITE)
        }

        //Pasar la posicion del adapter al recyclerview para conocer qué item se ha elegido
        holder.imageView.setOnClickListener {
            if (position != RecyclerView.NO_POSITION) {
                listener.addPictoBarra(position)
                listener.passData(position)

                pressed++
                listener.passClicked(pressed)
            }
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
    // Each data item is just a ItemBoard object.
    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.item_board_picto_imagen)
        val textView: TextView = view.findViewById(R.id.item_board_nombre_picto)
        val recubrimiento: FrameLayout = view.findViewById(R.id.frameLayout)
    }
}