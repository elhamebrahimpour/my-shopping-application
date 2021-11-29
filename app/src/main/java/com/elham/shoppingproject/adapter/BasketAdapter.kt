package com.elham.shoppingproject.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elham.shoppingproject.R
import com.elham.shoppingproject.database.Database
import com.elham.shoppingproject.model.Product
import com.elham.shoppingproject.service.OnAdapterUpdate
import com.elham.shoppingproject.service.OnRecyclerViewItemClicked
import org.jetbrains.annotations.NotNull
import java.lang.Exception
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class BasketAdapter (var productList: MutableList<Product>, var context: Context):
    RecyclerView.Adapter<BasketAdapter.BasketViewHolder>(){
    private var onRecyclerViewItemClicked: OnRecyclerViewItemClicked? =null
    private var executor: Executor? = Executors.newSingleThreadExecutor()
    var productDatabase: Database = Database.getInstance(context)
    private lateinit var onAdapterUpdate: OnAdapterUpdate

    @SuppressLint("NotifyDataSetChanged")
    fun setOnAdapterUpdate(onAdapterUpdate: OnAdapterUpdate) {
        this.onAdapterUpdate = onAdapterUpdate
        this.productList
        notifyDataSetChanged()
    }
    @NonNull
    @NotNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
        return BasketViewHolder(LayoutInflater.from(context).inflate(R.layout.basket_item,parent,false))
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BasketViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val product:Product= productList[position]
        holder.imgBasket.let { Glide.with(context).load(product.imageUrl).into(it!!) }
        holder.txtTitleBasket!!.text=product.title
        holder.txtPriceBasket!!.text= product.seasonSalePrice.toString()
        holder.txtBestPriceBasket!!.text=product.bestSalePrice.toString()
        holder.textViewCounter!!.text=product.count.toString()

        holder.imageViewAdd!!.setOnClickListener {
            AddProductCount(productList[holder.absoluteAdapterPosition])
        }
        holder.imageViewMin!!.setOnClickListener {
            MinProductCount(productList[holder.absoluteAdapterPosition])
        }
        holder.imgDelete!!.setOnClickListener {
            DeleteProduct(productList[holder.absoluteAdapterPosition])
        }
        holder.itemView.setOnClickListener {
            onRecyclerViewItemClicked!!.onProductClicked(position,product)
        }
    }
    override fun getItemCount(): Int {
        return productList.size
    }

    class BasketViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var imgBasket: ImageView?=null
        var txtTitleBasket: TextView?=null
        var txtPriceBasket: TextView?=null
        var txtBestPriceBasket: TextView?=null
        var textViewCounter: TextView?=null
        var imageViewAdd: ImageView?=null
        var imageViewMin: ImageView?=null
        var imgDelete:ImageView?=null
        init {
            imgBasket=itemView.findViewById(R.id.imgBasket)
            txtTitleBasket=itemView.findViewById(R.id.txtTitleBasket)
            txtPriceBasket=itemView.findViewById(R.id.txtPriceBasket)
            txtBestPriceBasket=itemView.findViewById(R.id.txtBestPriceBasket)
            textViewCounter=itemView.findViewById(R.id.textViewCounter)
            imageViewAdd=itemView.findViewById(R.id.imageViewAdd)
            imageViewMin=itemView.findViewById(R.id.imageViewMin)
            imgDelete=itemView.findViewById(R.id.imgDelete)
        }
    }
    private fun AddProductCount(product: Product) {
        product.count=product.count + 1
        executor!!.execute { productDatabase.productDao().insertProduct(product)
        }
        onAdapterUpdate.onAdapterUpdate()
    }
    private fun MinProductCount(product: Product) {
        if (product.count > 1) {
            product.count=product.count - 1
            executor!!.execute { productDatabase.productDao().insertProduct(product)
            }
            onAdapterUpdate.onAdapterUpdate()
        }
    }
    private fun DeleteProduct(product: Product) {
        executor!!.execute {
            try {
                productDatabase.productDao().deleteProduct(product)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        productList.remove(product)
        onAdapterUpdate.onAdapterUpdate()
    }
    fun setRecyclerViewItemClicked(onRecyclerViewItemClicked: OnRecyclerViewItemClicked){
        this.onRecyclerViewItemClicked=onRecyclerViewItemClicked
    }
}
