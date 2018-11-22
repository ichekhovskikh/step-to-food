package com.sugar.steptofood.model.dto

import android.arch.persistence.room.*
import android.arch.persistence.room.ForeignKey.CASCADE

@Entity(indices = [Index(value = ["recipeId", "productId"], unique = true)])
data class ProductRecipe(
        @PrimaryKey(autoGenerate = true)
        override var id: Int? = null,

        @ForeignKey(entity = Recipe::class, parentColumns = ["id"], childColumns = ["recipeId"], onDelete = CASCADE)
        var recipeId: Int? = null,

        @ForeignKey(entity = Product::class, parentColumns = ["id"], childColumns = ["productId"], onDelete = CASCADE)
        var productId: Int? = null,

        var weight: Int? = null
) : EntityDto