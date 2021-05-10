package com.example.proyectofinal.ui.agregadoPrenda

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import com.example.proyectofinal.R
import com.example.proyectofinal.ui.filesPlus.Inventarios
import com.example.proyectofinal.ui.filesPlus.Prenda
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File

class AgregarPrendaFragment : Fragment() {

    private lateinit var prenda: Prenda
    private lateinit var buttonOk: Button
    private lateinit var buttonCancel: Button
    private lateinit var imageView: ImageView
    private lateinit var imageButton: ImageButton
    private lateinit var archivoFoto: File
    private lateinit var labelSeccion :TextView
    private lateinit var labelTipoPrenda :TextView
    private lateinit var spinnerSeccion: Spinner
    private lateinit var spinnerTipo: Spinner
    private  var listaSeccion= listOf("Torso","Piernas","Pies")
    private var listaTipo = listOf("dress","pants","shirt","shoes","shorts")
    private val db = Firebase.firestore
    private val storage = Firebase.storage
    private var mountainsRef =storage.reference


    private var flagOk : Boolean = false

    companion object {
        fun newInstance() = AgregarPrendaFragment()
    }

    private lateinit var viewModel: AgregarPrendaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prenda = Prenda()
        //Log.d("AGREGAR PRENDA","Se recibió ${prenda.fechaDeCreacion} id: ${prenda.idPrenda}")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vista = inflater.inflate(R.layout.agregar_prenda_fragment, container, false)

        buttonOk = vista.findViewById(R.id.button_ok)
        buttonCancel = vista.findViewById(R.id.buttonCancel)
        imageView = vista.findViewById(R.id.fotoPrenda)
        imageButton = vista.findViewById(R.id.button_foto)
        labelSeccion = vista.findViewById(R.id.label_seccion)
        labelTipoPrenda = vista.findViewById(R.id.label_tipo_prenda)
        archivoFoto = File(context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "${prenda.idPrenda}.jpg")
        labelSeccion.text = "-----"
        labelTipoPrenda.text = "-----"
        spinnerTipo = vista.findViewById(R.id.spinnerTipo)
        spinnerSeccion = vista.findViewById(R.id.spinnerSeccion)

        val adaptadorSeccion = context?.let { ArrayAdapter(it,android.R.layout.simple_spinner_item,listaSeccion) }
        val adaptadorTipo = context?.let { ArrayAdapter(it,android.R.layout.simple_spinner_dropdown_item,listaTipo) }
        spinnerSeccion.adapter = adaptadorSeccion
        spinnerTipo.adapter = adaptadorTipo

        //foto predeterminada
        imageView.setImageResource(R.drawable.ropa)

        return vista
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AgregarPrendaViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        //Interacción para tomar la foto
        imageButton.apply {
            setOnClickListener {
                val intentFoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                archivoFoto = getArchivoFoto("fotoTemporal.jpg")
                val fileProvider = FileProvider.getUriForFile(context,"com.example.proyectofinal.fileprovider",archivoFoto)
                intentFoto.putExtra(MediaStore.EXTRA_OUTPUT,fileProvider)
                try {
                    startActivityForResult(intentFoto, 1)
                }catch (e: ActivityNotFoundException){
                }
            }
        }

        buttonCancel.setOnClickListener {
            if(archivoFoto.exists()){
                archivoFoto.delete()
                imageView.setImageResource(R.drawable.ropa)
            }
            labelSeccion.text = "-----"
            labelTipoPrenda.text = "-----"
        }

        buttonOk.setOnClickListener {
            flagOk = true
            //Para asegurar que exista una foto para guardar
            if(archivoFoto.exists()){
                //Aqui justamente es donde llamaramiamos a python para q nos diga en que sección y que tipo de prenda es
                prenda.seccionPrenda = labelSeccion.text.toString()
                prenda.tipoPrenda = labelTipoPrenda.text.toString()
                //Guardado en Firebase
                val prendanueva = hashMapOf(
                    "tipo" to prenda.tipoPrenda,
                    "fechaUp" to prenda.fechaDeCreacion,
                    "ultmafechaUso" to prenda.ultimaFechaDeUso,
                    "seccion" to prenda.seccionPrenda,
                    "usos" to prenda.Usos
                )

                db.collection(labelSeccion.text.toString()).document(prenda.idPrenda)
                    .set(prendanueva)
                    .addOnSuccessListener { Log.d("PRENDAN", "DocumentSnapshot successfully written!") }
                    .addOnFailureListener { e -> Log.w("PRENDAN", "Error writing document", e) }
                //subir foto a firebas
                // Get the data from an ImageView as bytes
                imageView.isDrawingCacheEnabled = true
                imageView.buildDrawingCache()
                val bitmap = (imageView.drawable as BitmapDrawable).bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                mountainsRef = storage.reference.child("${prenda.idPrenda}.jpg")

                var uploadTask = mountainsRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    // Handle unsuccessful uploads
                }.addOnSuccessListener { taskSnapshot ->
                    // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                    // ...
                }
                //-----
                view?.let { it1 ->
                    Snackbar.make(it1, "Tu Prenda se ha guardado. Puedes agregar más", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                }

                prenda = Prenda()
                labelSeccion.text = "-----"
                labelTipoPrenda.text = "-----"
                imageView.setImageResource(R.drawable.ropa)

            }else{
                view?.let { it1 ->
                    Snackbar.make(it1, "Falta foto para guardar", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                }
            }
        }

        spinnerSeccion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                labelSeccion.text = listaSeccion[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }

        spinnerTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                labelTipoPrenda.text = listaTipo[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }

    }

    fun getArchivoFoto(nombreDeArchivo: String): File{
        val rutaDeArchivo = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(rutaDeArchivo,nombreDeArchivo)
    }


    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            val bitmapDeFoto = BitmapFactory.decodeFile(archivoFoto.absolutePath)
            labelSeccion.text = "Torso"
            labelTipoPrenda.text = "dress"
            imageView.setImageBitmap(bitmapDeFoto)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
            //en caso de que se tome a foto pero no se termine el proceso ya sea con Ok o Cancelar
            if(archivoFoto.exists()){
                archivoFoto.delete()
            }
    }

}