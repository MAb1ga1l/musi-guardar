package com.example.proyectofinal.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.proyectofinal.R
import com.example.proyectofinal.ui.home.secciones.PiernasFragment
import com.example.proyectofinal.ui.home.secciones.PiesFragment
import com.example.proyectofinal.ui.home.secciones.Torso
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var textView: TextView
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        tabLayout = root.findViewById(R.id.tabLayout)
        viewPager = root.findViewById(R.id.view_pager)

        //Para cargar los fragmentos y los titulos en el TabLayout
        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager)

        viewPagerAdapter.addFragment(Torso(),"Torso")
        viewPagerAdapter.addFragment(PiernasFragment(),"Piernas")
        viewPagerAdapter.addFragment(PiesFragment(),"Pies")

        viewPager.adapter = viewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)

        return root
    }

    internal class ViewPagerAdapter(fragmentManager: FragmentManager):FragmentPagerAdapter(fragmentManager){

        private val fragments:ArrayList<Fragment> = ArrayList<Fragment>()
        private val titles:ArrayList<String> = ArrayList<String>()

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        fun addFragment(fragment: Fragment, title:String){
            fragments.add(fragment)
            titles.add(title)
        }

        override fun getPageTitle(i: Int): CharSequence? {
            return titles[i]
        }

    }

}