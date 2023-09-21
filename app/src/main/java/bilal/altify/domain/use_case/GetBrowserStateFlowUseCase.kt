package bilal.altify.domain.use_case

import bilal.altify.domain.controller.AltifySources
import bilal.altify.presentation.BrowserState
import kotlinx.coroutines.flow.combine

class GetBrowserStateFlowUseCase {

    operator fun invoke(
        repositories: AltifySources
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