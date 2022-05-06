package com.github.mstavares.cm.fichas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.mstavares.cm.fichas.databinding.ItemExpressionBinding

class HistoryAdapter(
    private var items: List<OperationUi> = listOf(),
    private val onClick: (OperationUi) -> Unit,
    private val onLongClick: (OperationUi) -> Boolean
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(val binding: ItemExpressionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(ItemExpressionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.binding.textUuid.text = items[position].uuid
        holder.binding.textExpression.text = items[position].expression
        holder.binding.textResult.text = items[position].result.toString()
        holder.itemView.setOnClickListener { onClick(items[position]) }
        holder.itemView.setOnLongClickListener { onLongClick(items[position]) }
    }

    override fun getItemCount() = items.size

    fun updateItems(items: List<OperationUi>) {
        this.items = items
        notifyDataSetChanged()
    }

}
