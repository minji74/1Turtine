package com.example.turtine

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.turtine.data.Item
import com.example.turtine.databinding.FragmentTimerBinding
import com.example.turtine.databinding.ItemListItemBinding
import kotlinx.android.synthetic.main.fragment_timer.*
import java.util.*
import kotlin.concurrent.timer


class TimerFragment : Fragment() {
    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!
    private val navigationArgs: TimerFragmentArgs by navArgs()
    private var itemBinding : ItemListItemBinding? = null



    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database.itemDao()
        )
    }


    private var time = 0
    private var timerTask: Timer? = null
    private val uiHandler by lazy { Handler(Looper.getMainLooper()) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.itemId // 탐색 인수를 검색하여 새로운 변수에 할당

        lifecycleScope.launchWhenResumed {
            viewModel.retrieveItem(id).observe(viewLifecycleOwner) { selectedItem ->
                time = (selectedItem.itemMin * 60 + selectedItem.itemSec) * 1000

                with(binding) {
                    //루틴내용
                    titleTextView.text = selectedItem.itemRoutine
                    //시간
                    timerTextView.text = "%02d:%02d".format(
                        Locale.US,
                        selectedItem.itemMin,
                        selectedItem.itemSec
                    )
                    progressIndicator.max = time
                    progressIndicator.progress = 0

                    start(selectedItem)
                }
            }
        }
    }

    override fun onDestroy() {
        timerTask?.cancel()
        super.onDestroy()
    }

    private fun start(item: Item) {
        fun setTime(time: Int) {
            uiHandler.post {
                val minute = (time / 1000) / 60
                val second = (time / 1000) % 60
                val progress = ((item.itemMin * 60 + item.itemSec) * 1000) - time

                if (_binding == null) return@post

                with(binding) {
                    timerTextView.text = "%02d:%02d".format(Locale.US, minute, second)
                    progressIndicator.progress = progress
                }
            }
        }
        lateinit var item: Item
        setTime(time)
        timerTask = timer(period = 10) {
            time -= 10
            if (time <= 0) {
                uiHandler.post {
                    binding.doneButton.isVisible = true
                    done_button.setOnClickListener{
                        requireActivity().onBackPressed()
                        viewModel.retrieveItem(id).observe(viewLifecycleOwner) { selectedItem ->
                            item = selectedItem
                            // 리스트 아이템의 이미지뷰 바꾸기
                            itemBinding?.timerImageView?.setImageResource(R.drawable.ic_baseline_check_circle_outline_24)
                        }
                    }
                    activity

                }

                cancel()
                return@timer
            }

            setTime(time)
        }
    }
}
