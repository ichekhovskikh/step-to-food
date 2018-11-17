package com.sugar.steptofood.model

import android.arch.persistence.room.*
import android.graphics.Bitmap
import com.google.gson.annotations.Expose

@Entity
data class Recipe(
        @PrimaryKey(autoGenerate = false)
        override var id: Int? = null,

        var name: String = "",

        var image: String? = "",

        var description: String = "",

        var calorie: Double? = null,

        var protein: Double? = null,

        var fat: Double? = null,

        var carbohydrates: Double? = null,

        @ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["authorId"], onDelete = ForeignKey.CASCADE)
        var authorId: Int? = null,

        @Ignore
        var author: User? = null,

        @Ignore
        @Expose
        var products: List<Product>? = mutableListOf(),

        @Ignore
        @Expose
        var hasYourLike: Boolean = false,

        @Ignore
        @Expose
        var bitmap: Bitmap? = null
) : EntityDto