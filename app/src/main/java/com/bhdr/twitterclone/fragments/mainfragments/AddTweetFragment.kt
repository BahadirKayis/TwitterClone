package com.bhdr.twitterclone.fragments.mainfragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import androidx.navigation.Navigation
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.databinding.FragmentAddTweetBinding
import com.bhdr.twitterclone.helperclasses.gone
import com.bhdr.twitterclone.helperclasses.snackBar
import com.bhdr.twitterclone.helperclasses.visible
import com.bhdr.twitterclone.repos.TweetRepository
import com.bhdr.twitterclone.viewmodels.mainviewmodel.TweetViewModel
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import java.io.IOException
import java.util.*


class AddTweetFragment : Fragment(R.layout.fragment_add_tweet) {
    private val binding by viewBinding(FragmentAddTweetBinding::bind)
    private val tweetViewModel by lazy { TweetViewModel() }

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    var selectedBitmap: Bitmap? = null
    var tweetImage: Uri? = null
    var preferences: SharedPreferences? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences =
            requireActivity().getSharedPreferences("com.bhdr.twitterclone", Context.MODE_PRIVATE)
        Picasso.get().load(preferences?.getString("profilepic", "null"))
            .into(binding.profilePicture)

        registerLauncher()
        tweetViewModelObservable()

        binding.addImage.setOnClickListener { selectImage() }
        binding.cancel.setOnClickListener { cancel() }
        binding.addTweetButton.setOnClickListener { addTweet(binding.tweetEditText.text.toString()) }

    }

    private fun addTweet(tweetText: String) {
        val uuid = UUID.randomUUID()
        val tweetImageName = "$uuid.jpg"

//        val prefences = requireActivity().getSharedPreferences("com.canerture.twittercloneapp", Context.MODE_PRIVATE)
//        val id = prefences.getString("id", "null")
//        val profilepic = prefences.getString("profilepic", "null")
//        val name = prefences.getString("name", "null")
//        val nickname = prefences.getString("nickname", "null")

        if (tweetText.isNotEmpty()) {
            if (tweetImage != null) {

                tweetViewModel.addTweet(1, tweetText, tweetImageName, tweetImage!!)
                //    Navigation.findNavController(requireView()).navigate(R.id.action_addTweetFragment_to_homeFragment)

            } else {
                tweetViewModel.addTweet(1, tweetText, "null", null)
                //  Navigation.findNavController(requireView()).navigate(R.id.action_addTweetFragment_to_homeFragment)
            }
        } else {
            Snackbar.make(requireView(), "Lütfen metin giriniz!", 1000).show()
        }

    }

    private fun cancel() {
        Navigation.findNavController(requireView()).popBackStack()
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

    private fun tweetViewModelObservable() {
        tweetViewModel.mainStatus.observe(viewLifecycleOwner) {
            when (it!!) {
                TweetRepository.MainStatus.LOADING -> binding.lottiAnim.visible()
                TweetRepository.MainStatus.DONE -> binding.lottiAnim.gone()
                TweetRepository.MainStatus.ERROR -> binding.lottiAnim.gone()
            }

        }
        tweetViewModel.tweetAdded.observe(viewLifecycleOwner) {
            when (it) {
                true -> Navigation.findNavController(requireView())
                    .navigate(R.id.action_addTweetFragment_to_mainScreenFragment)
                else -> snackBar(requireView(), "Sorun oluştu lütfen tekrar deneyiniz", 1000)
            }
        }
    }
}