package com.example.myapplication.ui.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.data.models.AddressRequest
import com.example.myapplication.databinding.FragmentRegisterBinding
import com.example.myapplication.viewModels.RegisterViewModel
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

    var address = ""
    var lat = 0f
    var lng = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTextInputListeners()

        binding.genderBtn.check(R.id.button_female)

        binding.genderBtn.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                val newGender = if (checkedId == R.id.button_male) "male" else "female"
                Log.d("RegisterFragment", "Gender changed to: $newGender")
            }
        }

        binding.apply {
            parentFragmentManager.setFragmentResultListener(
                "addressRequestKey",
                viewLifecycleOwner
            ) { _, bundle ->
                address = bundle.getString("address", "")
                lat = bundle.getFloat("lat", 0.0f)
                lng = bundle.getFloat("lng", 0.0f)

                if (address.isNotEmpty()) {
                    addressEdittext.setText(address)
                }
            }



            viewModel.loading.observe(viewLifecycleOwner) {
                if (it) {
                    loadingProgressbar.visibility = View.VISIBLE
                    nextStageBtn.visibility = View.GONE
                } else {
                    loadingProgressbar.visibility = View.GONE
                    nextStageBtn.visibility = View.VISIBLE
                }
            }

            nextStageBtn.setOnClickListener {
                val firstName = firstNameEdittext.text.toString()
                val lastName = lastNameEdittext.text.toString()
                val mobile = coordinateMobileEdittext.text.toString()
                val phone = coordinatePhoneNumberEdittext.text.toString()
                val gender = if (genderBtn.checkedButtonId == R.id.button_male) "male" else "female"
                val addressText = addressEdittext.text.toString()

                val request = AddressRequest(
                    region = 1,
                    address = addressText,
                    lat = lat,
                    lng = lng,
                    coordinate_mobile = mobile,
                    coordinate_phone_number = phone,
                    first_name = firstName,
                    last_name = lastName,
                    gender = gender
                )

                Log.d("RegisterFragment", "Request: $request")


                if (firstName.isEmpty() || lastName.isEmpty() || mobile.isEmpty() || phone.isEmpty() || addressText.isEmpty()) {
                    showError("لطفاً تمام فیلدها را کامل کنید.")
                    return@setOnClickListener
                }


                if (mobile.length != 11 || !mobile.matches(Regex("^09\\d{9}$"))) {
                    showError("لطفاً شماره موبایل معتبر وارد کنید.")
                    return@setOnClickListener
                }

                viewModel.createAddress(request)
            }

            addressEdittext.setOnClickListener {
                findNavController().navigate(
                    RegisterFragmentDirections.actionRegisterFragmentToConfirmAddressFragment()
                )
            }

            viewModel.creationStatus.observe(viewLifecycleOwner) { success ->
                if (success) {
                    Toast.makeText(
                        requireContext(),
                        "ثبت نام شما با موفقیت انجام شد.",
                        Toast.LENGTH_SHORT
                    ).show()

                    findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToAddressesFragment())
                } else {
                    showError("ثبت نام با مشکل مواجه شد. لطفاً دوباره تلاش کنید.")
                }
            }

            viewModel.errorStatus.observe(viewLifecycleOwner) { errorMessage ->
                errorMessage?.let {
                    showError(it)
                }
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun setupTextInputListeners() {
        binding.apply {
            firstNameTextInputLayout.editText?.addTextChangedListener {
                updateTextInputState(it.toString(), firstNameTextInputLayout)
            }
            lastNameTextInputLayout.editText?.addTextChangedListener {
                updateTextInputState(it.toString(), lastNameTextInputLayout)
            }
            coordinateMobileEdittext.addTextChangedListener {
                val text = it.toString()
                val isValid = text.length == 11 && text.matches(Regex("^09\\d{9}$"))
                updateTextInputState(isValid, coordinateMobileTextInputLayout)
            }
            coordinatePhoneNumberEdittext.addTextChangedListener {
                updateTextInputState(it.toString().length == 11, coordinatePhoneNumberTextInputLayout)
            }
            addressTextInputLayout.editText?.addTextChangedListener {
                updateTextInputState(it.toString(), addressTextInputLayout)
            }
        }
    }

    private fun updateTextInputState(text: String, layout: TextInputLayout) {
        val isValid = text.isNotEmpty()
        updateTextInputState(isValid, layout)
    }

    private fun updateTextInputState(isValid: Boolean, layout: TextInputLayout) {
        val iconRes = if (isValid) R.drawable.baseline_check_circle_24 else R.drawable.baseline_circle_24
        val tintRes = if (isValid) R.color.green else R.color.gray
        layout.setEndIconDrawable(iconRes)
        layout.setEndIconTintList(ContextCompat.getColorStateList(requireContext(), tintRes))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
