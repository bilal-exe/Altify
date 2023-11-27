package bilal.altify.util

import com.neovisionaries.i18n.CountryCode
import java.util.Locale

fun getISO3166code(): String =
    CountryCode.getByAlpha3Code(Locale.getDefault().isO3Country).alpha2
