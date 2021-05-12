package com.example.ausagi.database

import android.net.Uri

data class Picto(
        var imageResource: Uri?,
        var textResource: String,
        var level1: Boolean,
        var level2: Boolean,
        var level3: Boolean,
        var isCategory: Boolean = false
)