package com.template

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class History(
    @PrimaryKey
    val id : UUID = UUID.randomUUID(),
    val comba : String,
    val combaMoney : Int,
)
