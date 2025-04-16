package com.baixingkuaizu.live.android.busiess.followfragment

import com.baixingkuaizu.live.android.base.Baixing_Entity

class Baixing_FollowGirlEntity (
    var id: Int,
    var name: String,
    var image: String,
    var isOnLine: Boolean,
    var attention: Int,
): Baixing_Entity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Baixing_FollowGirlEntity

        if (id != other.id) return false
        if (isOnLine != other.isOnLine) return false
        if (attention != other.attention) return false
        if (name != other.name) return false
        if (image != other.image) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + isOnLine.hashCode()
        result = 31 * result + attention
        result = 31 * result + name.hashCode()
        result = 31 * result + image.hashCode()
        return result
    }
}