package ru.karasevm.privatednstoggle.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.materialswitch.MaterialSwitch
import ru.karasevm.privatednstoggle.R
import ru.karasevm.privatednstoggle.model.DnsServer


class ServerListRecyclerAdapter :
    RecyclerView.Adapter<ServerListRecyclerAdapter.DnsServerViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DnsServerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_row, parent, false)
        return DnsServerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    var onItemClick: ((Int) -> Unit)? = null
    var onItemSwitch: ((Int, Boolean) -> Unit)? = null
    private var items: MutableList<DnsServer> = mutableListOf()

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: DnsServerViewHolder, position: Int) {
        val item = items[position]
        if (item.label.isNotEmpty()) {
            holder.labelTextView.text = item.label
            holder.labelTextView.visibility = View.VISIBLE
        } else {
            holder.labelTextView.visibility = View.GONE
        }
        holder.serverTextView.text = item.server
        holder.id = item.id
        holder.dnsSwitch.setOnCheckedChangeListener(null)
        holder.dnsSwitch.isChecked = item.enabled
        holder.dnsSwitch.setOnCheckedChangeListener { _, isChecked ->
            onItemSwitch?.invoke(item.id, isChecked)
        }
    }

    class DiffCallback(
        private val oldList: List<DnsServer>, private var newList: List<DnsServer>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem.server == newItem.server && oldItem.label == newItem.label && oldItem.enabled == newItem.enabled
        }
    }

    /**
     *  Submit list to adapter
     *  @param list list to submit
     */
    fun submitList(list: List<DnsServer>) {
        val diffCallback = DiffCallback(items, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items.clear()
        items.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class DnsServerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val labelTextView: TextView = view.findViewById(R.id.labelTextView)
        val serverTextView: TextView = view.findViewById(R.id.textView)
        val dnsSwitch: MaterialSwitch = view.findViewById(R.id.dns_switch)
        private val serverInfoLayout: View = view.findViewById(R.id.server_info_layout)
        var id = 0

        init {
            serverInfoLayout.setOnClickListener {
                onItemClick?.invoke(id)
            }
        }
    }

}
