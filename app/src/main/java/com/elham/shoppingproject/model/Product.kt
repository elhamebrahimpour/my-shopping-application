package com.elham.shoppingproject.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.util.*
import kotlin.math.absoluteValue

@Parcelize
@Entity
data class Product(@ColumnInfo(name = "product_title") var title:String,
                   @ColumnInfo(name = "product_imageUrl") var imageUrl:String,
                   @ColumnInfo(name = "product_seasonSalePrice") var seasonSalePrice:Double):Parcelable{
    @IgnoredOnParcel
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "product_id")
    var id:String = UUID.randomUUID().toString()
    @IgnoredOnParcel
    var count = 0

    @ColumnInfo(name = "product_bestSalePrice")
    var bestSalePrice: Double? =null

    constructor(title: String, imageUrl: String, seasonSalePrice: Double, bestSalePrice: Double
    ):this(title,imageUrl, seasonSalePrice){
        this.bestSalePrice = bestSalePrice
    }
}