package com.github.mstavares.cm.fichas

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mstavares.cm.fichas.databinding.FragmentCalculatorBinding
import com.github.mstavares.cm.fichas.databinding.FragmentHistoryBinding
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HistoryFragment : Fragment() {

    private val TAG = HistoryFragment::class.java.simpleName
    private lateinit var viewModel: CalculatorViewModel
    private var adapter = HistoryAdapter(onClick = ::onOperationClick, onLongClick = ::onOperationLongClick)
    private lateinit var binding: FragmentHistoryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = getString(R.string.history)
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        viewModel = ViewModelProvider(this).get(CalculatorViewModel::class.java)
        binding = FragmentHistoryBinding.bind(view)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.rvHistory.layoutManager = LinearLayoutManager(context)
        binding.rvHistory.adapter = adapter
        //getAllOperationsWs { operations-> Log.i(TAG, operations.toString())}
        viewModel.onGetHistory { updateHistory(it) }
    }

    private fun onOperationClick(operation: OperationUi) {
        NavigationManager.goToOperationDetail(parentFragmentManager, operation)
    }

    private fun onOperationLongClick(operation: OperationUi): Boolean {
        Toast.makeText(context, getString(R.string.deleting), Toast.LENGTH_SHORT).show()
        viewModel.onDeleteOperation(operation.uuid) { viewModel.onGetHistory { updateHistory(it) } }
        return false
    }

    private fun updateHistory(operations: List<OperationUi>) {
        val history = operations.map { OperationUi(it.uuid, it.expression, it.result, it.timestamp) }
        CoroutineScope(Dispatchers.Main).launch {
            showHistory(history.isNotEmpty())
            adapter.updateItems(history)
        }
    }



    private fun showHistory(show: Boolean) {
        if (show) {
            binding.rvHistory.visibility = View.VISIBLE
            binding.textNoHistoryAvailable.visibility = View.GONE
        } else {
            binding.rvHistory.visibility = View.GONE
            binding.textNoHistoryAvailable.visibility = View.VISIBLE
        }
    }

    private fun getAllOperationsWs(callback: (List<OperationUi>) -> Unit) {
        data class GetAllOperationsResponse(val uuid: String, val expression: String, val result: Double, val timestamp: Long)

        CoroutineScope(Dispatchers.IO).launch {
            val request: Request = Request.Builder()
                .url("https://cm-calculatora.herokuapp.com/api/operations")
                .addHeader("apikey",
                    "8270435acfead39ccb03e8aafbf37c49359dfbbcac4ef4769ae82c9531da0e17")
                .build()

            val response = OkHttpClient().newCall(request).execute().body
            if(response != null) {
                val responseObj = Gson().fromJson(response.string(),
                    Array<GetAllOperationsResponse>::class.java).toList()

                callback(
                    responseObj.map {
                        OperationUi(it.uuid,it.expression,it.result,it.timestamp)
                    })
            }
        }
    }

}