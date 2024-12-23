package net.benwoodworth.knbt.internal

import net.benwoodworth.knbt.NbtNamed
import net.benwoodworth.knbt.tag.*
import net.benwoodworth.knbt.test.parameterizeTest
import net.benwoodworth.knbt.test.parameters.*
import kotlin.test.Test
import kotlin.test.assertEquals

class TreeNbtWriterTest {
    private val rootName = "root_name"

    private inline fun expectNbtWriterCalls(expectedTag: NbtTag, write: NbtWriter.() -> Unit) {
        var actualTag: NbtNamed<NbtTag>? = null
        TreeNbtWriter { actualTag = it }.write()
        assertEquals(NbtNamed(rootName, expectedTag), actualTag)
    }

    @Test
    fun should_write_Byte_correctly() = parameterizeTest {
        val value by parameterOfBytes()

        expectNbtWriterCalls(NbtByte(value)) {
            beginRootTag(NbtType.BYTE, rootName)
            writeByte(value)
        }
    }

    @Test
    fun should_write_Short_correctly() = parameterizeTest {
        val value by parameterOfShorts()

        expectNbtWriterCalls(NbtShort(value)) {
            beginRootTag(NbtType.SHORT, rootName)
            writeShort(value)
        }
    }

    @Test
    fun should_write_Int_correctly() = parameterizeTest {
        val value by parameterOfInts()

        expectNbtWriterCalls(NbtInt(value)) {
            beginRootTag(NbtType.INT, rootName)
            writeInt(value)
        }
    }

    @Test
    fun should_write_Long_correctly() = parameterizeTest {
        val value by parameterOfLongs()

        expectNbtWriterCalls(NbtLong(value)) {
            beginRootTag(NbtType.LONG, rootName)
            writeLong(value)
        }
    }

    @Test
    fun should_write_Float_correctly() = parameterizeTest {
        val value by parameterOfFloats()

        expectNbtWriterCalls(NbtFloat(value)) {
            beginRootTag(NbtType.FLOAT, rootName)
            writeFloat(value)
        }
    }

    @Test
    fun should_write_Double_correctly() = parameterizeTest {
        val value by parameterOfDoubles()

        expectNbtWriterCalls(NbtDouble(value)) {
            beginRootTag(NbtType.DOUBLE, rootName)
            writeDouble(value)
        }
    }

    @Test
    fun should_write_ByteArray_correctly() = parameterizeTest {
        val value by parameterOfByteArrays()

        expectNbtWriterCalls(NbtByteArray(value)) {
            beginRootTag(NbtType.BYTE_ARRAY, rootName)
            beginByteArray(value.size)
            value.forEach { entry ->
                beginByteArrayEntry()
                writeByte(entry)
            }
            endByteArray()
        }
    }

    @Test
    fun should_write_IntArray_correctly() = parameterizeTest {
        val value by parameterOfIntArrays()

        expectNbtWriterCalls(NbtIntArray(value)) {
            beginRootTag(NbtType.INT_ARRAY, rootName)
            beginIntArray(value.size)
            value.forEach { entry ->
                beginIntArrayEntry()
                writeInt(entry)
            }
            endIntArray()
        }
    }

    @Test
    fun should_write_LongArray_correctly() = parameterizeTest {
        val value by parameterOfLongArrays()

        expectNbtWriterCalls(NbtLongArray(value)) {
            beginRootTag(NbtType.LONG_ARRAY, rootName)
            beginLongArray(value.size)
            value.forEach { entry ->
                beginLongArrayEntry()
                writeLong(entry)
            }
            endLongArray()
        }
    }

    @Test
    fun should_write_Compound_with_no_entries_correctly() {
        expectNbtWriterCalls(buildNbtCompound {}) {
            beginRootTag(NbtType.COMPOUND, rootName)
            beginCompound()
            endCompound()
        }
    }

    @Test
    fun should_write_Compound_with_one_entry_correctly() {
        expectNbtWriterCalls(
            buildNbtCompound { put("entry", 5) }
        ) {
            beginRootTag(NbtType.COMPOUND, rootName)
            beginCompound()
            beginCompoundEntry(NbtType.INT, "entry")
            writeInt(5)
            endCompound()
        }
    }

    @Test
    fun should_write_List_with_no_entries_correctly() {
        expectNbtWriterCalls(NbtList<NbtTag>()) {
            beginRootTag(NbtType.LIST, rootName)
            beginList(NbtType.END, 0)
            endList()
        }
    }

    @Test
    fun should_write_List_with_one_entry_correctly() {
        expectNbtWriterCalls(NbtList.of(listOf("entry").map { NbtString(it) })) {
            beginRootTag(NbtType.LIST, rootName)
            beginList(NbtType.STRING, 1)
            beginListEntry()
            writeString("entry")
            endList()
        }
    }
}
