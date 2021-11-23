package com.elham.shoppingproject.service

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.elham.shoppingproject.views.HomeFragment
import ss.com.bannerslider.ImageLoadingService

class ImageLoadingService(var context: HomeFragment): ImageLoadingService {
    override fun loadImage(url: String?, imageView: ImageView?) {
        Glide.with(context).load(url).into(imageView!!)
    }

    override fun loadImage(resource: Int, imageView: ImageView?) {
        Glide.with(context).load(resource).into(imageView!!)
    }

    override fun loadImage(
        url: String?,
        placeHolder: Int,
        errorDrawable: Int,
        imageView: ImageView?
    ) {
        Glide.with(context).load(url).placeholder(placeHolder).error(errorDrawable).into(imageView!!)
    }
}