package com.timecat.module.user.record

import com.timecat.data.bmob.data.User

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/8/4
 * @description Record
 * @usage null
 */

const val BLOCK_PERM_no_access = 0
const val BLOCK_PERM_can_view = 1
const val BLOCK_PERM_can_comment = 2
const val BLOCK_PERM_can_edit = 3
const val BLOCK_PERM_full_access = 4
class PermissionController(owner: User) {

}