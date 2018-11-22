package com.sugar.steptofood.db.dao

import android.arch.persistence.room.*
import com.sugar.steptofood.db.Convectors
import com.sugar.steptofood.model.dto.Recipe
import com.sugar.steptofood.model.fullinfo.*

@Dao
@TypeConverters(Convectors.BooleanConverters::class, Convectors.ProductConverters::class)
interface RecipeDao : EntityDao<Recipe> {

    @Query("DELETE FROM recipe")
    fun removeAll()

    @Query("DELETE FROM recipe WHERE  id = :id")
    fun remove(id: Int)

    @Query("SELECT Recipe.id AS recipe_id, Recipe.name AS recipe_name, " +
            "image, description, calorie, protein, fat, carbohydrates, " +
            "GROUP_CONCAT(Product.id || ',' || Product.name || ',' || ProductRecipe.weight, ';') as products, " +
            "User.id, User.name, avatar, " +
            "(SELECT COUNT(*) FROM UserRecipe AS ur " +
            "WHERE ur.recipeId = Recipe.id AND ur.userId = :sessionUserId AND ur.type = 'LIKE') " +
            "|| '.toBoolean()' AS hasSessionUserLike " +
            "FROM ProductRecipe " +
            "INNER JOIN Recipe ON Recipe.id = ProductRecipe.recipeId " +
            "INNER JOIN Product ON Product.id = ProductRecipe.productId " +
            "INNER JOIN User ON User.id = Recipe.authorId " +
            "WHERE Recipe.id = :id " +
            "GROUP BY recipe_id")
    fun getById(id: Int, sessionUserId: Int = -1): FullRecipeInfo

    @Query("SELECT Recipe.id AS recipe_id, Recipe.name AS recipe_name, " +
            "image, description, calorie, protein, fat, carbohydrates, " +
            "GROUP_CONCAT(Product.id || ',' || Product.name || ',' || ProductRecipe.weight, ';') as products, " +
            "User.id, User.name, avatar, " +
            "(SELECT COUNT(*) FROM UserRecipe AS ur " +
            "WHERE ur.recipeId = Recipe.id AND ur.userId = :sessionUserId AND ur.type = 'LIKE') " +
            "|| '.toBoolean()' AS hasSessionUserLike " +
            "FROM ProductRecipe " +
            "INNER JOIN Recipe ON Recipe.id = ProductRecipe.recipeId " +
            "INNER JOIN Product ON Product.id = ProductRecipe.productId " +
            "INNER JOIN User ON User.id = Recipe.authorId " +
            "INNER JOIN UserRecipe ON UserRecipe.recipeId = Recipe.id " +
            "WHERE UserRecipe.userId = :userId AND UserRecipe.type = :type " +
            "AND Recipe.name LIKE '%' || :name || '%' " +
            "GROUP BY recipe_id " +
            "LIMIT :size OFFSET :start")
    fun getUserRecipes(type: String, name: String, userId: Int, sessionUserId: Int, start: Int, size: Int): List<FullRecipeInfo>

    @Query("SELECT Recipe.id AS recipe_id, Recipe.name AS recipe_name, " +
            "image, description, calorie, protein, fat, carbohydrates, " +
            "GROUP_CONCAT(Product.id || ',' || Product.name || ',' || ProductRecipe.weight, ';') as products, " +
            "User.id, User.name, avatar, " +
            "(SELECT COUNT(*) FROM UserRecipe AS ur " +
            "WHERE ur.recipeId = Recipe.id AND ur.userId = :sessionUserId AND ur.type = 'LIKE') " +
            "|| '.toBoolean()' AS hasSessionUserLike " +
            "FROM ProductRecipe " +
            "INNER JOIN Recipe ON Recipe.id = ProductRecipe.recipeId " +
            "INNER JOIN Product ON Product.id = ProductRecipe.productId " +
            "INNER JOIN User ON User.id = Recipe.authorId " +
            "WHERE authorId = :userId " +
            "AND Recipe.name LIKE '%' || :name || '%' " +
            "GROUP BY recipe_id " +
            "LIMIT :size OFFSET :start")
    fun getAddedRecipes(name: String, userId: Int, sessionUserId: Int, start: Int, size: Int): List<FullRecipeInfo>
}