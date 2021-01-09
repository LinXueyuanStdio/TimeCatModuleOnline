package com.timecat.module.user.social.app.add

import com.timecat.identity.readonly.RouterHub
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/6/11
 * @description 上传 一个 iOS 应用
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AddiOSAppActivity)
class AddiOSAppActivity : BaseAddAppActivity() {
    override fun appBlockStructure(): String {
        TODO("Not yet implemented")
    }
}