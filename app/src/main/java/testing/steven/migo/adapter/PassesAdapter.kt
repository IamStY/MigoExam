package testing.steven.migo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_passes_adapter_item.view.*
import kotlinx.android.synthetic.main.layout_passes_header_item.view.*
import testing.steven.migo.MigoConstants.Companion.DISPLAY_FORMAT
import testing.steven.migo.R
import testing.steven.migo.datamodel.AdapterType
import testing.steven.migo.datamodel.PassDataUIModel

class PassesAdapter(
    var passUIS: ArrayList<PassDataUIModel> = ArrayList(),
    val context: Context,
    val adapterCallback: AdapterCallback
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == AdapterType.DAYS_HEADER.ordinal || viewType == AdapterType.HOUR_HEADER.ordinal) {
            HeaderViewHolder(
                LayoutInflater.from(context)
                    .inflate(R.layout.layout_passes_header_item, parent, false)
            )
        } else {
            ContentViewHolder(
                LayoutInflater.from(context)
                    .inflate(R.layout.layout_passes_adapter_item, parent, false)
            )
        }

    }

    override fun getItemId(position: Int): Long {
        return passUIS[position].id.toLong()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            if (holder.itemViewType == AdapterType.DAYS_HEADER.ordinal) {
                holder.vHeaderText.text = context.getString(R.string.day_pass)
            } else if (holder.itemViewType == AdapterType.HOUR_HEADER.ordinal) {
                holder.vHeaderText.text = context.getString(R.string.hourly_pass)
            }
        } else if (holder is ContentViewHolder) {
            var passData = passUIS[position]
            holder.itemView.setOnClickListener {
                adapterCallback.enterDetail(passData)
            }
            holder.vTitle.text = passData.name
            if (passData.expiredTime != null) {
                holder.vContent.visibility = View.VISIBLE
                holder.vActionButton.isEnabled = false
                holder.vActionButton.text = context.getString(R.string.activated)
                holder.vContent.text = String.format(
                    context.getString(R.string.expired_format),
                    DISPLAY_FORMAT.format(passData.expiredTime)
                )
            } else {
                holder.vActionButton.setOnClickListener {
                    adapterCallback.activateButtonAction(passData.id)
                }
                holder.vActionButton.isEnabled = true
                holder.vActionButton.text = context.getString(R.string.active)
                holder.vContent.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return passUIS.size
    }

    override fun getItemViewType(position: Int): Int {
        return passUIS[position].adapterType.ordinal

    }

    fun updateData(updatedPassUIS: ArrayList<PassDataUIModel>) {
        this.passUIS.clear()
        this.passUIS.addAll(updatedPassUIS)
        notifyDataSetChanged()
    }
}


class ContentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var vTitle = view.vTitle
    var vContent = view.vContent
    var vActionButton = view.vActionButton

}

class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var vHeaderText = view.vHeaderText
}

interface AdapterCallback {
    fun activateButtonAction(id: Int)
    fun enterDetail(passDataUIModel: PassDataUIModel)
}
