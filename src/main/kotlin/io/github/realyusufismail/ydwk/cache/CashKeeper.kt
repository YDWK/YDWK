package io.github.realyusufismail.ydwk.cache

interface CashKeeper<Class> {

    val asArray: Array<Class>

    fun get(id: Long): Class?

    fun get(id : String) {
        get(id.toLong())
    }
}