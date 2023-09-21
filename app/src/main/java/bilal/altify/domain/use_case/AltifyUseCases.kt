package bilal.altify.domain.use_case

data class AltifyUseCases(
    val currentTrack: GetCurrentTrackFlowUseCase,
    val browser: GetBrowserStateFlowUseCase,
    val commands: ExecuteCommandUseCase
)
