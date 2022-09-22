package com.bhdr.twitterclone.ui.signup

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Patterns
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.common.*
import com.bhdr.twitterclone.databinding.FragmentSignUpBinding
import com.google.android.material.snackbar.Snackbar
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.*

@AndroidEntryPoint
class SignUpFragment() : Fragment(R.layout.fragment_sign_up), Parcelable {

   private val binding by viewBinding(FragmentSignUpBinding::bind)

   private val viewModel: SignUpViewModel by viewModels()
   private lateinit var permissionLauncher: ActivityResultLauncher<String>
   private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
   var selectedPicture: Uri? = null
   var selectedBitmap: Bitmap? = null

   constructor(parcel: Parcel) : this() {
      selectedPicture = parcel.readParcelable(Uri::class.java.classLoader)
      selectedBitmap = parcel.readParcelable(Bitmap::class.java.classLoader)
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)




      binding()
      registerLauncher()
      observable()
   }

   private fun binding() {
      with(binding) {

         back.setOnClickListener {
            findNavController().popBackStack()
         }

         profilePicture.setOnClickListener {
            selectImage()
         }

         signUpButton.setOnClickListener {
            createUser(
               usernameEditText.text.toString(),
               passwordEditText.text.toString(),
               nameEditText.text.toString(),
               emailEditText.text.toString(),
               phoneEditText.text.toString(),
               toLongDate().toString()
            )
         }

      }

   }

   private fun selectImage() {
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
               viewModel.createUser(
                  userName,
                  password,
                  name,
                  email,
                  phone,
                  imageName,
                  selectedPicture

               )


            } else {
               snackBar(requireView(), "Lütfen profil resmi seçiniz!", 1000)

            }


         } else {
            snackBar(requireView(), "Lütfen tüm boşlukları doldurun!", 1000)

         }

      } else {
         snackBar(requireView(), "Geçerli Bir Email Adresi giriniz", 1000)
      }

   }

   private fun observable() {
      with(viewModel) {
         with(binding) {

            userSavedStatus.observe(viewLifecycleOwner) {
               when (it!!) {
                  Status.LOADING -> lottiAnim.visible()
                  Status.ERROR -> {
                     lottiAnim.gone()
                     snackBar(requireView(), "Hata oluştu lütfen daha sonra tekrar deneyiniz", 2000)
                  }
                  Status.DONE -> {
                     lottiAnim.gone()
                  }
               }

            }

            userSaved.observe(viewLifecycleOwner) {
               when (it) {
                  true -> {
                     snackBar(requireView(), "Hesap Oluşturuldu", 2000)
                     lottiAnim.gone()
                     findNavController().navigate(R.id.action_signUpFragment_to_logInFragment)

                  }

                  false -> {
                     lottiAnim.gone()
                     snackBar(
                        requireView(),
                        "Girilen bilgiler mevcut",
                        2000
                     )
                  }
               }
            }
         }
      }
   }

   override fun writeToParcel(parcel: Parcel, flags: Int) {
      parcel.writeParcelable(selectedPicture, flags)
      parcel.writeParcelable(selectedBitmap, flags)
   }

   override fun describeContents(): Int {
      return 0
   }

   companion object CREATOR : Parcelable.Creator<SignUpFragment> {
      override fun createFromParcel(parcel: Parcel): SignUpFragment {
         return SignUpFragment(parcel)
      }

      override fun newArray(size: Int): Array<SignUpFragment?> {
         return arrayOfNulls(size)
      }
   }
}