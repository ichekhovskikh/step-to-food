package com.sugar.steptofood.model

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "product_recipe")
data class ProductRecipe (
        @DatabaseField(generatedId = true)
        override var id: Int? = null,

        @DatabaseField(foreignAutoRefresh = true, foreign = true)
        var recipe: Recipe? = null,

        @DatabaseField(foreignAutoRefresh = true, foreign = true)
        var product: Product? = null
) : Entity