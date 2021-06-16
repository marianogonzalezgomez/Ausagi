package com.example.ausagi.adapter

interface Communicator {
    fun passData(position: Int)
    fun addPictoBarra(position: Int)
    fun passClicked(pressed: Int)
    fun passClickedConfig(pressedConfig: Int)
    fun passClickedElim(pressedElim: Int)
}