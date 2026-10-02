package com.example.sam.rapidrecall

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


//class MyView(myController: MyController ): ViewObserver<MyModel> {
//    var currentCount by { mutableIntStateOf(0) };
//
//    override fun update(newModel: MyModel) {
//        currentCount = newModel.count;
//    }
//
//    @Composable
//    fun Content() {
//        Text(currentCount)
//        Button(onClick = {
//            myController.increaseCount()
//        })
//    }
//}
//
//class MyController(model: MyModel) {
//    fun increaseCount() {
//        model.setCount(model.count + 1)
//    }
//}
//
//class MainActivity() {
//    val viewInstance = MyView()
//    val viewModel = MyModel()
//    val viewController = MyController(viewModel)
//    viewModel.addObserver(viewInstance)
//
//    viewModel.setCount(2)
//
//    onCreate() {
//        Scaffold() {
//            viewInstance.Content()
//        }
//    }
//}
