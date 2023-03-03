package com.jkcq.homebike.mine.mvvm.viewmodel

import com.jkcq.base.base.BaseViewModel
import com.jkcq.homebike.mine.mvvm.repository.MineRepository

/**
 *  Created by BeyondWorlds
 *  on 2020/7/27
 */
class MineModel : BaseViewModel() {
    val mMineRepository by lazy { MineRepository() }
}