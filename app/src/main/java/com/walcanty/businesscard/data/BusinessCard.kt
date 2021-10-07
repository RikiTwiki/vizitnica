package com.walcanty.businesscard.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity
data class BusinessCard(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    val name:String,
    val company:String,
    val phone:String,
    val email:String,
    val background:String
): Parcelable