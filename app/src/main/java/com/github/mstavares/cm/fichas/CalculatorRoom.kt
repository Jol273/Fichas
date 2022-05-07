package com.github.mstavares.cm.fichas

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class CalculatorRoom(private val dao: OperationDao) : Calculator() {

    override fun performOperation(onSaved: () -> Unit) {
        val currentExpression = expression
        super.performOperation {
            val operation = OperationRoom(expression = currentExpression,
                result = expression.toDouble(), timestamp = Date().time)
            CoroutineScope(Dispatchers.IO).launch { dao.insert(operation) }
            onSaved()
        }
    }

    override fun insertOperations(operations: List<OperationUi>, onFinished: (List<OperationUi>) -> Unit)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val history = operations.map {
                OperationRoom(it.uuid, it.expression, it.result, it.timestamp)
            }
            dao.insertAll(history)
            onFinished(operations)
        }
    }

    override fun getLastOperation(onFinished: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            getHistory { onFinished(it.last().expression) }
        }
    }

    override fun deleteOperation(uuid: String, onSuccess: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = dao.delete(uuid)
            if(result == 1) onSuccess()
        }
    }
    override fun deleteAllOperations(onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteAll()
            onFinished()
        }
    }

    override fun getHistory(onFinished: (List<OperationUi>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val operations = dao.getAll()
            onFinished(operations.map{
                OperationUi(it.uuid,it.expression,it.result,it.timestamp)
            })
        }
    }
}
