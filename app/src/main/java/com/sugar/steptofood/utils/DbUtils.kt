package com.sugar.steptofood.utils

import com.sugar.steptofood.db.SQLiteHelper
import com.sugar.steptofood.model.Food
import com.sugar.steptofood.model.Product
import com.sugar.steptofood.model.ProductFood
import com.sugar.steptofood.model.UserFood

fun userFoodContentEquals(food: Food, userFood: UserFood, foodType: String) =
        userFood.food?.id == food.id &&
                userFood.user?.id == food.author?.id &&
                userFood.foodType == foodType

fun productFoodContentEquals(food: Food, product: Product, productFood: ProductFood) =
        productFood.food?.id == food.id && productFood.product?.id == product.id

fun isLoadedUserFood(userFood: UserFood, userId: Int, foodType: String, startId: Int) =
        userFood.user?.id == userId &&
                userFood.foodType == foodType &&
                userFood.food?.id!! >= startId

fun foodContainsProduct(dbHelper: SQLiteHelper, food: Food, product: Product) =
        dbHelper.productFoodDao.none { productFood ->
            productFood.food?.id == food.id && productFood.product?.id == product.id
        }