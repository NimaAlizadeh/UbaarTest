package com.example.myapplication.ui.confirmAddress

import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentConfirmAddressBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import java.util.*

class ConfirmAddressFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentConfirmAddressBinding? = null
    private val binding get() = _binding!!

    private lateinit var googleMap: GoogleMap
    private var selectedLatLng: LatLng? = null
    private var selectedAddress: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment =
            childFragmentManager.findFragmentById(com.example.myapplication.R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnConfirmLocation.setOnClickListener {
            if (selectedLatLng != null) {

                val bundle = Bundle().apply {
                    putString("address", selectedAddress)
                    putFloat("lat", selectedLatLng!!.latitude.toFloat())
                    putFloat("lng", selectedLatLng!!.longitude.toFloat())
                }

                parentFragmentManager.setFragmentResult("addressRequestKey", bundle)

                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), "موقعیتی انتخاب نشده است.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        val defaultLocation = LatLng(35.6892, 51.3890)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15f))

        googleMap.setOnCameraIdleListener {
            val centerLatLng = googleMap.cameraPosition.target
            selectedLatLng = centerLatLng

            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val addresses = geocoder.getFromLocation(centerLatLng.latitude, centerLatLng.longitude, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                selectedAddress = addresses[0].getAddressLine(0) ?: "آدرس نامشخص"
            } else {
                selectedAddress = "آدرس نامشخص"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
