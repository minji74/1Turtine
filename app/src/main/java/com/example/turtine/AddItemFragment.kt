package com.example.turtine

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.turtine.data.Item
import com.example.turtine.databinding.FragmentAddItemBinding

/**
 * Fragment to add or update an item in the Inventory database.
 */
class AddItemFragment : Fragment() {

    // ++
    // by activityViewModels() : Kotlin 속성 위임을 사용하여 프래그먼트 전체에서 ViewModel을 공유
    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database
                .itemDao() // ItemDao 인스턴스를 전달
        )
    }

    // ++
    lateinit var item: Item

    private val navigationArgs: ItemDetailFragmentArgs by navArgs()

    // Binding object instance corresponding to the fragment_add_item.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment
    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    // ++ 텍스트 필드가 비었는지 체크
    private fun isEntryValid(): Boolean {
        // viewModel 인스턴스에서 isEntryValid() 함수를 호출하여 텍스트 뷰의 텍스트를 전달
        // viewModel.isEntryValid() 함수의 값을 반환
        return viewModel.isEntryValid(
            binding.itemRoutine.text.toString(),
            binding.itemMin.text.toString(),
            binding.itemSec.text.toString()
        )
    }

    // ++ 아이템을 추가함
    private fun addNewItem() {
        if (isEntryValid()) {
            // 사용자가 입력한 항목 세부정보를 전달하고 binding 인스턴스를 사용하여 읽음.
            viewModel.addNewItem(
                binding.itemRoutine.text.toString(),
                binding.itemMin.text.toString(),
                binding.itemSec.text.toString()
            )
        }
        // ItemListFragment로 다시 이동
        val action = AddItemFragmentDirections.actionAddItemFragmentToItemListFragment()
        findNavController().navigate(action)
    }

    // ++ 리스트 페이지에서 보이게 하기
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 클릭 핸들러를 저장 버튼에 추가하고 addNewItem()을 호출
        binding.saveAction.setOnClickListener {
            addNewItem()
        }
    }

    /**
     * Called before fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }
}
