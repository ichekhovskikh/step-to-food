package com.sugar.steptofood.db

import com.sugar.steptofood.model.Food
import com.sugar.steptofood.model.Product
import com.sugar.steptofood.model.ProductFood
import com.sugar.steptofood.model.LikeFood
import com.sugar.steptofood.utils.FoodType
import kotlin.math.min

class FoodBusinessObject(private val dbHelper: SQLiteHelper) {

    fun getRangeFood(userId: Int, searchName: String, type: FoodType, startId: Int, size: Int) =
            if (type == FoodType.LIKE)
                getRangeLikeFood(userId, searchName, startId, size)
            else getRangeAddedFood(userId, searchName, startId, size)

    fun getRangeLikeFood(userId: Int, searchName: String, startId: Int, size: Int): List<Food> {
        val userFoods = dbHelper.likeFoodDao.filter { userFood ->
            isLoadedUserFood(userFood, searchName, userId, startId)
        }

        val foods = mutableListOf<Food>()
        for (i in 0 until min(userFoods.count(), size)) {
            val food = userFoods[i].food!!
            val products = dbHelper.productDao.filter { product ->
                foodContainsProduct(food, product)
            }
            food.products = products
            food.hasYourLike = userPutLike(userId, food.id!!)
            foods.add(food)
        }
        return foods
    }

    fun getRangeAddedFood(userId: Int, searchName: String, startId: Int, size: Int): List<Food> {
        val addedFoods = dbHelper.foodDao.filter { food ->
            food.id!! >= startId && food.author?.id == userId && food.name.contains(searchName)
        }

        val foods = mutableListOf<Food>()
        for (i in 0 until min(addedFoods.count(), size)) {
            val food = addedFoods[i]
            val products = dbHelper.productDao.filter { product ->
                foodContainsProduct(food, product)
            }
            food.products = products
            food.hasYourLike = userPutLike(userId, food.id!!)
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

    fun userPutLike(userId: Int, foodId: Int) =
            dbHelper.likeFoodDao.none { likeFood ->
                likeFood.food?.id == foodId && likeFood.user?.id == userId
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
        if (dbHelper.likeFoodDao.none { userFood -> userFoodContentEquals(food, userId, userFood) }) {
            val authorFromDb = dbHelper.userDao.queryForId(food.author?.id)
            val foodFromDb = dbHelper.foodDao.queryForId(food.id)
            dbHelper.likeFoodDao.create(LikeFood(null, foodFromDb, authorFromDb))
        }
    }

    private fun createProductFoodInDb(product: Product, food: Food) {
        if (dbHelper.productFoodDao.none { productFood -> productFoodContentEquals(food, product, productFood) }) {
            val productFromDb = dbHelper.productDao.queryForId(product.id)
            val foodFromDb = dbHelper.foodDao.queryForId(food.id)
            dbHelper.productFoodDao.create(ProductFood(null, foodFromDb, productFromDb))
        }
    }

    private fun isLoadedUserFood(likeFood: LikeFood, searchName: String, userId: Int, startId: Int) =
            likeFood.user?.id == userId && likeFood.food?.id!! >= startId && likeFood.food!!.name.contains(searchName)

    private fun productFoodContentEquals(food: Food, product: Product, productFood: ProductFood) =
            productFood.food?.id == food.id && productFood.product?.id == product.id

    private fun userFoodContentEquals(food: Food, userId: Int, likeFood: LikeFood) =
            likeFood.food?.id == food.id && likeFood.user?.id == userId
}