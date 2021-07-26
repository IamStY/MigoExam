package testing.steven.migo.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import testing.steven.migo.MigoConstants
import testing.steven.migo.R
import testing.steven.migo.adapter.AdapterCallback
import testing.steven.migo.adapter.PassesAdapter
import testing.steven.migo.datamodel.PassDataUIModel
import testing.steven.migo.datamodel.PassType
import testing.steven.migo.viewmodels.ContextViewModelFactory
import testing.steven.migo.viewmodels.MainActivityViewModel

class MainActivity : AppCompatActivity(), AdapterCallback {
    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViewModel()
        initView()
        updateAdapter()
    }

    private fun initView() {
        vSubmit.setOnClickListener {
            var inputNumber: Int? = vEditInput.text.toString().toIntOrNull()
            if (inputNumber == null || inputNumber == 0) {
                Toast.makeText(this, getString(R.string.input_format_error), Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            var type = PassType.DAILY
            if (vRadioHour.isChecked) {
                type = PassType.HOURLY
            }
            submit(inputNumber, type)
        }
    }

    private fun submit(inputNumber: Int, type: PassType) {
        mainActivityViewModel.insertPassToDatabase(this, inputNumber, type)
    }

    private fun updateAdapter(passUIS: ArrayList<PassDataUIModel> = ArrayList()) {
        var adapter = vRecycler.adapter as PassesAdapter?
        if (adapter == null) {
            adapter = PassesAdapter(context = this, adapterCallback = this)
            adapter.setHasStableIds(true)
            var layoutManager = LinearLayoutManager(this)
            vRecycler.adapter = adapter
            vRecycler.layoutManager = layoutManager
        } else {
            adapter.updateData(passUIS)
        }
    }

    private fun initViewModel() {
        var vmFactory = ContextViewModelFactory(this)
        mainActivityViewModel =
            ViewModelProvider(this, vmFactory).get(MainActivityViewModel::class.java)
        mainActivityViewModel.migoResponse.observe(this) { migoResponse ->
            vNetworkResponse.text = migoResponse
        }
        mainActivityViewModel.adapterListData.observe(this) { passesList ->
            updateAdapter(passesList)
        }
    }

    override fun activateButtonAction(id: Int) {
        mainActivityViewModel.activatePass(id)
    }

    override fun enterDetail(passDataUIModel: PassDataUIModel) {
        startActivity(Intent(this, DetailActivity::class.java).apply {
            putExtra(MigoConstants.PASS_DATA_BUNDLE_KEY, passDataUIModel)
        })
    }
}
