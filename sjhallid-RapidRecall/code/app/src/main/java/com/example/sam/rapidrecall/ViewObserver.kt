package com.example.sam.rapidrecall

interface ViewObserver<M> {
    fun update( model: M )
}