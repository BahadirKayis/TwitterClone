package com.bhdr.twitterclone.ui.addtweet

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.common.*
import com.bhdr.twitterclone.databinding.FragmentAddTweetBinding
import com.google.android.material.snackbar.Snackbar
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.*

@AndroidEntryPoint
class AddTweetFragment : Fragment(R.layout.fragment_add_tweet) {
   private val binding by viewBinding(FragmentAddTweetBinding::bind)
   private val viewModel: AddTweetViewModel by viewModels()

   private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
   private lateinit var permissionLauncher: ActivityResultLauncher<String>

   var selectedBitmap: Bitmap? = null
   var tweetImage: Uri? = null

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      binding()
      registerLauncher()
      observable()
   }

   private fun addTweet(tweetText: String) {
      val uuid = UUID.randomUUID()
      val tweetImageName = "$uuid.jpg"
      if (tweetText.isNotEmpty()) {
         if (tweetImage != null) {

            viewModel.addTweet(
               requireContext().userId(),
               tweetText,
               tweetImageName,
               tweetImage!!
            )
            //    Navigation.findNavController(requireView()).navigate(R.id.action_addTweetFragment_to_homeFragment)

         } else {
            viewModel.addTweet(requireContext().userId(), tweetText, "null", null)
            //  Navigation.findNavController(requireView()).navigate(R.id.action_addTweetFragment_to_homeFragment)
         }
      } else {
         Snackbar.make(requireView(), "Lütfen metin giriniz!", 1000).show()
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
                  tweetImage = intentFromResult.data
                  try {
                     if (Build.VERSION.SDK_INT >= 28) {
                        val source = ImageDecoder.createSource(
                           requireActivity().contentResolver,
                           tweetImage!!
                        )
                        selectedBitmap = ImageDecoder.decodeBitmap(source)
                        binding.tweetImage.setImageBitmap(selectedBitmap)
                     } else {
                        selectedBitmap = MediaStore.Images.Media.getBitmap(
                           requireActivity().contentResolver,
                           tweetImage
                        )
                        binding.tweetImage.setImageBitmap(selectedBitmap)
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
               val intentToGallery =
                  Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
               activityResultLauncher.launch(intentToGallery)
            } else {
               Toast.makeText(requireContext(), "Permisson needed!", Toast.LENGTH_LONG).show()
            }
         }
   }

   private fun observable() {
      with(viewModel) {
         mainStatus.observe(viewLifecycleOwner) {
            when (it!!) {
               Status.LOADING -> binding.lottiAnim.visible()
               Status.DONE -> binding.lottiAnim.gone()
               Status.ERROR -> binding.lottiAnim.gone()
            }

         }
         tweetAdded.observe(viewLifecycleOwner) {
            when (it) {
               true -> Navigation.findNavController(requireView())
                  .navigate(R.id.action_addTweetFragment_to_mainScreenFragment)
               else -> snackBar(requireView(), "Sorun oluştu lütfen tekrar deneyiniz", 1000)
            }
         }
      }
   }

   private fun binding() {
      with(binding) {
         profilePicture.picasso(requireContext().userPhotoUrl())
         addImage.setOnClickListener { selectImage() }
         cancel.setOnClickListener { Navigation.findNavController(requireView()).popBackStack() }
         addTweetButton.setOnClickListener { addTweet(tweetEditText.text.toString()) }
      }
   }
}