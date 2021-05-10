package com.example.proyectofinal.ui.home.secciones

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.R
import com.example.proyectofinal.ui.filesPlus.Prenda
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File


class Torso : Fragment() {

    val inventarioTorso: MutableList<Prenda> = mutableListOf<Prenda>()
    private lateinit var tableRecyclerViewDress: RecyclerView
    private lateinit var tableRecyclerViewShirt: RecyclerView
    private lateinit var archivoFoto: File
    private lateinit var imageButton: ImageButton

    private val db = Firebase.firestore
    val inventarioDress: MutableList<Prenda> = mutableListOf<Prenda>()
    val inventarioShirt: MutableList<Prenda> = mutableListOf<Prenda>()
    private val storage = Firebase.storage

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db.collection("Torso").whereEqualTo("tipo","dress").get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    val prendaParaAgregar = Prenda()
                    prendaParaAgregar.tipoPrenda = document.data.get("tipo") as String
                    prendaParaAgregar.idPrenda = document.id
                    prendaParaAgregar.ultimaFechaDeUso = document.data.get("ultmafechaUso") as String
                    prendaParaAgregar.Usos = document.data.get("usos") as ArrayList<String>
                    prendaParaAgregar.fechaDeCreacion= document.data.get("fechaUp") as String
                    inventarioDress.add(prendaParaAgregar)
                }
            }
        val prendaParaAgregar = Prenda()
        db.collection("Torso").whereEqualTo("tipo","shirt").get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    prendaParaAgregar.tipoPrenda = document.data.get("tipo") as String
                    prendaParaAgregar.idPrenda = document.id
                    prendaParaAgregar.ultimaFechaDeUso = document.data.get("ultmafechaUso") as String
                    prendaParaAgregar.Usos = document.data.get("usos") as ArrayList<String>
                    prendaParaAgregar.fechaDeCreacion= document.data.get("fechaUp") as String
                    Log.d("PIERNAS","pants: ${prendaParaAgregar.tipoPrenda}")
                    inventarioShirt.add(prendaParaAgregar)
                }
            }
    }

    @SuppressLint("WrongConstant")
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        if (inventarioDress.size == 0){
            inventarioDress.add(Prenda())
        }
        if (inventarioShirt.size ==0){
            inventarioShirt.add(Prenda())
        }
        val vista = inflater.inflate(R.layout.fragment_torso, container, false)
        tableRecyclerViewDress = vista.findViewById(R.id.recyclerView_dress) as RecyclerView
        tableRecyclerViewDress.layoutManager = LinearLayoutManager(context,OrientationHelper.HORIZONTAL,false)
        tableRecyclerViewDress.adapter = PrendasAdapter(inventarioDress)
        tableRecyclerViewShirt = vista.findViewById(R.id.recyclerView_shirt) as RecyclerView
        tableRecyclerViewShirt.layoutManager = LinearLayoutManager(context,OrientationHelper.HORIZONTAL,false)
        tableRecyclerViewShirt.adapter = PrendasAdapter(inventarioShirt)
        imageButton = vista.findViewById(R.id.buttonreloadTorso)
        return vista
    }

    private inner class PrendasAdapter(var inventarioT: List<Prenda>) : RecyclerView.Adapter<PrendaHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrendaHolder {
            val tarjeta = layoutInflater.inflate(R.layout.fragment_celda_prenda,parent,false)
            return PrendaHolder(tarjeta)
        }

        override fun onBindViewHolder(holder: PrendaHolder, position: Int) {
           val prenda = inventarioT[position]
            holder.holderBinding(prenda)
        }

        override fun getItemCount() = inventarioT.size
    }

    override fun onStart() {
        super.onStart()
        imageButton.setOnClickListener {
            db.collection("Torso").whereEqualTo("tipo","dress").get()
                .addOnSuccessListener { documents ->
                    for (document in documents){
                        val prendaParaAgregar = Prenda()
                        prendaParaAgregar.tipoPrenda = document.data.get("tipo") as String
                        prendaParaAgregar.idPrenda = document.id
                        prendaParaAgregar.ultimaFechaDeUso = document.data.get("ultmafechaUso") as String
                        prendaParaAgregar.Usos = document.data.get("usos") as ArrayList<String>
                        prendaParaAgregar.fechaDeCreacion= document.data.get("fechaUp") as String
                        inventarioDress.add(prendaParaAgregar)
                    }
                }
            val prendaParaAgregar = Prenda()
            db.collection("Torso").whereEqualTo("tipo","shirt").get()
                .addOnSuccessListener { documents ->
                    for (document in documents){
                        prendaParaAgregar.tipoPrenda = document.data.get("tipo") as String
                        prendaParaAgregar.idPrenda = document.id
                        prendaParaAgregar.ultimaFechaDeUso = document.data.get("ultmafechaUso") as String
                        prendaParaAgregar.Usos = document.data.get("usos") as ArrayList<String>
                        prendaParaAgregar.fechaDeCreacion= document.data.get("fechaUp") as String
                        Log.d("PIERNAS","pants: ${prendaParaAgregar.tipoPrenda}")
                        inventarioShirt.add(prendaParaAgregar)
                    }
                }
            tableRecyclerViewDress.adapter = PrendasAdapter(inventarioDress)
            tableRecyclerViewShirt.adapter = PrendasAdapter(inventarioShirt)

        }
    }

    private inner  class PrendaHolder(tarjeta:View): RecyclerView.ViewHolder(tarjeta), View.OnClickListener{
        private lateinit var prenda: Prenda
        val ultimoUsoTextView : TextView = itemView.findViewById(R.id.label_ultimo_uso)
        val numUsosTextView: TextView = itemView.findViewById(R.id.label_num_usos)
        val vistaFoto : ImageView = itemView.findViewById(R.id.prenda_imagen)

        init{
            itemView.setOnClickListener(this)
        }

        fun holderBinding(prenda: Prenda){
            this.prenda = prenda
            ultimoUsoTextView.text = prenda.ultimaFechaDeUso
            numUsosTextView.text = prenda.Usos.size.toString()
            //cargar imagen en file
            storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/musi-guardar-b8b4d.appspot.com/o/${prenda.idPrenda}.jpg")
                .downloadUrl.addOnSuccessListener {
                    // Got the download URL for 'users/me/profile.png'
                    archivoFoto = getArchivoFoto("${prenda.idPrenda}.jpg")
                    storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/musi-guardar-b8b4d.appspot.com/o/${prenda.idPrenda}.jpg")
                        .getFile(archivoFoto).addOnSuccessListener {
                            val bitmapDeFoto = BitmapFactory.decodeFile(archivoFoto.absolutePath)
                            vistaFoto.setImageBitmap(bitmapDeFoto)
                        }
                }.addOnFailureListener {
                    // Handle any errors
                    vistaFoto.setImageResource(R.drawable.ropados)
                }
        }

        override fun onClick(v: View?) {
            //salrdan las opciones para eliminar o seleccionar como outfit diario
        }
        fun getArchivoFoto(nombreDeArchivo: String): File{
            val rutaDeArchivo = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File(rutaDeArchivo,nombreDeArchivo)
        }
    }
}