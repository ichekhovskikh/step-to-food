package com.sugar.steptofood.db

import com.sugar.steptofood.model.Food
import com.sugar.steptofood.model.Product
import com.sugar.steptofood.model.ProductFood
import com.sugar.steptofood.model.UserFood
import com.sugar.steptofood.utils.FoodType
import java.lang.reflect.Type
import kotlin.math.min

class FoodBusinessObject(private val dbHelper: SQLiteHelper) {

    fun getRangeFood(userId: Int, type: FoodType, startId: Int, size: Int) =
            if (type == FoodType.LIKE)
                getRangeLikeFood(userId, startId, size)
            else getRangeAddedFood(userId, startId, size)

    fun getRangeLikeFood(userId: Int, startId: Int, size: Int): List<Food> {
        val userFoods = dbHelper.userFoodDao.filter { userFood ->
            isLoadedUserFood(userFood, userId, startId)
        }

        val foods = mutableListOf<Food>()
        for (i in 0 until min(userFoods.count(), size)) {
            val food = userFoods[i].food!!
            val products = dbHelper.productDao.filter { product ->
                foodContainsProduct(food, product)
            }
            food.products = products
            foods.add(food)
        }
        return foods
    }

    fun getRangeAddedFood(userId: Int, startId: Int, size: Int): List<Food> {
        val addedFoods = dbHelper.foodDao.filter { food ->
            food.id!! >= startId && food.author?.id == userId
        }

        val foods = mutableListOf<Food>()
        for (i in 0 until min(addedFoods.count(), size)) {
            val food = addedFoods[i]
            val products = dbHelper.productDao.filter { product ->
                foodContainsProduct(food, product)
            }
            food.products = products
            foods.add(food)
        }
        return foods
    }

    fun update(foods: List<Food>) {
        for (food in foods) {
            createOrUpdateFood(food)
        }
    }

    fun update(foods: List<Food>, putLikeUserId: Int) {
        for (food in foods) {
            createOrUpdateFood(food)
            createUserFood(food, putLikeUserId)
        }
    }

    fun foodContainsProduct(food: Food, product: Product) =
            dbHelper.productFoodDao.none { productFood ->
                productFood.food?.id == food.id && productFood.product?.id == product.id
            }

    private fun createOrUpdateFood(food: Food) {
        dbHelper.userDao.createOrUpdate(food.author)
        dbHelper.foodDao.createOrUpdate(food)
        food.products?.forEach { product ->
            dbHelper.productDao.createOrUpdate(product)
            createProductFoodInDb(product, food)
        }
    }

    private fun createUserFood(food: Food, userId: Int) {
        if (dbHelper.userFoodDao.none { userFood -> userFoodContentEquals(food, userId, userFood) }) {
            val authorFromDb = dbHelper.userDao.queryForId(food.author?.id)
            val foodFromDb = dbHelper.foodDao.queryForId(food.id)
            dbHelper.userFoodDao.create(UserFood(null, foodFromDb, authorFromDb))
        }
    }

    private fun createProductFoodInDb(product: Product, food: Food) {
        if (dbHelper.productFoodDao.none { productFood -> productFoodContentEquals(food, product, productFood) }) {
            val productFromDb = dbHelper.productDao.queryForId(product.id)
            val foodFromDb = dbHelper.foodDao.queryForId(food.id)
            dbHelper.productFoodDao.create(ProductFood(null, foodFromDb, productFromDb))
        }
    }

    private fun isLoadedUserFood(userFood: UserFood, userId: Int, startId: Int) =
            userFood.user?.id == userId && userFood.food?.id!! >= startId

    private fun productFoodContentEquals(food: Food, product: Product, productFood: ProductFood) =
            productFood.food?.id == food.id && productFood.product?.id == product.id

    private fun userFoodContentEquals(food: Food, userId: Int, userFood: UserFood) =
            userFood.food?.id == food.id && userFood.user?.id == userId
}