package net.benwoodworth.knbt

import net.benwoodworth.knbt.*
import net.benwoodworth.knbt.tag.*
import kotlinx.serialization.encodeToByteArray
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import kotlin.test.assertContentEquals

class JvmStreamsTest {
    @Test
    fun should_not_read_more_bytes_than_necessary() {
        val nbt = JavaNetworkNbt {
            protocolVersion = 764
            compression = NbtCompression.None
        }

        val tag = buildNbtCompound {
            put("text", "test")
        }

        val tagBytes = nbt.encodeToByteArray(tag)
        val extraBytes = byteArrayOf(3, 5)

        val input = ByteArrayInputStream(tagBytes + extraBytes)

        nbt.decodeFromStream<NbtTag>(input)
        val actualExtraBytes = input.readBytes()

        assertContentEquals(extraBytes, actualExtraBytes)
    }
}
