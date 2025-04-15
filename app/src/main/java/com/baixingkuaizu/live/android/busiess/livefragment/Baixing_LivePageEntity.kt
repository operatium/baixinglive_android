package com.baixingkuaizu.live.android.busiess.livefragment

import com.baixingkuaizu.live.android.base.Baixing_Entity

data class Baixing_LivePageEntity(
    val id:String,
    val page:Int,
    val data: ArrayList<Baixing_LiveDataEntity>,
):Baixing_Entity()
