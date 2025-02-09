package com.hoicham.orc.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.hoicham.orc.core.utils.dateAsString
import com.hoicham.orc.database.entity.Scan
import com.hoicham.orc.databinding.ModelPinnedHeaderBinding
import com.hoicham.orc.databinding.ModelScanHeaderBinding
import com.hoicham.orc.databinding.ModelScanLoadingBarBinding
import com.hoicham.orc.databinding.ModelScanTopBarBinding
import com.hoicham.orc.databinding.ScanListItemBinding

// Data class representing different view types
sealed class ScanListItem {
    data class TopBar(val onInfoClicked: () -> Unit) : ScanListItem()
    data class Header(val numOfScans: String) : ScanListItem()
    data class ListHeader(val headerTitle: String) : ScanListItem()
    data class ScanItem(val scan: Scan) : ScanListItem()
    data object Loading : ScanListItem()
}

class ScanListAdapter(
    private val onScanClicked: (Scan) -> Unit,
    private val onInfoClicked: () -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<ScanListItem> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<ScanListItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ScanListItem.TopBar -> VIEW_TYPE_TOP_BAR
            is ScanListItem.Header -> VIEW_TYPE_HEADER
            is ScanListItem.ListHeader -> VIEW_TYPE_LIST_HEADER
            is ScanListItem.ScanItem -> VIEW_TYPE_SCAN_ITEM
            is ScanListItem.Loading -> VIEW_TYPE_LOADING
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_TOP_BAR -> TopBarViewHolder(
                ModelScanTopBarBinding.inflate(inflater, parent, false)
            )

            VIEW_TYPE_HEADER -> HeaderViewHolder(
                ModelScanHeaderBinding.inflate(inflater, parent, false)
            )

            VIEW_TYPE_LIST_HEADER -> ListHeaderViewHolder(
                ModelPinnedHeaderBinding.inflate(inflater, parent, false)
            )

            VIEW_TYPE_SCAN_ITEM -> ScanItemViewHolder(
                ScanListItemBinding.inflate(inflater, parent, false)
            )

            VIEW_TYPE_LOADING -> LoadingViewHolder(
                ModelScanLoadingBarBinding.inflate(inflater, parent, false)
            )

            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is ScanListItem.TopBar -> (holder as TopBarViewHolder).bind(item)
            is ScanListItem.Header -> (holder as HeaderViewHolder).bind(item)
            is ScanListItem.ListHeader -> (holder as ListHeaderViewHolder).bind(item)
            is ScanListItem.ScanItem -> (holder as ScanItemViewHolder).bind(item)
            is ScanListItem.Loading -> (holder as LoadingViewHolder).bind()
        }
    }

    override fun getItemCount() = items.size

    inner class TopBarViewHolder(
        private val binding: ModelScanTopBarBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ScanListItem.TopBar) {
            binding.imageViewInfo.setOnClickListener { item.onInfoClicked() }
        }
    }

    inner class HeaderViewHolder(
        private val binding: ModelScanHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ScanListItem.Header) {
            binding.textViewNumOfScans.text = item.numOfScans
        }
    }

    inner class ListHeaderViewHolder(
        private val binding: ModelPinnedHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ScanListItem.ListHeader) {
            binding.textViewListHeader.text = item.headerTitle
        }
    }

    inner class ScanItemViewHolder(
        private val binding: ScanListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ScanListItem.ScanItem) {
            val scan = item.scan
            val title = scan.scanTitle.ifEmpty { scan.scanText.lines()[0] }

            binding.apply {
                textViewDate.text = dateAsString(scan.dateModified)
                textViewTitle.text = title
                textViewContent.text = scan.scanText
                card.setOnClickListener { onScanClicked(scan) }
                imageViewPinned.isVisible = scan.isPinned
            }
        }
    }

    inner class LoadingViewHolder(
        binding: ModelScanLoadingBarBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {} // Nothing to bind
    }

    companion object {
        private const val VIEW_TYPE_TOP_BAR = 0
        private const val VIEW_TYPE_HEADER = 1
        private const val VIEW_TYPE_LIST_HEADER = 2
        private const val VIEW_TYPE_SCAN_ITEM = 3
        private const val VIEW_TYPE_LOADING = 4
    }
}