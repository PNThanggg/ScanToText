package com.hoicham.orc.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.google.android.material.transition.MaterialSharedAxis
import com.google.mlkit.vision.common.InputImage
import com.hoicham.orc.R
import com.hoicham.orc.core.base.BaseFragment
import com.hoicham.orc.core.utils.collectFlow
import com.hoicham.orc.core.utils.isTiramisuPlus
import com.hoicham.orc.core.utils.showCameraPermissionInfoDialog
import com.hoicham.orc.core.utils.showSnackbarLongWithAction
import com.hoicham.orc.core.utils.showSnackbarShort
import com.hoicham.orc.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override fun inflateLayout(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var adapter: ScanListAdapter

    private val selectImageRequest = registerForActivityResult(CropImageContract()) {
        if (it.isSuccessful) {
            it.uriContent?.let { uri ->
                handleImage(uri)
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                //permission granted. Continue workflow
                //selectImageRequest.launch(cropImageCameraOptions)
                selectImageRequest.launch(cropImageCameraOptions)
            } else {
                //Provide explanation on why the permission is needed. AlertDialog maybe?
                viewModel.handlePermissionDenied()
            }
        }

    private val cropImageGalleryOptions = CropImageContractOptions(
        null,
        CropImageOptions().apply {
            imageSourceIncludeCamera = false
            imageSourceIncludeGallery = true
        },
    )

    private val cropImageCameraOptions = CropImageContractOptions(
        null,
        CropImageOptions().apply {
            imageSourceIncludeCamera = true
            imageSourceIncludeGallery = false
        },
    )


    override fun initView(view: View, savedInstanceState: Bundle?) {
        viewModel.onHomeFrag()
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        setupRecyclerView()

        collectFlow(viewModel.state) { state ->
            binding.apply {
                linearLayoutEmpty.isVisible = state.isEmpty
            }
        }

        binding.apply {
            setupScrollListener()

            observeState()
        }

        collectFlow(viewModel.events) { homeEvents ->
            when (homeEvents) {
                is HomeEvents.ShowCurrentScanSaved -> {
//                    val action = HomeFragmentDirections.toDetailScanFragment(homeEvents.id, 1)
//                    findNavController().safeNav(action)
                }

                is HomeEvents.ShowLoadingDialog -> {
                    binding.cardViewLoading.animate().translationX(0f)
                    binding.buttonCameraScan.isEnabled = false
                    binding.buttonGalleryScan.isEnabled = false
                }

                is HomeEvents.ShowScanEmpty -> {
                    showSnackbarShort(
                        message = getString(R.string.no_text_found),
                        anchor = binding.buttonCameraScan
                    )
                    binding.cardViewLoading.animate().translationX(-1000f)
                    binding.buttonCameraScan.isEnabled = true
                    binding.buttonGalleryScan.isEnabled = true
                }

                is HomeEvents.ShowUndoDeleteScan -> {
                    showSnackbarLongWithAction(
                        message = getString(R.string.scan_deleted),
                        anchor = binding.buttonCameraScan,
                        actionText = getString(R.string.undo)
                    ) {
                        viewModel.insertScan(homeEvents.scan)
                    }
                }

                is HomeEvents.ShowOnboarding -> {
                    findNavController().navigate(R.id.action_homeFragment_to_viewPagerFragment)
                }

                is HomeEvents.ShowErrorWhenScanning -> {
                    showSnackbarShort(
                        message = getString(R.string.something_went_wrong),
                        anchor = binding.buttonCameraScan
                    )
                }

                is HomeEvents.ShowPermissionInfo -> {
                    showCameraPermissionInfoDialog()
                }

                is HomeEvents.ShowSupportDialog -> {
                    findNavController().navigate(R.id.action_homeFragment_to_supportFragment)
                }
            }
        }
    }

    override fun initData() {
    }

    override fun onBack() {
    }

    override fun initListener() {
        binding.apply {
            buttonCameraScan.setOnClickListener {
                exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
                reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)

                when {
                    ContextCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        selectImageRequest.launch(cropImageCameraOptions)
                    }

                    else -> {
                        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
            }

            buttonGalleryScan.setOnClickListener {
                exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
                reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
                selectImageRequest.launch(cropImageGalleryOptions)
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = ScanListAdapter(
            onScanClicked = { scan ->
                exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
                reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
//                val action = HomeScanFragmentDirections.toDetailScanFragment(
//                    scan.scanId.toInt(), 0
//                )
//                findNavController().safeNav(action).also {
//                    viewModel.moveAwayFromScreen()
//                }
            },
            onInfoClicked = {
                exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
                reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
//                findNavController().navigate(R.id.to_about_fragment).also {
//                    viewModel.moveAwayFromScreen()
//                }
            },
        )
        binding.recyclerViewScans.adapter = adapter
    }

    private fun setupScrollListener() {
        binding.recyclerViewScans.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    binding.buttonCameraScan.hide()
                    binding.buttonGalleryScan.hide()
                }
                if (dy < 0) {
                    binding.buttonCameraScan.show()
                    binding.buttonGalleryScan.show()
                }
            }
        })
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                val items = buildList {
                    // Top bar
                    add(
                        ScanListItem.TopBar {},
                    )

                    // Header with scan count
                    add(ScanListItem.Header(getString(R.string.num_of_scans, state.itemCount)))

                    // Loading state
                    if (state.isLoading) {
                        add(ScanListItem.Loading)
                    }

                    // Pinned scans section
                    if (state.pinnedScans.isNotEmpty()) {
                        add(ScanListItem.ListHeader(getString(R.string.header_pinned)))
                        state.pinnedScans.forEach { scan ->
                            add(ScanListItem.ScanItem(scan))
                        }
                    }

                    // Other scans section
                    if (state.scans.isNotEmpty()) {
                        add(ScanListItem.ListHeader(getString(R.string.headers_other)))
                        state.scans.forEach { scan ->
                            add(ScanListItem.ScanItem(scan))
                        }
                    }
                }
                adapter.submitList(items)
            }
        }
    }

    private fun handleIntent(intent: Intent) {
        if (isTiramisuPlus()) {
            intent.getParcelableExtra(
                Intent.EXTRA_STREAM, Uri::class.java
            )?.let {
                handleImage(it)
            }
        } else {
            (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let {
                handleImage(it)
            }
        }

        intent.action = ""
    }

    private fun handleImage(uri: Uri) {
        val image = InputImage.fromFilePath(requireContext(), uri)
        viewModel.handleScan(image)
    }
}