package com.timecat.module.plugin.picturebed

import android.annotation.TargetApi
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.timecat.module.plugin.R

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/6/10
 * @description null
 * @usage null
 */
class PictureBedView : LinearLayout {
    private lateinit var root: View
    lateinit var recyclerView: RecyclerView

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, layout: Int) : super(context) {
        init(context, layout)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    @TargetApi(21)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        init(context)
    }

    private fun init(context: Context, layout: Int = R.layout.plugin_picture_header) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        root = inflater.inflate(layout, null)
        orientation = VERTICAL
        val params = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        root.layoutParams = params

        recyclerView = root.findViewById(R.id.rv)

        addView(root)
    }
}