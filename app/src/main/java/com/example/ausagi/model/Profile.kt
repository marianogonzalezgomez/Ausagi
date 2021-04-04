package com.example.ausagi.model

import android.net.Uri

//data antes del class
data class Profile(
   // @DrawableRes val imageResourceId: Int
    var imageResource: Uri?,
    var name: String,
    var comment: String
)