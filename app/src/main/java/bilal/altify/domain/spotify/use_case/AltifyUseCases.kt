package bilal.altify.domain.spotify.use_case

data class AltifyUseCases(
    val currentTrack: GetCurrentTrackFlowUseCase,
    val browser: GetBrowserStateFlowUseCase,
    val commands: ExecuteCommandUseCase
)
