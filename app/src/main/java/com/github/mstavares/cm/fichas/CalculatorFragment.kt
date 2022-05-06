package com.github.mstavares.cm.fichas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mstavares.cm.fichas.databinding.FragmentCalculatorBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.objecthunter.exp4j.ExpressionBuilder

class CalculatorFragment : Fragment() {

    private lateinit var binding: FragmentCalculatorBinding
    private lateinit var viewModel: CalculatorViewModel
    private var adapter = HistoryAdapter(onClick = ::onOperationClick, onLongClick = ::onOperationLongClick)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = getString(R.string.calculator)
        val view = inflater.inflate(R.layout.fragment_calculator, container, false)
        viewModel = ViewModelProvider(this).get(CalculatorViewModel::class.java)
        binding = FragmentCalculatorBinding.bind(view)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.rvHistory?.layoutManager = LinearLayoutManager(context)
        binding.rvHistory?.adapter = adapter
        viewModel.onGetHistory { updateHistory(it) }
        binding.textVisor.text = viewModel.getDisplayValue()
        binding.button0.setOnClickListener { onClickSymbol("0") }
        binding.button00.setOnClickListener { onClickSymbol("00") }
        binding.button1.setOnClickListener { onClickSymbol("1") }
        binding.button2.setOnClickListener { onClickSymbol("2") }
        binding.button3.setOnClickListener { onClickSymbol("3") }
        binding.button4.setOnClickListener { onClickSymbol("4") }
        binding.button5.setOnClickListener { onClickSymbol("5") }
        binding.button6.setOnClickListener { onClickSymbol("6") }
        binding.button7.setOnClickListener { onClickSymbol("7") }
        binding.button8.setOnClickListener { onClickSymbol("8") }
        binding.button9.setOnClickListener { onClickSymbol("9") }
        binding.buttonDot.setOnClickListener { onClickSymbol(".") }
        binding.buttonAdition.setOnClickListener { onClickSymbol("+") }
        binding.buttonSubtraction.setOnClickListener { onClickSymbol("-") }
        binding.buttonMultiplication.setOnClickListener { onClickSymbol("*") }
        binding.buttonDivision.setOnClickListener { onClickSymbol("/") }
        binding.buttonClear.setOnClickListener { onClickClear() }
        binding.buttonBackspace.setOnClickListener { onClickBackspace() }
        binding.buttonPrev.setOnClickListener { onClickGetPreviousOperation() }
        binding.buttonEquals.setOnClickListener { onClickEquals() }
    }

    private fun onClickSymbol(symbol: String) {
        val displayUpdated = viewModel.onClickSymbol(symbol)
        binding.textVisor.text = displayUpdated
    }

    private fun onClickEquals() {
        val displayUpdated = viewModel.onClickEquals {
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(context, getString(R.string.registry_saved), Toast.LENGTH_LONG).show()
                viewModel.onGetHistory { updateHistory(it) }
            }
        }
        binding.textVisor.text = displayUpdated
    }

    private fun onClickClear() {
        val displayUpdated = viewModel.onClickClear()
        binding.textVisor.text = displayUpdated
    }

    private fun onClickBackspace() {
        val displayUpdated = viewModel.onClickBackspace()
        binding.textVisor.text = displayUpdated
    }

    private fun onClickGetPreviousOperation() {
        viewModel.onClickGetLastOperation {
            binding.textVisor.text = it
        }
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
            adapter.updateItems(history)
        }
    }

}