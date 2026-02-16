/*
 * Copyright (C) 2016-2025 Álinson Santos Xavier <git@axavier.org>
 *
 * This file is part of Loop Habit Tracker.
 *
 * Loop Habit Tracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Loop Habit Tracker is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.isoron.uhabits.activities.habits.list

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import nl.dionsegijn.konfetti.xml.KonfettiView
import org.isoron.uhabits.R
import org.isoron.uhabits.activities.common.views.ScrollableChart
import org.isoron.uhabits.activities.common.views.TaskProgressBar
import org.isoron.uhabits.activities.habits.list.views.EmptyListView
import org.isoron.uhabits.activities.habits.list.views.HabitCardListAdapter
import org.isoron.uhabits.activities.habits.list.views.HabitCardListView
import org.isoron.uhabits.activities.habits.list.views.HabitCardListViewFactory
import org.isoron.uhabits.activities.common.views.FloatingToolbarNavigation
import org.isoron.uhabits.activities.habits.list.views.HeaderView
import org.isoron.uhabits.activities.habits.list.views.HintView
import org.isoron.uhabits.core.models.ModelObservable
import org.isoron.uhabits.core.models.PaletteColor
import org.isoron.uhabits.core.preferences.Preferences
import org.isoron.uhabits.core.tasks.TaskRunner
import org.isoron.uhabits.core.ui.screens.habits.list.HintListFactory
import org.isoron.uhabits.core.utils.MidnightTimer
import org.isoron.uhabits.inject.ActivityContext
import org.isoron.uhabits.inject.ActivityScope
import org.isoron.uhabits.utils.buildToolbar
import org.isoron.uhabits.utils.currentTheme
import org.isoron.uhabits.utils.dim
import org.isoron.uhabits.utils.dp
import org.isoron.uhabits.utils.setupToolbar
import org.isoron.uhabits.utils.sres
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

const val MAX_CHECKMARK_COUNT = 60

@ActivityScope
class ListHabitsRootView @Inject constructor(
    @ActivityContext context: Context,
    hintListFactory: HintListFactory,
    preferences: Preferences,
    midnightTimer: MidnightTimer,
    runner: TaskRunner,
    private val listAdapter: HabitCardListAdapter,
    habitCardListViewFactory: HabitCardListViewFactory
) : FrameLayout(context), ModelObservable.Listener {

    interface Listener {
        fun onSettingsClicked()
        fun onAddClicked()
    }

    private var listener: Listener? = null

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    val listView: HabitCardListView = habitCardListViewFactory.create().apply {
        clipToPadding = false
        setPadding(0, 0, 0, dp(100f).toInt())
    }
    val llEmpty = EmptyListView(context)
    val tbar = buildToolbar()
    val konfettiView = KonfettiView(context).apply {
        translationZ = 10f
    }
    val progressBar = TaskProgressBar(context, runner)
    val hintView: HintView
    val header = HeaderView(context, preferences, midnightTimer)

    init {
        val hints = resources.getStringArray(R.array.hints)
        val hintList = hintListFactory.create(hints)
        hintView = HintView(context, hintList)

        val coordinatorLayout = CoordinatorLayout(context)

        val appBarLayout = AppBarLayout(context).apply {
            background = null // Let background be handled by children or window
            stateListAnimator = null // Remove shadow
            fitsSystemWindows = true // Ensure content starts below status bar
        }

        val collapsingToolbarLayout = CollapsingToolbarLayout(context).apply {
            title = resources.getString(R.string.main_activity_title)
            setExpandedTitleTextAppearance(R.style.TextAppearance_App_Title_Expanded)
            setCollapsedTitleTextAppearance(R.style.TextAppearance_App_Title_Collapsed)

            // Set scroll flags: Scroll | ExitUntilCollapsed
            val params = AppBarLayout.LayoutParams(
                MATCH_PARENT,
                dp(120f).toInt() // Expanded height approximation
            ).apply {
                scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
                        AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
            }
            layoutParams = params

            setContentScrimColor(sres.getColor(com.google.android.material.R.attr.colorSurfaceContainer))
            setStatusBarScrimColor(sres.getColor(com.google.android.material.R.attr.colorSurfaceContainer))
        }

        // Toolbar
        val toolbarParams = CollapsingToolbarLayout.LayoutParams(
            MATCH_PARENT,
            dim(R.dimen.abc_action_bar_default_height_material).toInt()
        )
        toolbarParams.setCollapseMode(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN)
        tbar.layoutParams = toolbarParams
        collapsingToolbarLayout.addView(tbar)

        appBarLayout.addView(collapsingToolbarLayout)

        // HeaderView (Calendar Strip) - Pinned below Collapsing Toolbar
        header.layoutParams = AppBarLayout.LayoutParams(
            MATCH_PARENT,
            WRAP_CONTENT
        )
        appBarLayout.addView(header)

        coordinatorLayout.addView(appBarLayout, MATCH_PARENT, WRAP_CONTENT)

        // ListView
        val listParams = CoordinatorLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT).apply {
            behavior = AppBarLayout.ScrollingViewBehavior()
        }
        coordinatorLayout.addView(listView, listParams)

        // Empty View
        val emptyParams = CoordinatorLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT).apply {
            behavior = AppBarLayout.ScrollingViewBehavior()
            topMargin = dp(60f).toInt() // Push down slightly
        }
        coordinatorLayout.addView(llEmpty, emptyParams)

        // Progress Bar - Anchor to AppBarLayout bottom? Or simple top placement.
        // Original was below header. Here we can put it below AppBar.
        val progressParams = CoordinatorLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
            behavior = AppBarLayout.ScrollingViewBehavior()
            // Negative margin to overlap slightly or zero
            topMargin = dp(-6.0f).toInt()
        }
        coordinatorLayout.addView(progressBar, progressParams)

        // Hint View - Bottom
        val hintParams = CoordinatorLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
            gravity = android.view.Gravity.BOTTOM
        }
        coordinatorLayout.addView(hintView, hintParams)

        // Konfetti - Top / Overlay
        coordinatorLayout.addView(konfettiView, MATCH_PARENT, MATCH_PARENT)

        val rootView = coordinatorLayout.apply {
            background = sres.getDrawable(com.google.android.material.R.attr.colorSurfaceContainer)
        }

        rootView.setupToolbar(
            toolbar = tbar,
            title = resources.getString(R.string.main_activity_title),
            color = PaletteColor(17),
            displayHomeAsUpEnabled = false,
            theme = currentTheme()
        )
        addView(rootView, MATCH_PARENT, MATCH_PARENT)
        listAdapter.setListView(listView)

        val composeView = ComposeView(context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    FloatingToolbarNavigation(
                        activeScreen = ListHabitsActivity::class.java,
                        onSettingsClick = { listener?.onSettingsClicked() },
                        onAddClick = { listener?.onAddClicked() }
                    )
                }
            }
        }
        val composeParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
            gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
            bottomMargin = dp(16f).toInt()
        }
        addView(composeView, composeParams)
    }

    override fun onModelChange() {
        updateEmptyView()
    }

    private fun setupControllers() {
        header.setScrollController(
            object : ScrollableChart.ScrollController {
                override fun onDataOffsetChanged(newDataOffset: Int) {
                    listView.dataOffset = newDataOffset
                }
            }
        )
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setupControllers()
        listAdapter.observable.addListener(this)
    }

    override fun onDetachedFromWindow() {
        listAdapter.observable.removeListener(this)
        super.onDetachedFromWindow()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val count = getCheckmarkCount()
        header.buttonCount = count
        header.setMaxDataOffset(max(MAX_CHECKMARK_COUNT - count, 0))
        listView.checkmarkCount = count
        super.onSizeChanged(w, h, oldw, oldh)
    }

    private fun getCheckmarkCount(): Int {
        val nameWidth = dim(R.dimen.habitNameWidth)
        val buttonWidth = dim(R.dimen.checkmarkWidth)
        val labelWidth = max((measuredWidth / 3).toFloat(), nameWidth)
        val buttonCount = ((measuredWidth - labelWidth) / buttonWidth).toInt()
        return min(MAX_CHECKMARK_COUNT, max(0, buttonCount))
    }

    private fun updateEmptyView() {
        if (listAdapter.itemCount == 0) {
            if (listAdapter.hasNoHabit()) {
                llEmpty.showEmpty()
            } else {
                llEmpty.showDone()
            }
        } else {
            llEmpty.hide()
        }
    }
}
