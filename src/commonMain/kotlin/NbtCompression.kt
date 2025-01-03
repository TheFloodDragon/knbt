package net.benwoodworth.knbt

import net.benwoodworth.knbt.internal.*
import okio.Sink
import okio.Source

public abstract class NbtCompression private constructor() {
    internal abstract fun decompress(source: Source): Source
    internal abstract fun compress(sink: Sink, level: Int?): Sink

    public companion object;

    public data object None : NbtCompression() {
        override fun decompress(source: Source): Source = source
        override fun compress(sink: Sink, level: Int?): Sink = sink
    }

    public data object Gzip : NbtCompression() {
        override fun decompress(source: Source): Source = source.asGzipSource()
        override fun compress(sink: Sink, level: Int?): Sink = sink.asGzipSink(level ?: -1)
    }

    public data object Zlib : NbtCompression() {
        override fun decompress(source: Source): Source = source.asZlibSource()
        override fun compress(sink: Sink, level: Int?): Sink = sink.asZlibSink(level ?: -1)
    }
}

/**
 * @throws NbtDecodingException when unable to detect NbtCompression.
 */
internal fun NbtCompression.Companion.detect(context: NbtContext, firstByte: Byte): NbtCompression =
    when (firstByte) {
        // NBT Tag type IDs
        in 0..12 -> NbtCompression.None

        // Gzip header: 0x1F8B
        0x1F.toByte() -> NbtCompression.Gzip

        // Zlib headers: 0x7801, 0x789C, and 0x78DA
        0x78.toByte() -> NbtCompression.Zlib

        else -> {
            val message = "Unable to detect NbtCompression. Unexpected first byte: 0x${firstByte.toHex()}"
            throw NbtDecodingException(context, message)
        }
    }

/**
 * Peek in the [byteArray] and detect what [NbtCompression] is used.
 *
 * @throws NbtDecodingException when unable to detect NbtCompression.
 */
public fun NbtCompression.Companion.detect(byteArray: ByteArray): NbtCompression =
    detect(EmptyNbtContext, byteArray[0])
