package com.example.ausagi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ausagi.adapter.ItemProfileAdapter
import com.example.ausagi.data.Datasource
import com.example.ausagi.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {

        val recyclerView = binding.recyclerView
        val myDataset = Datasource().loadProfiles()

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            // set the custom adapter to the RecyclerView
            adapter = ItemProfileAdapter(context, myDataset)
        }

    }
}

