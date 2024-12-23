package net.benwoodworth.knbt

public class NbtConfiguration internal constructor(
    override val encodeDefaults: Boolean,
    override val ignoreUnknownKeys: Boolean,
    override val lenientNbtNames: Boolean,
) : NbtFormatConfiguration() {
    override fun toString(): String =
        "NbtConfiguration(" +
                "encodeDefaults=$encodeDefaults" +
                ", ignoreUnknownKeys=$ignoreUnknownKeys" +
                ", lenientNbtNames=$lenientNbtNames" +
                ")"
}

public class NbtBuilder(nbt: NbtFormat? = null) : NbtFormatBuilder(nbt) {
    override fun build(): Nbt {
        return Nbt(
            configuration = NbtConfiguration(
                encodeDefaults = encodeDefaults,
                ignoreUnknownKeys = ignoreUnknownKeys,
                lenientNbtNames = lenientNbtNames,
            ),
            serializersModule = serializersModule,
        )
    }
}
