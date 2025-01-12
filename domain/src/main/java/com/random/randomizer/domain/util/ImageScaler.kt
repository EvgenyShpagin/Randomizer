package com.random.randomizer.domain.util

import com.random.randomizer.domain.model.Image

interface ImageScaler {
    fun scale(image: Image, minSideSize: Int): Image
}