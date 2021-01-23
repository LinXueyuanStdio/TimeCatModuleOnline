package com.timecat.module.user.game.cube

import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.saveBlock
import com.timecat.identity.data.base.*
import com.timecat.identity.data.block.MomentBlock
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.base.BaseComplexEditorActivity
import com.xiaojinzi.component.anno.RouterAnno
import kotlinx.android.synthetic.main.user_activity_moment_add.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-13
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_CubeSkillEditorActivity)
class CubeSkillEditorActivity : BaseComplexEditorActivity() {

    override fun title(): String = "方块技能"
    override fun routerInject() = NAV.inject(this)

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
    }

    override fun publish(content: String, attachments: AttachmentTail?) {
        val block = Block.forMoment(I(), content)
        block.privacy = PrivacyScope(isPrivate = true)
        block.structure = MomentBlock(
            mediaScope = attachments,
            atScope = AtScope(emojiEditText.realUserList.map {
                AtItem(it.user_name, it.user_id)
            }.toMutableList()),
            topicScope = TopicScope(emojiEditText.realTopicList.map {
                TopicItem(it.topicName, it.topicId)
            }.toMutableList()),
        ).toJson()
        saveBlock {
            target = block
            onError = errorCallback
            onSuccess = {
            }
        }
    }
}