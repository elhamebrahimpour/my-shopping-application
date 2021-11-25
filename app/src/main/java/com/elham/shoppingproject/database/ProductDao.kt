package com.elham.shoppingproject.database

import androidx.room.*
import com.elham.shoppingproject.model.Product

@Dao
interface ProductDao {
    @Query("select * from Product")
    fun getAllProduct():List<Product>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(product: Product)

    @Update
    fun updateProduct(product:Product)

    @Delete
    fun deleteProduct(product: Product)

    @Query("select sum(count) from Product")
    fun getSum(): Int
}