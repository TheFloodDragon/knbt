package net.benwoodworth.knbt.external

import kotlinx.serialization.Serializable
import net.benwoodworth.knbt.NbtName
import net.benwoodworth.knbt.NbtNamed
import net.benwoodworth.knbt.tag.*
import net.benwoodworth.knbt.test.file.*
import net.benwoodworth.knbt.test.parameterizeTest
import net.benwoodworth.knbt.test.parameters.parameterOfVerifyingNbt
import kotlin.test.Test
import kotlin.test.assertEquals

class ClassSerializerTest {
    @Test
    fun serializing_a_class_with_an_NBT_name_should_correctly_serialize_the_name() = parameterizeTest {
        @Serializable
        @NbtName("RootKey")
        data class MyClass(val property: String)

        val nbt by parameterOfVerifyingNbt()

        nbt.verifyEncoderOrDecoder(
            MyClass.serializer(),
            MyClass("value"),
            NbtNamed("RootKey", buildNbtCompound { put("property", "value") }),
            testDecodedValue = { value, decodedValue ->
                assertEquals(value, decodedValue, "decodedValue")
            }
        )
    }

    @Test
    fun should_serialize_TestNbt_class_correctly() = parameterizeTest {
        val nbt by parameterOfVerifyingNbt()

        nbt.verifyEncoderOrDecoder(
            TestNbt.serializer(),
            testClass,
            testTag,
            testDecodedValue = { value, decodedValue ->
                assertEquals(value, decodedValue, "decodedValue")
            }
        )
    }

    @Test
    fun should_serialize_BigTestNbt_class_correctly() = parameterizeTest {
        val nbt by parameterOfVerifyingNbt()

        nbt.verifyEncoderOrDecoder(
            BigTestNbt.serializer(),
            bigTestClass,
            bigTestTag,
            testDecodedValue = { value, decodedValue ->
                assertEquals(value, decodedValue, "decodedValue")
            }
        )
    }

    @Test
    fun should_serialize_class_with_one_property_correctly() = parameterizeTest {
        @Serializable
        @NbtName("OneProperty")
        data class OneProperty(val property: Int)

        val nbt by parameterOfVerifyingNbt()

        nbt.verifyEncoderOrDecoder(
            OneProperty.serializer(),
            OneProperty(7),
            buildNbtCompound("OneProperty") {
                put("property", 7)
            },
            testDecodedValue = { value, decodedValue ->
                assertEquals(value, decodedValue, "decodedValue")
            }
        )
    }

    @Test
    fun should_serialize_class_with_two_properties_correctly() = parameterizeTest {
        @Serializable
        @NbtName("TwoProperties")
        data class TwoProperties(val entry1: String, val entry2: Long)

        val nbt by parameterOfVerifyingNbt()

        nbt.verifyEncoderOrDecoder(
            TwoProperties.serializer(),
            TwoProperties(entry1 = "value1", entry2 = 1234L),
            buildNbtCompound("TwoProperties") {
                put("entry1", "value1")
                put("entry2", 1234L)
            },
            testDecodedValue = { value, decodedValue ->
                assertEquals(value, decodedValue, "decodedValue")
            }
        )
    }
}
