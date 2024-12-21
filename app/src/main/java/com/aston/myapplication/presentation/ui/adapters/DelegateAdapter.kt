package com.aston.myapplication.presentation.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface DelegateAdapter<T, VH : RecyclerView.ViewHolder> {
    fun isForViewType(item: T, position: Int): Boolean
    fun onCreateViewHolder(parent: ViewGroup): VH
    fun onBindViewHolder(holder: VH, item: T, position: Int)
}