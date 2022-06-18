package com.bhdr.twitterclone.loginupfragments

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.databinding.FragmentSignUpBinding
import com.bhdr.twitterclone.helperclasses.LoadingDialog
import com.bhdr.twitterclone.repos.LoginRepository
import com.bhdr.twitterclone.viewmodels.SignUpViewModel
import com.bhdr.twitterclone.viewmodels.UserNameEmailViewModel
import com.google.android.material.snackbar.Snackbar
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class SignUpFragment : Fragment(R.layout.fragment_sign_up) {
    private val binding by viewBinding(FragmentSignUpBinding::bind)

    private val viewModelGetUserNE by lazy { UserNameEmailViewModel() }
    private val viewModelSignUp by lazy { SignUpViewModel() }

    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    var selectedPicture: Uri? = null
    var selectedBitmap: Bitmap? = null
    var userSaved: Boolean? = null


    private val loadingDialog by lazy { LoadingDialog() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.profilePicture.setOnClickListener {
            selectImage()
        }

        binding.signUpButton.setOnClickListener {

            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val formatted = current.format(formatter)
            createUser(
                binding.usernameEditText.text.toString(),
                binding.passwordEditText.text.toString(),
                binding.nameEditText.text.toString(),
                binding.emailEditText.text.toString(),
                binding.phoneEditText.text.toString(),
                formatted.toString()
            )
        }


        registerLauncher()

        viewModelGetUserNE
        createUserObservable()
    }

    fun selectImage() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                Snackbar.make(
                    requireView(),
                    "Permission needed for gallery",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("Give Permission",
                    View.OnClickListener {
                        permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    }).show()
            } else {
                permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        } else {
            val intentToGallery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)
        }
    }

    private fun registerLauncher() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val intentFromResult = result.data
                    if (intentFromResult != null) {
                        selectedPicture = intentFromResult.data
                        try {
                            if (Build.VERSION.SDK_INT >= 28) {
                                val source = ImageDecoder.createSource(
                                    requireActivity().contentResolver,
                                    selectedPicture!!
                                )
                                selectedBitmap = ImageDecoder.decodeBitmap(source)
                                binding.profilePicture.setImageBitmap(selectedBitmap)
                            } else {
                                selectedBitmap = MediaStore.Images.Media.getBitmap(
                                    requireActivity().contentResolver,
                                    selectedPicture
                                )
                                binding.profilePicture.setImageBitmap(selectedBitmap)
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                if (result) {
                    //permission granted
                    val intentToGallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGallery)
                } else {
                    //permission denied
                    Snackbar.make(requireView(), "İzin gerekli!", 1000).show()
                }
            }
    }

    private fun createUser(
        userName: String,
        password: String,
        name: String,
        email: String,
        phone: String,
        date: String
    ) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (name.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty() && userName.isNotEmpty()) {
                val uuid = UUID.randomUUID()
                val imageName = "$uuid.jpg"

                if (selectedPicture != null) {
                    viewModelGetUserNE.userData.observe(viewLifecycleOwner) { it ->

                        var control = it.find { it.userName == userName }

                        if (control != null) {
                            Snackbar.make(requireView(), "Bu Kullanıcı Adı mevcut!", 1500).show()
                        } else {

                            control = it.find { it.email == email }
                            if (control != null) {
                                Snackbar.make(requireView(), "Bu Email  mevcut!", 1500).show()
                            } else {
                                viewModelSignUp.SignUpview(
                                    userName,
                                    password,
                                    name,
                                    email,
                                    phone,
                                    date,
                                    imageName,
                                    selectedPicture

                                )

                            }
                        }

                    }

                } else {
                    Snackbar.make(requireView(), "Lütfen profil resmi seçiniz!", 1000).show()
                }


            } else {
                Snackbar.make(requireView(), "Lütfen tüm boşlukları doldurun!", 1000).show()
            }

        } else {
            Snackbar.make(requireView(), "Geçerli Bir Email Adresi giriniz", 1500).show()
        }

    }

    private fun createUserObservable() {
        viewModelSignUp.userSavedStatus.observe(viewLifecycleOwner) {
            when (it!!) {
                LoginRepository.LogInUpStatus.LOADING -> loadingDialog.loadingDialogStart(
                    requireActivity()
                )
                LoginRepository.LogInUpStatus.ERROR -> {
                    loadingDialog.loadingDialogClose();Snackbar.make(
                        requireView(),
                        "Hata Oluştu Tekrar Deneyiniz",
                        3000
                    ).show()
                }
                LoginRepository.LogInUpStatus.DONE -> {
                    loadingDialog.loadingDialogClose();Snackbar.make(
                        requireView(),
                        "Giriş Başarılı",
                        3000
                    ).show()
                }
            }

        }
    }
}