package com.sugar.steptofood.db.dto

import com.j256.ormlite.dao.ForeignCollection
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.field.ForeignCollectionField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "food")
data class Food(
        @DatabaseField(generatedId = true)
        override var id: Int? = null,

        @DatabaseField(canBeNull = false)
        var name: String = "",

        @ForeignCollectionField(eager = false)
        var products: ForeignCollection<ProductFood>? = null,

        @DatabaseField(foreign = true)
        var author: User? = null,

        @DatabaseField(canBeNull = false)
        var description: String = "",

        @DatabaseField(canBeNull = false)
        var calorie: Double? = null,

        @DatabaseField(canBeNull = false)
        var protein: Double? = null,

        @DatabaseField(canBeNull = false)
        var fat: Double? = null,

        @DatabaseField(canBeNull = false)
        var carbohydrates: Double? = null
) : Dto