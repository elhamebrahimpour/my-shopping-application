package com.elham.shoppingproject.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elham.shoppingproject.R
import com.elham.shoppingproject.database.Database
import com.elham.shoppingproject.model.Product
import com.elham.shoppingproject.service.AddProduct
import com.elham.shoppingproject.service.OnRecyclerViewItemClicked
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class ProductBestSaleAdapter(var productList:MutableList<Product>, var context: Context, var addProduct: AddProduct):
    RecyclerView.Adapter<ProductBestSaleAdapter.BestSaleViewHolder>() {
    var onRecyclerViewItemClicked: OnRecyclerViewItemClicked? =null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestSaleViewHolder {
        return BestSaleViewHolder(LayoutInflater.from(context).inflate(R.layout.bestsale_item,parent,false))
    }

    override fun onBindViewHolder(holder: BestSaleViewHolder, position: Int) {
        val product:Product=productList[position]
        holder.imgProductBestSale?.let { Glide.with(context).load(product.imageUrl).into(it) }
        holder.txtBestProductTitle!!.text = product.title
        holder.txtPreviousPrice!!.text= product.seasonSalePrice.toString()
        holder.txtPreviousPrice!!.paintFlags= Paint.STRIKE_THRU_TEXT_FLAG
        holder.txtBestPrice!!.text=product.bestSalePrice.toString()

        holder.itemView.setOnClickListener{
            onRecyclerViewItemClicked?.onProductClicked(position,product)
        }
        holder.imgAddToBasket!!.setOnClickListener {
            product.count=product.count + 1
            val productDataBase: Database = Database.getInstance(context)
            val executor: Executor = Executors.newSingleThreadExecutor()
            executor.execute {
                productDataBase.productDao().insertProduct(product)
            }
            addProduct.addNewProduct()
        }
    }

    override fun getItemCount(): Int {  return productList.size
    }
    //inner class viewHolder:
    class BestSaleViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var imgProductBestSale: ImageView?=null
        var txtBestProductTitle: TextView?=null
        var txtPreviousPrice: TextView?=null
        var txtBestPrice: TextView?=null
        var imgAddToBasket: ImageView?=null
        init {
            this.imgProductBestSale= itemView.findViewById<ImageView>(R.id.imgProductBestSale)
            this.txtBestProductTitle= itemView.findViewById<TextView>(R.id.txtBestProductTitle)
            this.txtPreviousPrice= itemView.findViewById<TextView>(R.id.txtPreviousPrice)
            this.txtBestPrice= itemView.findViewById<TextView>(R.id.txtBestPrice)
            this.imgAddToBasket=itemView.findViewById(R.id.imgAddToBasket)
        }
    }
    fun setRecyclerViewItemClicked(onRecyclerViewItemClicked: OnRecyclerViewItemClicked){
        this.onRecyclerViewItemClicked=onRecyclerViewItemClicked
    }
}