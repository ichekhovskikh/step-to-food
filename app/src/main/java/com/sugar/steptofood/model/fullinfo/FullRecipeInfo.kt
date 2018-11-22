package com.sugar.steptofood.model.fullinfo

import android.arch.persistence.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.sugar.steptofood.model.dto.*

@Entity
data class FullRecipeInfo(
        @ColumnInfo(name = "recipe_id") override var id: Int? = null,
        @ColumnInfo(name = "recipe_name") var name: String = "",
        var image: String? = "",
        var description: String = "",
        var calorie: Double? = null,
        var protein: Double? = null,
        var fat: Double? = null,
        var carbohydrates: Double? = null,
        @Embedded var author: User? = null,
        @Expose var products: List<FullProductInfo>? = mutableListOf(),
        @Expose
        @SerializedName("hasYourLike")
        var hasSessionUserLike: Boolean = false
) : EntityDto