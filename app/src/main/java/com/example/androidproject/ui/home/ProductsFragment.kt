package com.example.androidproject.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidproject.AddProductActivity
import com.example.androidproject.R
import com.example.androidproject.SettingsActivity
import com.example.androidproject.databinding.FragmentProductsBinding
import com.example.androidproject.firestore.FirestoreClass
import com.example.androidproject.model.Product
import kotlinx.android.synthetic.main.fragment_products.*

class ProductsFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentProductsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }
    fun successProductsList(productList: ArrayList<Product>){
        if(productList.size > 0){
            rv_product_items.visibility = View.VISIBLE
            //tv_no_products.visibility = View.GONE

            rv_product_items.layoutManager = LinearLayoutManager(activity)
            rv_product_items.setHasFixedSize(true)
            val adapterProducts = ProductListAdapter(requireActivity(), productList)
            rv_product_items.adapter = adapterProducts
        }
    }
    private fun getProductListFromFirestore(){
        FirestoreClass().getProductsList(this)
    }

    override fun onResume() {
        super.onResume()
        getProductListFromFirestore()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_product_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id){
            R.id.action_add_product -> {
                startActivity(Intent(activity, AddProductActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}