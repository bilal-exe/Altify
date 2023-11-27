package bilal.altify.data.spotify.respoitories

import bilal.altify.domain.model.Category
import bilal.altify.domain.spotify.repositories.CategoriesRepository

class CategoriesRepositoryImpl : CategoriesRepository {

    override suspend fun getCategories(): List<Category> {
        TODO("Not yet implemented")
    }

    override suspend fun getCategory(id: String): Category {
        TODO("Not yet implemented")
    }
}