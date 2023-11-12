package com.example.capstonedesign.ui.fragment

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.capstonedesign.R
import com.example.capstonedesign.databinding.FragmentCameraBinding
import com.example.capstonedesign.ui.activity.CameraSearchListActivity
import com.example.capstonedesign.utils.Constants.Companion.TAG
import java.io.File
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class CameraFragment : Fragment() {

    private lateinit var binding: FragmentCameraBinding
    private var imageCapture: ImageCapture? = null

    private lateinit var outputDirectory: File
    private lateinit var savedUri: Uri
    private lateinit var cameraExecutor: ExecutorService


    private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)

    override fun onCreate(savedInstanceState: Bundle?) {
        if (allPermissionsGrandted() == false) {
            ActivityCompat.requestPermissions(
                requireActivity(), REQUIRED_PERMISSIONS, 101
            )
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCameraBinding.inflate(inflater, container, false)

        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        startCamera()
        binding.captureButton.setOnClickListener {
            takePhoto()
        }
        binding.capturePreview.setOnClickListener {
            if (::savedUri.isInitialized) {
                val intent = Intent(activity, CameraSearchListActivity::class.java)
                intent.putExtra("imgUri", savedUri.toString())
                Log.d("imgUri", savedUri.toString())
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "사진을 먼저 촬영해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val sound = android.media.MediaActionSound()
        sound.play(android.media.MediaActionSound.SHUTTER_CLICK)
        val photoFile = File(
            outputDirectory,
            String.format(Locale.KOREA, "%s.jpg", System.currentTimeMillis())
        )
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    savedUri = Uri.fromFile(photoFile)
                    val msg = "사진 저장됨.: $savedUri"

                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)

                    // Set the image URI after it's saved
                    binding.capturePreview.setImageURI(savedUri)
                }
            })
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder().build()
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
        cameraProviderFuture.addListener(Runnable {
            try {
                preview.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                imageCapture = ImageCapture.Builder()
                    .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                    .build()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun getOutputDirectory(): File {
        val mediaDir = requireContext().externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireContext().filesDir
    }


    private fun allPermissionsGrandted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
    }

    override fun onPause() {
        super.onPause()
        cameraExecutor.shutdown()
    }

}