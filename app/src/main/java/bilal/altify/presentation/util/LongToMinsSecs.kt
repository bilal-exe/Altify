package bilal.altify.presentation.util

import kotlin.time.Duration

fun Duration.toMinsSecs(): String {
    return String.format(
        "%02d:%02d",
        inWholeMinutes,
        inWholeSeconds % 60
    )
}