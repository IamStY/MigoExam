package testing.steven.migo.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail_pass.*
import testing.steven.migo.MigoConstants
import testing.steven.migo.MigoConstants.Companion.PASS_DATA_BUNDLE_KEY
import testing.steven.migo.R
import testing.steven.migo.datamodel.PassDataUIModel

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail_pass)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val data = intent.getParcelableExtra<PassDataUIModel>(PASS_DATA_BUNDLE_KEY)
        data ?: return
        initViewAndData(data)

    }

    private fun initViewAndData(passDataUIModel: PassDataUIModel) {
        vTitle.text = passDataUIModel.name
        if (passDataUIModel.activeTime != null) {
            vStatus.text = getString(R.string.activated)
            vActiveTime.text = String.format(
                getString(R.string.active_format),
                MigoConstants.DISPLAY_FORMAT.format(passDataUIModel.activeTime)
            )
            vExpiredTime.text = String.format(
                getString(R.string.expired_format),
                MigoConstants.DISPLAY_FORMAT.format(passDataUIModel.expiredTime)
            )

        } else {
            vExpiredTime.text = String.format(
                getString(R.string.expired_format),
                getString(R.string.not_available),
            )
            vStatus.text = getString(R.string.not_active)
            vActiveTime.text =
                String.format(getString(R.string.insert_format), getString(R.string.not_available))
        }

        vInsertionTime.text =
            String.format(
                getString(R.string.insert_format),
                MigoConstants.DISPLAY_FORMAT.format(passDataUIModel.insertionTime)
            )

    }
}
