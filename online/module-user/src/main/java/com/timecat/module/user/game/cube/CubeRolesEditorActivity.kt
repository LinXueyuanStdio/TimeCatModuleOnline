package com.timecat.module.user.game.cube

import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.base.BaseComplexEditorActivity
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-13
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_CubeRolesEditorActivity)
class CubeRolesEditorActivity : BaseComplexEditorActivity() {
    override fun release() {
        TODO("Not yet implemented")
    }
    override fun title(): String = "角色"

}