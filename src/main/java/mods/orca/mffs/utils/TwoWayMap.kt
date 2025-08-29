package mods.orca.mffs.utils

interface TwoWayMap<K, V> : Map<K, V> {
    val inverse: TwoWayMap<V, K>
}

interface MutableTwoWayMap<K, V> : TwoWayMap<K, V>, MutableMap<K, V> {
    override val inverse: MutableTwoWayMap<V, K>
}

fun <K, V> twoWayMapOf(vararg pairs: Pair<K, V>): TwoWayMap<K, V> {
    return mutableTwoWayMapOf(*pairs)
}

fun <K, V> mutableTwoWayMapOf(vararg pairs: Pair<K, V>): MutableTwoWayMap<K, V> {
    return MutableTwoWayMapImpl(pairs.toMap().toMutableMap())
}

private class MutableTwoWayMapImpl<K, V>(
    private val map: MutableMap<K, V>,
    inverse: MutableTwoWayMapImpl<V, K>? = null
) : MutableTwoWayMap<K, V> {
    override var inverse: MutableTwoWayMapImpl<V, K> = inverse
        ?: MutableTwoWayMapImpl(map.invert().toMutableMap(), this)

    override val size: Int
        get() = map.size

    override fun isEmpty(): Boolean {
        return map.isEmpty()
    }

    override fun containsKey(key: K): Boolean {
        return map.containsKey(key)
    }

    override fun containsValue(value: V): Boolean {
        return inverse.map.containsKey(value)
    }

    override fun get(key: K): V? {
        return map.get(key)
    }

    override val keys: MutableSet<K>
        get() = map.keys

    override val values: MutableSet<V>
        get() = inverse.map.keys

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = map.entries

    override fun put(key: K, value: V): V? {
        inverse.map.put(value, key)
        return map.put(key, value)
    }

    override fun remove(key: K): V? {
        return map.remove(key)?.also { inverse.map.remove(it) }
    }

    override fun putAll(from: Map<out K, V>) {
        inverse.map.putAll(from.invert())
        return map.putAll(from)
    }

    override fun clear() {
        inverse.map.clear()
        map.clear()
    }

    companion object {
        private fun <K, V> Map<K, V>.invert() = entries
            .groupBy { it.value }
            .mapValues { entry ->
                entry.value.also { values ->
                    assert(values.size == 1) { "All keys and values in a two-way map must be unique" }
                }.first().key
            }
    }
}
