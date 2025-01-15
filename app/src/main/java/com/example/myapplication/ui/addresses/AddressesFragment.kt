package com.example.myapplication.ui.addresses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentAddressesBinding
import com.example.myapplication.ui.adapters.AddressesCustomAdapter
import com.example.myapplication.viewModels.AddressViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressesFragment : Fragment() {

    private var _binding: FragmentAddressesBinding? = null
    private val binding get() = _binding!!

    private val addressViewModel: AddressViewModel by viewModels()

    private val addressesAdapter by lazy { AddressesCustomAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddressesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            addressesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            addressesRecyclerView.adapter = addressesAdapter


            addressViewModel.addresses.observe(viewLifecycleOwner) { addressesList ->
                if (addressesList.isEmpty())
                    emptyListTxt.visibility = View.VISIBLE
                else
                    emptyListTxt.visibility = View.GONE


                addressesAdapter.setData(addressesList)
            }

            addressViewModel.loading.observe(viewLifecycleOwner){
                if(it){
                    loadingProgressbar.visibility = View.VISIBLE
                    addressesRecyclerView.visibility = View.GONE
                }
                else{
                    loadingProgressbar.visibility = View.GONE
                    addressesRecyclerView.visibility = View.VISIBLE
                }
            }


            addressViewModel.fetchAddresses()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
