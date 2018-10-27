package com.sugar.steptofood.paging.source

import android.arch.paging.ItemKeyedDataSource
import com.sugar.steptofood.db.SQLiteHelper
import com.sugar.steptofood.model.Food
import com.sugar.steptofood.model.Product
import com.sugar.steptofood.model.ProductFood
import com.sugar.steptofood.model.UserFood
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.utils.*
import io.reactivex.disposables.CompositeDisposable
import kotlin.math.min

class FoodSource(private val api: ApiService,
                 private val compositeDisposable: CompositeDisposable,
                 private val userId: Int,
                 private val type: FoodType,
                 private val dbHelper: SQLiteHelper?) : ItemKeyedDataSource<Int, Food>() {

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Food>) {
        compositeDisposable.add(api
                .getFoodAll(userId, type, params.requestedInitialKey!!, params.requestedLoadSize)
                .subscribe({ response ->
                    if (response.success) {
                        updateDb(response.content!!)
                        callback.onResult(response.content)
                    } else if (!response.success) {
                        onError(params, callback)
                    }
                }, { onError(params, callback) }))
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Food>) {
        compositeDisposable.add(api
                .getFoodAll(userId, type, params.key, params.requestedLoadSize)
                .subscribe({ response ->
                    if (response.success) {
                        updateDb(response.content!!)
                        callback.onResult(response.content)
                    } else if (!response.success) {
                        onError(params, callback)
                    }
                }, { onError(params, callback) }))
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Food>) {}

    override fun getKey(item: Food) = item.id!!

    private fun updateDb(foods: List<Food>) {
        for (food in foods) {
            dbHelper!!.userDao.createOrUpdate(food.author)
            dbHelper.foodDao.createOrUpdate(food)
            createUserFoodInDb(food)
            food.products?.forEach { product ->
                dbHelper.productDao.createOrUpdate(product)
                createProductFoodInDb(product, food)
            }
        }
    }

    private fun createUserFoodInDb(food: Food) {
        if (dbHelper!!.userFoodDao.none { userFood -> userFoodContentEquals(food, userFood, type.toString()) }) {
            val authorFromDb = dbHelper.userDao.queryForId(food.author?.id)
            val foodFromDb = dbHelper.foodDao.queryForId(food.id)
            dbHelper.userFoodDao.create(UserFood(null, type.toString(), foodFromDb, authorFromDb))
        }
    }

    private fun createProductFoodInDb(product: Product, food: Food) {
        if (dbHelper!!.productFoodDao.none { productFood -> productFoodContentEquals(food, product, productFood) }) {
            val productFromDb = dbHelper.productDao.queryForId(product.id)
            val foodFromDb = dbHelper.foodDao.queryForId(food.id)
            dbHelper.productFoodDao.create(ProductFood(null, foodFromDb, productFromDb))
        }
    }

    private fun onError(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Food>) {
        callback.onResult(getRangeFromDb(params.requestedInitialKey!!, params.requestedLoadSize))
    }

    private fun onError(params: LoadParams<Int>, callback: LoadCallback<Food>) {
        callback.onResult(getRangeFromDb(params.key, params.requestedLoadSize))
    }

    private fun getRangeFromDb(startId: Int, size: Int): List<Food> {
        val userFoods = dbHelper!!.userFoodDao.filter { userFood ->
            isLoadedUserFood(userFood, userId, type.toString(), startId)
        }

        val foods = mutableListOf<Food>()
        for (i in 0 until min(userFoods.count(), size)) {
            val food = userFoods[i].food!!
            food.author = userFoods[i].user

            val products = dbHelper.productDao.filter { product ->
                foodContainsProduct(dbHelper, food, product)
            }
            food.products = products
            foods.add(food)
        }
        return foods
    }
}