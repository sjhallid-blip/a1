package com.example.sam.rapidrecall

// This interface is implemented by every model that should be observed, such as the HistoryModel
abstract class ObservableModel<M> {
    private val observers = mutableListOf< ViewObserver<M> >()
    // “all models keep track of their views”

    fun addObserver( o: ViewObserver<M> ) {
        observers.add(o)
    }
    fun removeObserver( o: ViewObserver <M> ) {
        observers.remove(o)
    }
    // “all models notify their views to update”
    protected fun notifyObservers( model: M ) {
        for(i in observers){
            i.update(model)
        }
    }
}
