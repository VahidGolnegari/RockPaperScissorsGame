package com.vahidgolnegari.rpcgame

import android.content.res.Resources
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

fun Double.calculateCos(): Double = cos(this * (PI / 180.0))
fun Double.calculateSin(): Double = sin(this * (PI / 180.0))
fun Int.randomNumberForThis(): Int = Random.nextInt(0, this)

