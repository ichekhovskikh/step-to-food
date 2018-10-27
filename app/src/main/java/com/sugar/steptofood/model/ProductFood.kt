package com.sugar.steptofood.model

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "product_food")
data class ProductFood (
        @DatabaseField(generatedId = true)
        override var id: Int? = null,

        @DatabaseField(foreign = true)
        var food: Food? = null,

        @DatabaseField(foreign = true)
        var product: Product? = null
) : Entity