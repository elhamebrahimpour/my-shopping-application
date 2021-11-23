package com.elham.shoppingproject.adapter

import ss.com.bannerslider.adapters.SliderAdapter
import ss.com.bannerslider.viewholder.ImageSlideViewHolder

class BannerSliderAdapter:SliderAdapter() {
    override fun getItemCount(): Int {
        return 4
    }

    override fun onBindImageSlide(position: Int, imageSlideViewHolder: ImageSlideViewHolder?) {
        when(position){
            0 -> imageSlideViewHolder!!.bindImageSlide(
                "https://media.istockphoto.com/photos/waiting-for-his-opportunity-to-swing-picture-id1268743446?b=1&k=20&m=1268743446&s=170667a&w=0&h=GIYiwknqttyEkPKZu6SAmh47LkkSloDwagaUX1xTdt0=")
            1 -> imageSlideViewHolder!!.bindImageSlide(
                "https://images.unsplash.com/photo-1476480862126-209bfaa8edc8?ixid=MnwxMjA3fDB8MHxzZWFyY2h8NHx8c3BvcnR8ZW58MHx8MHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60")
            2 -> imageSlideViewHolder!!.bindImageSlide(
                "https://image.shutterstock.com/image-photo/kielce-poland-20210520-shop-go-600w-1977157430.jpg")
            3 -> imageSlideViewHolder!!.bindImageSlide("https://cdn.dribbble.com/users/3369843/screenshots/7814826/media/81e7aeff79df510150331d7c3d50027a.jpg?compress=1&resize=800x600")
        }
    }
}