package com.elham.shoppingproject.service

import com.elham.shoppingproject.model.Product

interface OnRecyclerViewItemClicked {
    fun onProductClicked(position:Int,product: Product)
}