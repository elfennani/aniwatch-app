package com.elfennani.aniwatch.utils

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache

fun Context.imageLoader() = ImageLoader.Builder(this)
    .memoryCache {
        MemoryCache.Builder(this)
            .maxSizePercent(0.25)
            .build()
    }
    .diskCache {
        DiskCache.Builder()
            .directory(this.cacheDir.resolve("image_cache"))
            .maxSizePercent(0.02)
            .build()
    }
    .crossfade(true)
    .build()