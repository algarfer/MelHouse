package com.uniovi.melhouse.factories.presentation.viewholder

import android.view.View
import com.uniovi.melhouse.presentation.viewholder.NextTasksViewHolder
import dagger.assisted.AssistedFactory

@AssistedFactory
interface NextTasksViewHolderFactory {

    fun create(view: View): NextTasksViewHolder

}