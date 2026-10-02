package com.example.sam.rapidrecall

// This interface is for all views that want to subscribe to any models
interface ViewObserver<M> {
    fun update( model: M )
}