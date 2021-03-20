package com.example.ausagi.data

import com.example.ausagi.R
import com.example.ausagi.model.Profile


class Datasource {

    fun loadProfiles(): MutableList<Profile> {
        return mutableListOf<Profile>(
            Profile(R.drawable.kid1),
            Profile(R.drawable.kid2)
        )
    }
}