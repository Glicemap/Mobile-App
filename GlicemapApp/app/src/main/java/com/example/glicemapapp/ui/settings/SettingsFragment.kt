package com.example.glicemapapp.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.glicemapapp.R
import com.example.glicemapapp.ui.base.ToolbarFragment
import com.example.glicemapapp.databinding.FragmentSettingsBinding
import com.example.glicemapapp.ui.MainActivity

class SettingsFragment : ToolbarFragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val activity = requireActivity() as MainActivity
        binding.personalNameTv.text = activity.user.name
        binding.personalEmailTv.text = activity.user.email
        binding.personalMinTv.text = context?.getString(R.string.settings_sugar_min_level,activity.user.sugarMin)
        binding.personalMaxTv.text = context?.getString(R.string.settings_sugar_max_level,activity.user.sugarMax)
        binding.doctorNameTv.text = "Cadastre seu Médico"
        setListeners()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setListeners(){
        binding.run{
            personal.setOnClickListener {
                findNavController().navigate(SettingsFragmentDirections.toPersonal())
            }

            doctor.setOnClickListener {

            }
        }
    }
}