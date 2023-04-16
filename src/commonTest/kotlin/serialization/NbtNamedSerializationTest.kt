package net.benwoodworth.knbt.serialization

import kotlinx.serialization.Serializable
import net.benwoodworth.knbt.*
import kotlin.test.Test

class NbtNamedSerializationTest : SerializationTest() {
    @Serializable
    @NbtNamed("root-name")
    private data class TestNbtClass(
        val string: String,
        val int: Int,
    )

    private val testNbt = TestNbtClass(
        string = "string",
        int = 42,
    )

    private val testNbtTag = buildNbtCompound("root-name") {
        put("string", "string")
        put("int", 42)
    }

    @Test
    fun should_serialize_nested_under_name() {
        defaultNbt.testSerialization(
            testNbt,
            testNbtTag
        )
    }

    @Test
    fun should_serialize_nested_under_name_when_within_a_class() {
        @Serializable
        data class OuterClass(val testNbtClass: TestNbtClass)

        defaultNbt.testSerialization(
            OuterClass(testNbt),
            buildNbtCompound {
                put("testNbtClass", testNbtTag)
            }
        )
    }

    @Test
    fun should_serialize_nested_under_name_when_within_a_list() {
        defaultNbt.testSerialization(
            listOf(testNbt),
            buildNbtList {
                add(testNbtTag)
            }
        )
    }
}