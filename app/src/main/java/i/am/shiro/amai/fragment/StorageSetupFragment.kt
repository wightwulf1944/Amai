package i.am.shiro.amai.fragment

import android.content.Context
import android.os.Bundle
import android.text.format.Formatter
import android.view.View
import androidx.fragment.app.Fragment
import i.am.shiro.amai.R
import i.am.shiro.amai.dagger.component
import i.am.shiro.amai.databinding.ItemStorageOptionBinding
import i.am.shiro.amai.model.StorageOption
import i.am.shiro.amai.util.addChild
import i.am.shiro.amai.util.goToMain
import i.am.shiro.amai.util.loadInt
import i.am.shiro.amai.util.saveInt
import kotlinx.android.synthetic.main.fragment_storage_setup.*

class StorageSetupFragment : Fragment(R.layout.fragment_storage_setup) {

    private val preferences by lazy { component.preferences }

    private lateinit var storageOptions: List<StorageOption>

    private var selectedIndex: Int = -1

    override fun onAttach(context: Context) {
        super.onAttach(context)

        storageOptions = context.getExternalFilesDirs(null).map { StorageOption(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.loadInt(::selectedIndex)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.saveInt(::selectedIndex)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        finishButton.isEnabled = selectedIndex != -1
        finishButton.setOnClickListener { onFinish() }

        storageOptions.forEachIndexed { i, storageOption ->

            optionsLayout.addChild(ItemStorageOptionBinding::inflate) {
                root.isSelected = selectedIndex == i
                root.setOnClickListener {
                    optionsLayout.dispatchSetSelected(false)
                    root.isSelected = true
                    selectedIndex = i
                    finishButton.isEnabled = true
                }

                titleText.text = getString(R.string.storage_label, i)

                pathText.text = storageOption.path

                val spaceFree = storageOption.spaceFree
                val freeSpaceSize = Formatter.formatFileSize(context, spaceFree)
                freeSpaceText.text = getString(R.string.storage_free, freeSpaceSize)

                gauge.progress = storageOption.percentUsed
            }
        }
    }

    private fun onFinish() {
        goToMain()

        preferences.storagePath = storageOptions[selectedIndex].path
        preferences.isFirstRun = false
    }
}
