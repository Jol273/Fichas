package com.github.mstavares.cm.fichas

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.objecthunter.exp4j.ExpressionBuilder
import java.util.*

//class Calculator(private val dao: OperationDao) {

abstract class Calculator {

    var expression: String = "0"
    //private val history = mutableListOf<Operation>()

    fun insertSymbol(symbol: String): String {
        expression = if(expression == "0") symbol else "$expression$symbol"
        return expression
    }

    fun clear(): String {
        expression = "0"
        return expression
    }

    fun deleteLastSymbol(): String {
        expression = if(expression.length > 1) expression.dropLast(1) else "0"
        return expression
    }

    abstract fun getLastOperation(onFinished: (String) -> Unit)
    /*{
        CoroutineScope(Dispatchers.IO).launch {
            val history = dao.getAll()
            //Thread.sleep(10 * 1000)
            expression = if (history.isNotEmpty()) history[history.size - 1].expression else expression
            onFinished(expression)
        }
    }
    */

    abstract fun deleteAllOperations(onFinished: () -> Unit)

    abstract fun deleteOperation(uuid: String, onSuccess: () -> Unit)
    /*{
        CoroutineScope(Dispatchers.IO).launch {
            val history = dao.getAll()
            Thread.sleep(10 * 1000)
            val operation = history.find { it.uuid == uuid }
            /*dao.
            history.remove(operation)*/
            onSuccess()
        }
    }*/

    open fun performOperation(onSaved: () -> Unit) {
        val expressionBuilder = ExpressionBuilder(expression).build()
        val result = expressionBuilder.evaluate()
        expression = result.toString()
        //val operation = OperationRoom(expression = expression, result = result, timestamp = Date().time)
        onSaved()
        /*CoroutineScope(Dispatchers.IO).launch {
            dao.insert(operation)
            onSaved()
        }*/
    }

    abstract fun insertOperations(operations: List<OperationUi>, onFinished: (List<OperationUi>) -> Unit)

    abstract fun getHistory(onFinished: (List<OperationUi>) -> Unit)
    /* {
        CoroutineScope(Dispatchers.IO).launch {
            //Thread.sleep(10 * 1000)
            val operations = dao.getAll()
            onFinished(operations.map {
                OperationUi(it.uuid,it.expression, it.result, it.timestamp)
            })
        }
    }*/

    /*private fun addToHistory(operation: OperationRoom) {
        Thread.sleep(10 * 1000)
        dao.insert(operation)
    }*/

}
