package com.uniovi.melhouse.presentation.viewholder

import android.view.View
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.model.User
import com.uniovi.melhouse.data.model.getInitials
import com.uniovi.melhouse.databinding.FlatPartnerLayoutBinding
import com.uniovi.melhouse.utils.makeGone
import com.uniovi.melhouse.utils.makeVisible
import com.uniovi.melhouse.viewmodel.FlatFragmentViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class PartnersViewHolder @AssistedInject constructor(
    @Assisted private val view: View,
    @Assisted private val viewModel: FlatFragmentViewModel,
    @Assisted private val user: User
) : AbstractViewHolder<User>(view) {

    private val binding = FlatPartnerLayoutBinding.bind(view)
    private lateinit var observer: Observer<Boolean>

    override fun render(item: User) {
        binding.profileLayout.tvProfile.text = item.getInitials()
        binding.tvUserName.text = item.name

        observer = Observer {
            if (canShowButtons(item, user, it)) {
                binding.btnKick.makeVisible()
                binding.btnAscend.makeVisible()
            } else {
                binding.btnKick.makeGone()
                binding.btnAscend.makeGone()
            }
        }

        viewModel.isAdmin.observeForever(observer)

        binding.btnKick.setOnClickListener {
            MaterialAlertDialogBuilder(view.context)
                .setTitle(view.resources.getString(R.string.partner_kick_dialog_title))
                .setMessage(view.resources.getString(R.string.partner_kick_dialog_supporting_text, item.name))
                .setNeutralButton(view.resources.getString(R.string.cancel)) { _, _ -> }
                .setPositiveButton(view.resources.getString(R.string.continuar)) { _, _ ->
                    viewModel.kickUser(item)
                }
                .show()
        }

        binding.btnAscend.setOnClickListener {
            MaterialAlertDialogBuilder(view.context)
                .setTitle(view.resources.getString(R.string.partner_promote_dialog_title))
                .setMessage(view.resources.getString(R.string.partner_promote_dialog_supporting_text, item.name))
                .setNeutralButton(view.resources.getString(R.string.cancel)) { _, _ -> }
                .setPositiveButton(view.resources.getString(R.string.continuar)) { _, _ ->
                    viewModel.promoteToAdmin(item)
                }
                .show()
        }
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        viewModel.done.removeObserver(observer)
    }

    private fun canShowButtons(displayedUser: User, currentUser: User, isAdmin: Boolean): Boolean {
        if(displayedUser.id == currentUser.id) return false
        return isAdmin
    }
}
