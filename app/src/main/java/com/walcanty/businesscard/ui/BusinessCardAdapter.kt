package com.walcanty.businesscard.ui

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.dhaval2404.colorpicker.util.ColorUtil
import com.google.android.material.card.MaterialCardView
import com.walcanty.businesscard.R
import com.walcanty.businesscard.data.BusinessCard
import com.walcanty.businesscard.databinding.ItemBusinessCardBinding


class BusinessCardAdapter :
    ListAdapter<BusinessCard, BusinessCardAdapter.viewHolder>(DiffCalback()) {

    var listenerShare: (View) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBusinessCardBinding.inflate(inflater, parent, false)

        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.cardView.animation =
            AnimationUtils.loadAnimation(holder.cardView.context, R.anim.anim_item)
        holder.bind(getItem(position))

    }


    inner class viewHolder(
        private val binding: ItemBusinessCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        var cardView: MaterialCardView = binding.mcvContent
        fun bind(item: BusinessCard) {
            binding.tvNome.apply {
                text = item.name
                setTextColor(getContrastColor(Color.parseColor(item.background)))
            }
            binding.tvTelefone.apply {
                text = item.phone
                setTextColor(getContrastColor(Color.parseColor(item.background)))
            }
            binding.tvEmail.apply {
                text = item.email
                setTextColor(getContrastColor(Color.parseColor(item.background)))
            }
            binding.tvNomeEmpresa.apply {
                text = item.company
                setTextColor(getContrastColor(Color.parseColor(item.background)))
            }
            binding.mcvContent.setCardBackgroundColor(Color.parseColor(item.background))

            binding.mcvContent.setOnLongClickListener {
                listenerShare(it)
                true
                //Log.e("TAG","Click long")
            }

        }

    }

}

@ColorInt
fun getContrastColor(@ColorInt color: Int): Int {
    val whiteContrast = ColorUtils.calculateContrast(Color.WHITE, color)
    val blackContrast = ColorUtils.calculateContrast(Color.BLACK, color)

    return if (whiteContrast > blackContrast) Color.WHITE else Color.BLACK
}


class DiffCalback : DiffUtil.ItemCallback<BusinessCard>() {

    override fun areItemsTheSame(oldItem: BusinessCard, newItem: BusinessCard) = oldItem == newItem

    override fun areContentsTheSame(oldItem: BusinessCard, newItem: BusinessCard) =
        oldItem.id == newItem.id

}