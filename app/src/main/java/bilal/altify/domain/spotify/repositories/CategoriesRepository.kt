package bilal.altify.domain.spotify.repositories

import bilal.altify.domain.model.ListItems

interface CategoriesRepository {

    suspend fun getCategories(): ListItems

}