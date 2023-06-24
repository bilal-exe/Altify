package bilal.altify.data.util

fun String.clipLen(len: Int = 25): String =
    if (this.length > len) this.substring(0, len - 3) + "..." else this