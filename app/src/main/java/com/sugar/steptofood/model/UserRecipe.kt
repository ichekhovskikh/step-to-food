package com.sugar.steptofood.model

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import com.sugar.steptofood.utils.RecipeType

@DatabaseTable(tableName = "user_recipe")
data class UserRecipe (
        @DatabaseField(generatedId = true)
        override var id: Int? = null,

        @DatabaseField(foreignAutoRefresh = true, foreign = true)
        var recipe: Recipe? = null,

        @DatabaseField(foreignAutoRefresh = true, foreign = true)
        var user: User? = null,

        @DatabaseField
        var type: String = RecipeType.LIKE.toString()
) : Entity