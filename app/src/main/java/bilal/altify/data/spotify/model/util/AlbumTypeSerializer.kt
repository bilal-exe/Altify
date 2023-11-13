package bilal.altify.data.spotify.model.util

import bilal.altify.domain.model.ExtendedItem
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object AlbumTypeSerializer : KSerializer<ExtendedItem.Album.AlbumType> {

    override val descriptor = PrimitiveSerialDescriptor(
        serialName = "AlbumType",
        kind = PrimitiveKind.STRING,
    )

    override fun deserialize(decoder: Decoder): ExtendedItem.Album.AlbumType =
        decoder.decodeString().asAlbumType()

    override fun serialize(encoder: Encoder, value: ExtendedItem.Album.AlbumType) =
        encoder.encodeString(value.serializedName)

}


private fun String?.asAlbumType(): ExtendedItem.Album.AlbumType =
    when (this) {
        null -> ExtendedItem.Album.AlbumType.Album
        else -> ExtendedItem.Album.AlbumType.values()
            .find { it.serializedName == this }
            ?: ExtendedItem.Album.AlbumType.Album
    }
