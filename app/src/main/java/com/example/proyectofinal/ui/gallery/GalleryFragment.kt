package com.example.proyectofinal.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal.R

class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel
    private lateinit var imageViewTorso: ImageView
    private lateinit var imageViewPiernas: ImageView
    private lateinit var imageViewPies: ImageView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        galleryViewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)
        imageViewTorso=root.findViewById(R.id.image_outfit_torso)
        imageViewPiernas=root.findViewById(R.id.image_outfit_piernas)
        imageViewPies=root.findViewById(R.id.image_outfit_pies)
        imageViewTorso.setImageResource(R.drawable.ropados)
        imageViewPiernas.setImageResource(R.drawable.ropados)
        imageViewPies.setImageResource(R.drawable.ropados)

        return root
    }
}