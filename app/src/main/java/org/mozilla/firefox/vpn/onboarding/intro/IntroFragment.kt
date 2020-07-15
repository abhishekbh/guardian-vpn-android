package org.mozilla.firefox.vpn.onboarding.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.mozilla.firefox.vpn.R
import org.mozilla.firefox.vpn.databinding.FragmentIntroBinding
import org.mozilla.firefox.vpn.util.color
import org.mozilla.firefox.vpn.util.viewBinding

class IntroFragment : BottomSheetDialogFragment() {

    private var binding: FragmentIntroBinding by viewBinding()

    private lateinit var adapter: IntroAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentIntroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupIntroPager()

        binding.closeBtn.setOnClickListener {
            dismiss()
        }
        binding.skipBtn.setOnClickListener {
            binding.introPager.smoothScrollToPosition(adapter.itemCount - 1)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val bottomSheet = it.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet) ?: return@let
            val params = (bottomSheet.layoutParams as? CoordinatorLayout.LayoutParams) ?: return@let
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
            params.behavior = BottomSheetBehavior<FrameLayout>()

            (params.behavior as? BottomSheetBehavior)?.apply {
                addBottomSheetCallback(bottomSheetBehaviorCallback)
                peekHeight = 0
                state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (isAdded) {
            return
        } else {
            super.show(manager, tag)
        }
    }

    private fun setupIntroPager() {
        adapter = IntroAdapter()
        binding.introPager.adapter = adapter
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.introPager)
        val decoration =
            DotsIndicatorDecoration(context!!,
                colorActive = context!!.color(R.color.blue50),
                colorInactive = context!!.color(R.color.gray20)
            )
        binding.introPager.addItemDecoration(decoration)
        binding.introPager.isNestedScrollingEnabled = false
    }

    private val bottomSheetBehaviorCallback = object : BottomSheetBehavior.BottomSheetCallback() {

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                dismissAllowingStateLoss()
            }
        }
    }

    companion object {
        fun newInstance(): IntroFragment = IntroFragment()
    }
}
