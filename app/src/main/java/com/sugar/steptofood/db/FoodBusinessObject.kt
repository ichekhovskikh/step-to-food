package com.sugar.steptofood.db

import com.sugar.steptofood.model.Food
import com.sugar.steptofood.model.Product
import com.sugar.steptofood.model.ProductFood
import com.sugar.steptofood.model.UserFood
import com.sugar.steptofood.utils.FoodType
import kotlin.math.min

class FoodBusinessObject(private val dbHelper: SQLiteHelper) {

    fun getRangeFood(userId: Int, foodType: FoodType, startId: Int, size: Int): List<Food> {
        val userFoods = dbHelper.userFoodDao.filter { userFood ->
            isLoadedUserFood(userFood, userId, foodType.toString(), startId)
        }

        val foods = mutableListOf<Food>()
        for (i in 0 until min(userFoods.count(), size)) {
            val food = userFoods[i].food!!
            food.author = userFoods[i].user

            val products = dbHelper.productDao.filter { product ->
                foodContainsProduct(food, product)
            }
            food.products = products
            foods.add(food)
        }
        return foods
    }

    fun update(foods: List<Food>, foodType: FoodType) {
        for (food in foods) {
            dbHelper.userDao.createOrUpdate(food.author)
            dbHelper.foodDao.createOrUpdate(food)
            createUserFood(food, foodType)
            food.products?.forEach { product ->
                dbHelper.productDao.createOrUpdate(product)
                createProductFoodInDb(product, food)
            }
        }
    }

    fun foodContainsProduct(food: Food, product: Product) =
            dbHelper.productFoodDao.none { productFood ->
                productFood.food?.id == food.id && productFood.product?.id == product.id
            }

    private fun createUserFood(food: Food, foodType: FoodType) {
        if (dbHelper.userFoodDao.none { userFood -> userFoodContentEquals(food, userFood, foodType.toString()) }) {
            val authorFromDb = dbHelper.userDao.queryForId(food.author?.id)
            val foodFromDb = dbHelper.foodDao.queryForId(food.id)
            dbHelper.userFoodDao.create(UserFood(null, foodType.toString(), foodFromDb, authorFromDb))
        }
    }

    private fun createProductFoodInDb(product: Product, food: Food) {
        if (dbHelper.productFoodDao.none { productFood -> productFoodContentEquals(food, product, productFood) }) {
            val productFromDb = dbHelper.productDao.queryForId(product.id)
            val foodFromDb = dbHelper.foodDao.queryForId(food.id)
            dbHelper.productFoodDao.create(ProductFood(null, foodFromDb, productFromDb))
        }
    }

    private fun isLoadedUserFood(userFood: UserFood, userId: Int, foodType: String, startId: Int) =
            userFood.user?.id == userId &&
                    userFood.foodType == foodType &&
                    userFood.food?.id!! >= startId

    private fun productFoodContentEquals(food: Food, product: Product, productFood: ProductFood) =
            productFood.food?.id == food.id && productFood.product?.id == product.id

    private fun userFoodContentEquals(food: Food, userFood: UserFood, foodType: String) =
            userFood.food?.id == food.id &&
                    userFood.user?.id == food.author?.id &&
                    userFood.foodType == foodType
}