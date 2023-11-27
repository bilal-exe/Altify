package bilal.altify.domain.spotify.repositories

import bilal.altify.domain.model.Category

interface CategoriesRepository {

    suspend fun getCategories(): List<Category>

    suspend fun getCategory(id: String): Category

}