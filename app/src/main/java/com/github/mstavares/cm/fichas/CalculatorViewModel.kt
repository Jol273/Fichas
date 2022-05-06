package com.github.mstavares.cm.fichas

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel

class CalculatorViewModel(application: Application) : AndroidViewModel(application) {

    private val model = Calculator(
        CalculatorDatabase.getInstance(application).operationDao()
    )

    fun getDisplayValue(): String {
        return model.expression
    }

    fun onClickSymbol(symbol: String): String {
        return model.insertSymbol(symbol)
    }

    fun onClickClear(): String {
        return model.clear()
    }

    fun onClickBackspace(): String {
        return model.deleteLastSymbol()
    }

    fun onClickGetLastOperation(onFinished: (String) -> Unit) {
        model.getLastOperation(onFinished)
    }

    fun onGetHistory(onFinished: (List<OperationUi>) -> Unit) {
        model.getHistory(onFinished)
    }

    fun onDeleteOperation(uuid: String, onSuccess: () -> Unit) {
        model.deleteOperation(uuid, onSuccess)
    }

    fun onClickEquals(onSaved: () -> Unit): String {
        model.performOperation(onSaved)
        val result = getDisplayValue().toDouble()
        return if(result % 1 == 0.0) result.toLong().toString() else result.toString()
    }

}