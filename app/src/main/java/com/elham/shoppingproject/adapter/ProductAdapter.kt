package com.elham.shoppingproject.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elham.shoppingproject.R
import com.elham.shoppingproject.database.Database
import com.elham.shoppingproject.model.Product
import com.elham.shoppingproject.service.AddProduct
import com.elham.shoppingproject.service.OnAdapterUpdate
import com.elham.shoppingproject.service.OnRecyclerViewItemClicked
import org.jetbrains.annotations.NotNull
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class ProductAdapter(var productList: MutableList<Product>, var context: Context,
                     var addProduct: AddProduct)
    : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    var onRecyclerViewItemClicked: OnRecyclerViewItemClicked? =null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.season_bestsale_item,parent,false))
    }
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product:Product= productList[position]
        holder.imgProduct?.let { Glide.with(context).load(product.imageUrl).into(it) }
        holder.txtProductTitle!!.text = product.title
        holder.txtSeasonSalePrice!!.text= product.seasonSalePrice.toString()
        holder.btnAddToBasket?.setOnClickListener {
            product.count=product.count + 1
            val productDataBase: Database = Database.getInstance(context)
            val executor: Executor = Executors.newSingleThreadExecutor()
            executor.execute {
                productDataBase.productDao().insertProduct(product)
            }
            addProduct.addNewProduct()
        }
        holder.itemView.setOnClickListener{
            onRecyclerViewItemClicked?.onProductClicked(position,product)
        }
    }
    override fun getItemCount(): Int {
        return productList.size
    }
    //inner class viewHolder:
    class ProductViewHolder(@NotNull itemView: View) : RecyclerView.ViewHolder(itemView){
        var imgProduct: ImageView?=null
        var txtProductTitle: TextView?=null
        var txtSeasonSalePrice: TextView?=null
        var btnAddToBasket: Button?=null
        init {
            this.imgProduct= itemView.findViewById(R.id.imgProductSeasonBestSale)
            this.txtProductTitle= itemView.findViewById(R.id.txtProductTitle)
            this.txtSeasonSalePrice= itemView.findViewById(R.id.txtSeasonSalePrice)
            this.btnAddToBasket=itemView.findViewById(R.id.btnAddToBasket)
        }
    }
    fun setRecyclerViewItemClicked(onRecyclerViewItemClicked: OnRecyclerViewItemClicked){
        this.onRecyclerViewItemClicked=onRecyclerViewItemClicked
    }
}