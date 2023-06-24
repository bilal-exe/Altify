package bilal.altify.data.util

import kotlin.time.Duration.Companion.milliseconds

fun Long.toMinsSecs(): String {
    val millis = this.milliseconds
    return String.format(
        "%02d:%02d",
        millis.inWholeMinutes,
        millis.inWholeSeconds % 60
    )
}