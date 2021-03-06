package com.example.proyectofinal.ui.home.secciones

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.R
import com.example.proyectofinal.ui.filesPlus.Prenda
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File


class PiesFragment : Fragment() {

    //Adaptar al inventario de la sección a traves de firebase
    private lateinit var archivoFoto: File
    private lateinit var tableRecyclerViewShoes: RecyclerView
    private lateinit var imageButton: ImageButton
    private val db = Firebase.firestore
    val inventarioShoes: MutableList<Prenda> = mutableListOf<Prenda>()

    private val storage = Firebase.storage
    override fun onAttach(context: Context) {
        super.onAttach(context)
        db.collection("Pies").whereEqualTo("tipo","shoes").get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    val prendaParaAgregar = Prenda()
                    prendaParaAgregar.tipoPrenda = document.data.get("tipo") as String
                    prendaParaAgregar.idPrenda = document.id
                    prendaParaAgregar.ultimaFechaDeUso = document.data.get("ultmafechaUso") as String
                    prendaParaAgregar.Usos = document.data.get("usos") as ArrayList<String>
                    prendaParaAgregar.fechaDeCreacion= document.data.get("fechaUp") as String
                    inventarioShoes.add(prendaParaAgregar)
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
        if (inventarioShoes.size == 0){
            inventarioShoes.add(Prenda())
        }

        val vista = inflater.inflate(R.layout.fragment_pies, container, false)
        tableRecyclerViewShoes = vista.findViewById(R.id.recyclerView_shoes) as RecyclerView
        tableRecyclerViewShoes.layoutManager = LinearLayoutManager(context,
            OrientationHelper.HORIZONTAL,false)
        imageButton = vista.findViewById(R.id.buttonreloadPies)
        tableRecyclerViewShoes.adapter = PrendasAdapter(inventarioShoes)
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
            db.collection("Pies").whereEqualTo("tipo","shoes").get()
                .addOnSuccessListener { documents ->
                    for (document in documents){
                        val prendaParaAgregar = Prenda()
                        prendaParaAgregar.tipoPrenda = document.data.get("tipo") as String
                        prendaParaAgregar.idPrenda = document.id
                        prendaParaAgregar.ultimaFechaDeUso = document.data.get("ultmafechaUso") as String
                        prendaParaAgregar.Usos = document.data.get("usos") as ArrayList<String>
                        prendaParaAgregar.fechaDeCreacion= document.data.get("fechaUp") as String
                        inventarioShoes.add(prendaParaAgregar)
                    }
                }

            tableRecyclerViewShoes.adapter = PrendasAdapter(inventarioShoes)
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
                    vistaFoto.setImageResource(R.drawable.zapatos)
                }
        }

        fun getArchivoFoto(nombreDeArchivo: String): File{
            val rutaDeArchivo = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File(rutaDeArchivo,nombreDeArchivo)
        }

        override fun onClick(v: View?) {
            //salrdan las opciones para eliminar o seleccionar como outfit diario
        }
    }
}