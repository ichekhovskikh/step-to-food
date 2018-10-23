package com.sugar.steptofood.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "food")
data class Food(
        @DatabaseField(columnName = "id", id = true)
        @SerializedName("id")
        override var id: Int? = null,

        @DatabaseField(columnName = "name", canBeNull = false)
        @SerializedName("name")
        var name: String = "",

        @DatabaseField(columnName = "image", canBeNull = false)
        @SerializedName("image")
        var image: String = "",

        @DatabaseField(columnName = "description", canBeNull = false)
        @SerializedName("description")
        var description: String = "",

        @DatabaseField(columnName = "calorie", canBeNull = false)
        @SerializedName("calorie")
        var calorie: Double? = null,

        @DatabaseField(columnName = "image", canBeNull = false)
        @SerializedName("protein")
        var protein: Double? = null,

        @DatabaseField(columnName = "fat", canBeNull = false)
        @SerializedName("fat")
        var fat: Double? = null,

        @DatabaseField(columnName = "carbohydrates", canBeNull = false)
        @SerializedName("carbohydrates")
        var carbohydrates: Double? = null,

        @DatabaseField(foreign = true)
        @SerializedName("author")
        var author: User? = null,

        @SerializedName("products")
        @Expose
        var products: List<Product>? = mutableListOf(),

        @SerializedName("is_your_added")
        @Expose
        var isYourAdded: Boolean = false,

        @SerializedName("has_your_like")
        @Expose
        var hasYourLike: Boolean = false
) : Entity