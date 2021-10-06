package com.example.glicemapapp.ui.home

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.example.glicemapapp.data.Result
import com.example.glicemapapp.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setListeners()
        loadDates(0)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadDates(addMonth: Int){
        binding.progressBar.visibility = View.VISIBLE
        binding.progressBar.bringToFront()
        homeViewModel.loadDates(addMonth).observe(viewLifecycleOwner){
            binding.progressBar.visibility = View.INVISIBLE
            val result = it?.let { result ->
                when (result) {
                    is Result.Success -> {
                        result.data?.let {
                            setCalendar(homeViewModel.setCalendarData(it))
                            true
                        } ?: false
                    }
                    is Result.Error -> {
                        Snackbar.make(
                            binding.root,
                            result.exception.message.toString(),
                            Snackbar.LENGTH_LONG
                        ).show()
                        false
                    }
                }
            }
        }
    }

    private fun setCalendar(events: List<EventDay>) {
        val calendarView: CalendarView = binding.calendarView as CalendarView
        calendarView.setEvents(events)
        calendarView.setOnForwardPageChangeListener {
            loadDates(1)
        }
        calendarView.setOnPreviousPageChangeListener {
            loadDates(-1)
        }
        calendarView.setOnDayClickListener {
            val dialog = BottomSheetFragment()
            val formatDate = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("pt","BR"))
            val formatWeekDay =  SimpleDateFormat("EEEE", Locale("pt","BR"))
            dialog.date = formatDate.format(it.calendar.time)
            dialog.weekDay = formatWeekDay.format(it.calendar.time)
            dialog.show(requireFragmentManager(),"ModalBottomSheet")
        }
    }

    private fun setListeners(){
        binding.newMeasurementBt.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.toNewMeasurement())
        }
    }

}