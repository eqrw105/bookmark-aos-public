package com.nims.bookmark.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class RegisterAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    var items: List<Fragment> = listOf()
    override fun getItemCount(): Int = items.size

    override fun createFragment(position: Int): Fragment {
        return items[position]
    }
}
//class RegisterAdapter: ListAdapter<Fragment, RegisterViewHolder>(RegisterDiffCallback()) {
//    var items: List<Fragment> = listOf()
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegisterViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        val view = inflater.inflate(R.layout.item_item_register, parent, false)
//        return RegisterViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: RegisterViewHolder, position: Int) {
//        val item = items[position]
//    }
//
//    override fun getItemCount(): Int = items.size
//}
//
//class RegisterViewHolder(view: View): BindingViewHolder<ItemItemRegisterBinding>(view)
//class RegisterDiffCallback: DiffUtil.ItemCallback<Fragment>() {
//    override fun areItemsTheSame(oldItem: Fragment, newItem: Fragment): Boolean {
//        return oldItem.id == newItem.id
//    }
//
//    override fun areContentsTheSame(oldItem: Fragment, newItem: Fragment): Boolean {
//        return oldItem == newItem
//    }
//
//}