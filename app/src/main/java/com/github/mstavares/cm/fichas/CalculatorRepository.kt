package com.github.mstavares.cm.fichas

import android.content.Context
import java.lang.IllegalStateException

class CalculatorRepository(private val context: Context, private val local: Calculator, private val remote: Calculator) {

    fun getExpression() = local.expression

    fun insertSymbol(symbol: String): String{
        return remote.insertSymbol(symbol)
    }

    fun clear(): String{
        return remote.clear()
    }

    fun deleteLastSymbol(): String{
        return remote.deleteLastSymbol()
    }

    fun performOperation(onFinished: () -> Unit) {
        remote.performOperation{
            local.expression = remote.expression
            onFinished()
        }
    }

    fun getLastOperation(onFinished: (String) -> Unit){
        if (ConnectivityUtil.isOnline(context)){
            remote.getLastOperation(onFinished)
        } else {
            local.getLastOperation(onFinished)
        }
    }

    fun deleteOperation(uuid: String, onSuccess: () -> Unit){
        remote.deleteOperation(uuid, onSuccess)
    }

    fun getHistory(onFinished: (List<OperationUi>) -> Unit){
        if (ConnectivityUtil.isOnline(context)){
            remote.getHistory{
                history -> local.deleteAllOperations {
                    local.insertOperations(history) {
                        onFinished(history)
                    }
                }
            }
        } else {
            local.getHistory(onFinished)
        }
    }

    companion object {

        private var instance: CalculatorRepository? = null

        fun init(context: Context, local: Calculator,remote: Calculator){
            synchronized(this){
                if (instance == null){
                    instance = CalculatorRepository(context, local, remote)
                }
            }
        }

        fun getInstance(): CalculatorRepository{
            return instance ?: throw IllegalStateException("Repository not initialized")
        }

    }

}