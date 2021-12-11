package com.example.androidproject.ui.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.GlideLoader
import com.example.androidproject.R
import com.example.androidproject.model.Product
import com.example.androidproject.ui.home.ProductListAdapter
import kotlinx.android.synthetic.main.item_dashboard_layout.view.*
import kotlinx.android.synthetic.main.item_list_layout.view.*

class DashboardItemListAdapter(
    private val context: Context,
    private val list: ArrayList<Product>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProductListAdapter.MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_dashboard_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if(holder is ProductListAdapter.MyViewHolder){
            GlideLoader(context).loadProductPicture(model.image, holder.itemView.iv_dashboard_image)
            holder.itemView.tv_dashboard_item_name.text = model.title
            holder.itemView.tv_dashboard_item_price.text = "${model.price} dollars"
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
}