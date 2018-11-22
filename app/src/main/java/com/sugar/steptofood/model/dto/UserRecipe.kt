package com.sugar.steptofood.model.dto

import android.arch.persistence.room.*

@Entity(indices = [Index(value = ["recipeId", "userId", "type"], unique = true)])
data class UserRecipe(
        @PrimaryKey(autoGenerate = true)
        override var id: Int? = null,

        @ForeignKey(entity = Recipe::class, parentColumns = ["id"], childColumns = ["recipeId"], onDelete = ForeignKey.CASCADE)
        var recipeId: Int? = null,

        @ForeignKey(entity = Product::class, parentColumns = ["id"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE)
        var userId: Int? = null,

        var type: String? = null
) : EntityDto