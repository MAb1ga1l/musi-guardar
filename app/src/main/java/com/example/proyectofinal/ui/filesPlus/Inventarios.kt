package com.example.proyectofinal.ui.filesPlus

import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.IOException

class Inventarios {
    val inventarioShorts: MutableList<Prenda> = mutableListOf<Prenda>()
    val inventarioPants: MutableList<Prenda> = mutableListOf<Prenda>()
    private val db = Firebase.firestore

    public  fun  DataTronco(){
        db.collection("Piernas").whereEqualTo("tipo","pants").get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    val prendaParaAgregar = Prenda()
                    prendaParaAgregar.tipoPrenda = document.data.get("tipo") as String
                    prendaParaAgregar.idPrenda = document.id
                    prendaParaAgregar.ultimaFechaDeUso = document.data.get("ultmafechaUso") as String
                    prendaParaAgregar.Usos = document.data.get("usos") as ArrayList<String>
                    prendaParaAgregar.fechaDeCreacion= document.data.get("fechaUp") as String
                    inventarioPants.add(prendaParaAgregar)
                }
            }
        val prendaParaAgregar = Prenda()
        db.collection("Piernas").whereEqualTo("tipo","shorts").get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    prendaParaAgregar.tipoPrenda = document.data.get("tipo") as String
                    prendaParaAgregar.idPrenda = document.id
                    prendaParaAgregar.ultimaFechaDeUso = document.data.get("ultmafechaUso") as String
                    prendaParaAgregar.Usos = document.data.get("usos") as ArrayList<String>
                    prendaParaAgregar.fechaDeCreacion= document.data.get("fechaUp") as String
                    Log.d("PIERNAS","pants: ${prendaParaAgregar.tipoPrenda}")
                    inventarioShorts.add(prendaParaAgregar)
                }
            }

    }
}