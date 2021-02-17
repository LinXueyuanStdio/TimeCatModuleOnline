package com.timecat.module.user.game.cube

import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.base.BaseComplexEditorActivity
import com.timecat.module.user.game.cube.add.BaseCubeAddActivity
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-13
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_CubeSkillEditorActivity)
class CubeSkillEditorActivity : BaseCubeAddActivity() {
    override fun title(): String = "技能"
    override fun release() {
        TODO("Not yet implemented")
    }

    override fun currentBlock(): Block? {
        TODO("Not yet implemented")
    }

    override fun subtype(): Int {
        TODO("Not yet implemented")
    }

    override fun savableBlock(): Block {
        TODO("Not yet implemented")
    }

    override fun updatableBlock(): Block.() -> Unit {
        TODO("Not yet implemented")
    }

}