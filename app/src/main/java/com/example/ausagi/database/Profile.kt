package com.example.ausagi.database

import android.net.Uri

data class Profile(
        var imageResource: Uri?,
        var name: String,
        var level: String,
        var comment: String,
        var colour: String
)