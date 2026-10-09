package com.cleteci.redsolidaria.ui.fragments.infoService

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.databinding.FragmentInfoServiceBinding
import com.cleteci.redsolidaria.models.Resource
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.cleteci.redsolidaria.util.showInfoDialog
import com.cleteci.redsolidaria.viewModels.BaseViewModel
import com.cleteci.redsolidaria.viewModels.OrganizationViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class InfoServiceFragment : BaseFragment() {

    private var _binding: FragmentInfoServiceBinding? = null
    private val binding get() = _binding!!
    private val organizationVM by viewModel<OrganizationViewModel>()
    private lateinit var resourceId: String
    private lateinit var resourceName: String
    private lateinit var resourceType: Resource.Type
    private var resourceIconId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.also { bundle ->
            resourceId = bundle.getString(RESOURCE_ID, "")
            resourceName = bundle.getString(NAME, "")
            resourceIconId = bundle.getInt(ICON_ID, 0)
            val typeStr = bundle.getString(TYPE, "")
            resourceType = if (typeStr.isNotEmpty()) Resource.Type.valueOf(typeStr) else Resource.Type.CATEGORY
        }

        organizationVM.totalCategoryAttentions.observe(this) { totalCategoryAttentions: Int ->
            loadDataSuccess(totalCategoryAttentions)
        }
        organizationVM.totalServiceAttentions.observe(this) { totalServiceAttentions: Int ->
            loadDataSuccess(totalServiceAttentions)
        }
        organizationVM.status.observe(this) { status: BaseViewModel.QueryStatus? ->
            when (status) {
                BaseViewModel.QueryStatus.NOTIFY_LOADING -> showLoading(true)
                BaseViewModel.QueryStatus.NOTIFY_SUCCESS -> {
                    showLoading(false)
                }
                BaseViewModel.QueryStatus.NOTIFY_FAILURE -> {
                    showError(getString(R.string.error_getting_information))
                    showLoading(false)
                }
                BaseViewModel.QueryStatus.NOTIFY_UNKNOWN_HOST_FAILURE -> {
                    showInfoDialog(
                        activity,
                        getString(R.string.error_unknown_host_title),
                        getString(R.string.error_unknown_host)
                    )
                    showLoading(false)
                }
                else -> {
                    showError(getString(R.string.error_getting_information))
                    showLoading(false)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.setTextToolbar(
            getText(R.string.info_services).toString(),
            ContextCompat.getColor(requireContext(), R.color.colorWhite)
        )
        loadAttentions()
    }

    private fun initView() {
        binding.imgIconResource.setImageResource(resourceIconId)
        binding.tvName.text = resourceName
        loadAttentions()

        binding.btAttend.setOnClickListener {
            (activity as? MainActivity)?.openAttendFragment(resourceId, resourceType)
        }

        binding.btInterest.setOnClickListener {
            context?.let { ctx ->
                Toast.makeText(ctx, getString(R.string.in_build), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadAttentions() {
        if (resourceType == Resource.Type.CATEGORY) {
            organizationVM.getTotalCategoryAttentions(resourceId)
        } else if (resourceType == Resource.Type.SERVICE) {
            organizationVM.getTotalServiceAttentions(resourceId)
        }
    }

    fun loadDataSuccess(total: Int) {
        activity?.runOnUiThread {
            if (_binding != null) {
                binding.totalServed.text = total.toString()
            }
        }
    }

    companion object {
        const val TAG: String = "InfoServiceFragment"
        private const val RESOURCE_ID: String = "resource_id"
        private const val NAME: String = "name"
        private const val ICON_ID: String = "icon_id"
        private const val TYPE: String = "type"

        fun newInstance(resource: Resource): InfoServiceFragment =
            InfoServiceFragment().apply {
                this.arguments = Bundle().apply {
                    putString(RESOURCE_ID, resource.id)
                    putString(NAME, resource.name)
                    putInt(ICON_ID, resource.iconId)
                    putString(TYPE, resource.type.name)
                }
            }
    }
}
