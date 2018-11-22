package com.sugar.steptofood.model.dto

import android.arch.persistence.room.*

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

        @ForeignKey(
                entity = User::class,
                parentColumns = ["id"],
                childColumns = ["authorId"],
                onDelete = ForeignKey.CASCADE)
        var authorId: Int? = null
) : EntityDto