package com.sugar.steptofood.model

import android.arch.persistence.room.*
import android.arch.persistence.room.ForeignKey.CASCADE

@Entity
data class ProductRecipe(
        @PrimaryKey(autoGenerate = true)
        override var id: Int? = null,

        @ForeignKey(entity = Recipe::class, parentColumns = ["id"], childColumns = ["recipeId"], onDelete = CASCADE)
        var recipeId: Int? = null,

        @ForeignKey(entity = Product::class, parentColumns = ["id"], childColumns = ["productId"], onDelete = CASCADE)
        var productId: Int? = null,

        var weight: Int? = null
) : EntityDto