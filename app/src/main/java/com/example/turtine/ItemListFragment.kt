/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.turtine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.turtine.databinding.FragmentItemListBinding

/**
 * Main fragment displaying details for all items in the database.
 */
class ItemListFragment : Fragment() {


    // ++
    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database.itemDao()
        )
    }

    private var _binding: FragmentItemListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        return binding.root
    }


    // 리스트 페이지에서 보이게 하기
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //++ 아무것도 전달하지 않는 기본 생성자 ItemListAdapter{}를 사용하여 새 adapter 속성을 초기화
        val adapter = ItemListAdapter(
            onItemClicked = {
                // 항목 id를 전달하는 ItemListFragmentDirections에서 actionItemListFragmentToItemDetailFragment() 메서드를 호출합니다. 반환된 NavDirections 객체를 action에 할당
                val action =
                    ItemListFragmentDirections.actionItemListFragmentToItemDetailFragment(it.id)
                this.findNavController().navigate(action)
            },
            onTimeClicked = {
                val action =
                    ItemListFragmentDirections.actionItemListFragmentToTimerFragment(
                        it.itemRoutine,
                        it.id
                    )
                this.findNavController().navigate(action)
            })

        binding.recyclerView.adapter = adapter // 새로 만든 adapter를 recyclerView에 바인딩



        // ++ allItems에 관찰자를 연결하여 데이터 변경사항을 수신 대기
        viewModel.allItems.observe(this.viewLifecycleOwner) { items ->
            items.let {
                // 관찰자 내 adapter에서 submitList()를 호출하고 새 목록을 전달
                // 그러면 새 항목이 목록에 포함되어 RecyclerView가 업데이트 됨.
                adapter.submitList(it)
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.floatingActionButton.setOnClickListener {
            val action = ItemListFragmentDirections.actionItemListFragmentToAddItemFragment(
                getString(R.string.add_fragment_title)
            )
            this.findNavController().navigate(action)
        }
    }

}
