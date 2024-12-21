package com.aston.myapplication.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.aston.myapplication.databinding.ActivityMainBinding
import com.aston.myapplication.domain.entity.Contact
import com.aston.myapplication.presentation.ui.adapters.CompositeDelegateAdapter
import com.aston.myapplication.presentation.ui.adapters.ContactDelegateAdapter
import com.aston.myapplication.presentation.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private var isClick = false

    private val viewBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    private val selectedMap = mutableMapOf<Int, Contact>()
    private var modeDelete = false

    private val contactAdapter by lazy {
        CompositeDelegateAdapter(
            listOf(
                ContactDelegateAdapter(
                    clickOnItemListener = { contact ->
                        startActivity(NewContactActivity.newIntentEdit(this, contact))
                    },
                    selectedMap = selectedMap,
                    modeDelete = modeDelete
                )
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        viewBinding.rvContacts.adapter = contactAdapter

        observers()
        clickListeners()
        setupItemTouchHelper()
    }

    private fun observers() {
        viewModel.listContact.observe(this) {
            contactAdapter.submitList(it)
        }

        viewModel.shouldCloseMode.observe(this) { cancel() }
    }

    private fun clickListeners() {
        viewBinding.fabAdd.setOnClickListener {
            startActivity(NewContactActivity.newIntentAdd(this))
        }

        viewBinding.btnDelete.setOnClickListener {
            viewModel.removeListContact(selectedMap.values.toList())
        }

        viewBinding.fabDelete.setOnClickListener { cancel() }

        viewBinding.btnCancel.setOnClickListener { cancel() }
    }

    private fun cancel() {
        isClick = !isClick
        animationVisibility(viewBinding.clDeleteMode, getVisibilityStatus(isClick))
        animationVisibility(viewBinding.fabAdd, getVisibilityStatus(!isClick))
        modeDelete = isClick
        selectedMap.clear()
        contactAdapter.notifyDataSetChanged()
    }

    private fun animationVisibility(view: View, statusVisibility: Int) {
        view.apply {
            alpha = getAlpha(statusVisibility)
            visibility = statusVisibility

            animate()
                .alpha(if (alpha > 0) 0f else 1f)
                .setDuration(DURATION)
        }
    }

    private fun getAlpha(statusVisibility: Int) = if (statusVisibility == View.VISIBLE) 0f else 1f

    private fun getVisibilityStatus(isClick: Boolean) =
        if (isClick) View.VISIBLE else View.INVISIBLE

    private fun setupItemTouchHelper() {
        val callback = object :
            ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition

                if (fromPosition == RecyclerView.NO_POSITION || toPosition == RecyclerView.NO_POSITION) {
                    return false
                }

                val updatedList = contactAdapter.items.toMutableList()
                val movedItem = updatedList.removeAt(fromPosition)
                updatedList.add(toPosition, movedItem)

                contactAdapter.submitList(updatedList)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) = Unit
        }
        ItemTouchHelper(callback).attachToRecyclerView(viewBinding.rvContacts)
    }

    companion object {
        private const val DURATION = 500L
    }
}
