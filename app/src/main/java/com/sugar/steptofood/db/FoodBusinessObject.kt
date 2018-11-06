package com.sugar.steptofood.db

import com.sugar.steptofood.model.Food
import com.sugar.steptofood.model.Product
import com.sugar.steptofood.model.ProductFood
import com.sugar.steptofood.model.UserFood
import com.sugar.steptofood.utils.FoodType
import kotlin.math.min

class FoodBusinessObject(private val dbHelper: SQLiteHelper) {

    fun removeAll() {
        dbHelper.userFoodDao.removeAll()
        dbHelper.userDao.removeAll()
        dbHelper.productFoodDao.removeAll()
        dbHelper.productDao.removeAll()
        dbHelper.foodDao.removeAll()
    }

    fun cascadeRemove(foodId: Int) {
        val userFoods = dbHelper.userFoodDao.filter { userFood ->
            userFood.food?.id == foodId
        }
        dbHelper.userFoodDao.removeRange(userFoods)

        val productFoods = dbHelper.productFoodDao.filter { productFood ->
            productFood.food?.id == foodId
        }
        dbHelper.productFoodDao.removeRange(productFoods)
        dbHelper.foodDao.remove(foodId)
    }

    fun foodCount(type: FoodType, userId: Int) =
            getRangeFood(type, null, userId, "", 0, Int.MAX_VALUE).count()

    fun getRangeFood(type: FoodType, yourId: Int?, otherUserId: Int, searchName: String, start: Int, size: Int) =
            if (type == FoodType.ADDED)
                getRangeAddedFood(yourId, otherUserId, searchName, start, size)
            else getRangeUserFood(type, yourId, otherUserId, searchName, start, size)

    fun addFoods(foods: List<Food>, foodType: FoodType = FoodType.ADDED, hasUserLike: Int = -1) {
        if (foodType == FoodType.ADDED) putAddedFood(foods)
        else putUserFood(foodType, foods, hasUserLike)
    }

    fun putAddedFood(foods: List<Food>) {
        for (food in foods) {
            createOrUpdateFood(food)
        }
    }

    fun putUserFood(foodType: FoodType, foods: List<Food>, putLikeUserId: Int) {
        for (food in foods) {
            createOrUpdateFood(food)
            createUserFood(foodType, food, putLikeUserId)
        }
    }

    fun foodContainsProduct(food: Food, product: Product) =
            dbHelper.productFoodDao.none { productFood ->
                productFood.food?.id == food.id && productFood.product?.id == product.id
            }

    fun hasUserLike(userId: Int, foodId: Int) =
            dbHelper.userFoodDao.none { likeFood ->
                likeFood.food?.id == foodId
                        && likeFood.user?.id == userId
                        && likeFood.type == FoodType.LIKE.toString()
            }

    private fun getRangeUserFood(type: FoodType,
                                 yourId: Int?,
                                 userId: Int,
                                 searchName: String,
                                 start: Int,
                                 size: Int): List<Food> {
        val userFoods = dbHelper.userFoodDao.filter { userFood ->
            userFood.user?.id == userId
                    && userFood.food!!.name.contains(searchName)
                    && userFood.type == type.toString()
        }

        val foods = mutableListOf<Food>()
        for (i in start until min(userFoods.count(), size)) {
            val food = userFoods[i].food!!
            setFoodProperty(food, yourId, userId)
            foods.add(food)
        }
        return foods
    }

    private fun getRangeAddedFood(yourId: Int?,
                                  userId: Int,
                                  searchName: String,
                                  start: Int,
                                  size: Int): List<Food> {
        val addedFoods = dbHelper.foodDao.filter { food ->
            food.author?.id == userId && food.name.contains(searchName)
        }

        val foods = mutableListOf<Food>()
        for (i in start until min(addedFoods.count(), size)) {
            val food = addedFoods[i]
            setFoodProperty(food, yourId, userId)
            foods.add(food)

        }
        return foods
    }

    private fun setFoodProperty(food: Food, yourId: Int?, userId: Int) {
        val products = dbHelper.productDao.filter { product ->
            foodContainsProduct(food, product)
        }
        food.products = products
        if (yourId != null)
            food.hasYourLike = hasUserLike(yourId, food.id!!)
    }

    private fun createOrUpdateFood(food: Food) {
        dbHelper.userDao.createOrUpdate(food.author)
        dbHelper.foodDao.createOrUpdate(food)
        food.products?.forEach { product ->
            dbHelper.productDao.createOrUpdate(product)
            createProductFood(product, food)
        }
    }

    private fun createUserFood(foodType: FoodType, food: Food, userId: Int) {
        if (dbHelper.userFoodDao.none { userFood -> userFoodContentEquals(foodType, food, userId, userFood) }) {
            val foodFromDb = dbHelper.foodDao.queryForId(food.id)
            val authorFromDb = dbHelper.userDao.queryForId(food.author?.id)
            dbHelper.userFoodDao.create(UserFood(null, foodFromDb, authorFromDb, foodType.toString()))
        }
    }

    private fun createProductFood(product: Product, food: Food) {
        if (dbHelper.productFoodDao.none { productFood -> productFoodContentEquals(food, product, productFood) }) {
            val foodFromDb = dbHelper.foodDao.queryForId(food.id)
            val productFromDb = dbHelper.productDao.queryForId(product.id)
            dbHelper.productFoodDao.create(ProductFood(null, foodFromDb, productFromDb))
        }
    }

    private fun productFoodContentEquals(food: Food, product: Product, productFood: ProductFood) =
            productFood.food?.id == food.id && productFood.product?.id == product.id

    private fun userFoodContentEquals(foodType: FoodType, food: Food, userId: Int, userFood: UserFood) =
            userFood.food?.id == food.id && userFood.user?.id == userId && userFood.type == foodType.toString()
}