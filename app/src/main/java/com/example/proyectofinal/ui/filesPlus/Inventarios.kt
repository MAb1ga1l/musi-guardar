package com.example.proyectofinal.ui.filesPlus

class Inventarios {
    val inventarioTorso: MutableList<Prenda> = mutableListOf<Prenda>()

    fun agregaPrenda(prenda: Prenda, seccion:String){
        if(seccion=="Torso"){
            inventarioTorso.add(prenda)
        }
    }
}