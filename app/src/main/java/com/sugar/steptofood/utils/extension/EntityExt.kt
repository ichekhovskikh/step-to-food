package com.sugar.steptofood.utils.extension

import com.sugar.steptofood.model.dto.*
import com.sugar.steptofood.model.fullinfo.*

fun FullRecipeInfo.toRecipe() =
        Recipe(id,
                name,
                image,
                description,
                calorie,
                protein,
                fat,
                carbohydrates,
                author?.id)

fun List<FullProductInfo>.toProductList() = map { Product(it.id, it.name) }.toList()