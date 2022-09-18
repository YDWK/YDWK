package io.github.realyusufismail.ydwk.cache

import gnu.trove.map.TLongObjectMap
import gnu.trove.map.hash.TLongObjectHashMap
import kotlin.reflect.KClass

class CashKeeperImpl<OBJECT : KClass<OBJECT>> (private val objectAsClass : KClass<OBJECT>) : CashKeeper<OBJECT> {
    val map : TLongObjectMap<OBJECT> = TLongObjectHashMap()


    override val asArray: Array<OBJECT>
        get() = TODO("Not yet implemented")

    override fun get(id: Long): OBJECT? {
        return map[id]
    }
}