package com.baixingkuaizu.live.android.busiess.profilefragment

import com.baixingkuaizu.live.android.base.Baixing_Entity

data class Baixing_MenuItem(
    var baixing_iconResId: Int = 0,
    var baixing_title: String = "",
    var baixing_action: String = ""
) : Baixing_Entity()