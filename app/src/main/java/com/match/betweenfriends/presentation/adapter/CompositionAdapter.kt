package com.match.betweenfriends.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.match.betweenfriends.data.model.Player
import com.match.betweenfriends.databinding.TeamViewBinding

class CompositionAdapter : ListAdapter<Player, CompositionAdapter.VH>(DiffUtil()) {

    lateinit var onPlayerNameEdit: (name: String, player: Player) -> Unit

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Player>() {
        override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean {
            return oldItem == newItem
        }
    }

    class VH(val binding: TeamViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(
            TeamViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: VH, position: Int) {
        val player = currentList[position]
        val number =
            if (position < 9) "0${position + 1}. " else "${position + 1}. "
        holder.binding.apply {
            tvNumber.text = number
            edtName.setText(player.playerName)

            edtName.doAfterTextChanged {
                it?.let {
                    onPlayerNameEdit(it.toString(), player)
                }
            }
        }
    }
}