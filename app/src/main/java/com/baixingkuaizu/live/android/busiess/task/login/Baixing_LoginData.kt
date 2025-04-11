package com.baixingkuaizu.live.android.busiess.task.login

import com.baixingkuaizu.live.android.base.Baixing_Entity

data class Baixing_LoginData(
    var mBaixing_token: String = "",
    val mBaixing_phone: String = "",
): Baixing_Entity()
