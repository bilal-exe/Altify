package bilal.altify.domain.spotify.use_case

import bilal.altify.domain.spotify.use_case.model.BrowserState
import bilal.altify.domain.spotify.repositories.AltifyRepositories
import kotlinx.coroutines.flow.combine

class GetBrowserStateFlowUseCase {

    operator fun invoke(
        repositories: AltifyRepositories
    ) = combine(
        repositories.content.listItemsFlow,
        repositories.images.thumbnailFlow,
        repositories.user.browserLibraryState,
    ) {
        listItems, thumbnails, libraryState ->
        BrowserState(
            listItems = listItems,
            libraryState = libraryState,
            thumbnailMap = thumbnails
        )
    }

}