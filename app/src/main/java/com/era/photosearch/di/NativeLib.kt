package com.era.photosearch.di

class NativeLib {
    external fun getApiKey(): String

    init {
        System.loadLibrary("keys")
    }
}