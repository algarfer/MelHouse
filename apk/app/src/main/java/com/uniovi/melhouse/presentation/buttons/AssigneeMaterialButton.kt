package com.uniovi.melhouse.presentation.buttons

import android.content.Context
import com.google.android.material.button.MaterialButton
import com.uniovi.melhouse.data.model.User

class AssigneeMaterialButton(context: Context) : MaterialButton(context) {

    lateinit var asignee: User

    constructor(context: Context, asignee: User) : this(context) {
        this.asignee = asignee
    }
}