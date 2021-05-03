package com.example.proyectofinal.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyectofinal.ui.filesPlus.Prenda

class HomeViewModel : ViewModel() {

    val inventarioTorso: MutableList<Prenda> = mutableListOf<Prenda>()

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun agregaCosa(prenda: Prenda){
        inventarioTorso.add(prenda)
    }
}