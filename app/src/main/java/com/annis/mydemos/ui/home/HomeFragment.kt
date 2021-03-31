package com.annis.mydemos.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.annis.mydemos.databinding.FragmentHomeBinding
import androidx.core.content.FileProvider.getUriForFile
import com.annis.mydemos.utils.FileUtil
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

open class HomeFragment : Fragment() {
    companion object {
        const val INTENT_CAMERA_REQUEST = 1254
        const val INTENT_CAMERA_CROP_REQUEST = 1241
        const val PERMISSION_REQUEST_CAMERA = 2345
        const val TAG = "HomeFragment"
    }

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

        homeViewModel.text.observe(viewLifecycleOwner, Observer {

        })

        binding.abtCamera.text = "点击-拍照"
        binding.abtCamera.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) ==
                        PackageManager.PERMISSION_GRANTED -> {
                    checkStorage()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                    checkStorage()
                }
                else -> {
                    requestPermissions(
                        arrayOf(Manifest.permission.CAMERA),
                        PERMISSION_REQUEST_CAMERA
                    )
                }
            }
        }
        return root
    }

    private fun checkStorage() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) ==
                    PackageManager.PERMISSION_GRANTED -> {
                openCamera()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                openCamera()
            }
            else -> {
                requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CAMERA
                )
            }
        }
    }

    private var photoFile: File? = null
    private var cropFile: File? = null
    private var photoUri: Uri? = null
    private var cropUri: Uri? = null
    private fun openCamera() {
        val imageDir = File(requireContext().filesDir, "images")
        if (!imageDir.exists()) {
            imageDir.mkdir()
        }
        FileUtil.setAuthority("com.annis.mydemos.provider");
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date())

        photoFile = File(imageDir, "$format.jpg")
        photoUri = FileUtil.getUri(requireContext(), photoFile!!)

//        cropFile = File(imageDir, "${format}_crop.jpg")
//        cropFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "${format}_crop.jpg")
        cropFile = File(Environment.getExternalStorageDirectory(), "${format}_crop.jpg")
        cropUri = FileUtil.getUri(requireContext(), cropFile!!)
        Log.w("Storage", cropFile!!.absolutePath)
        withoutCrop(photoUri!!)
    }


    private fun withoutCrop(uri: Uri) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra("outputFormat", "JPEG")
            putExtra(MediaStore.EXTRA_OUTPUT, uri)
        }.let {
            startActivityForResult(it, INTENT_CAMERA_REQUEST)
        }
    }

    private fun withCrop(uri: Uri, outputUri: Uri) {
        Intent("com.android.camera.action.CROP").apply {
            setDataAndType(uri, "image/*")
            putExtra("crop", "true")
            putExtra("outputX", 200)
            putExtra("outputY", 200)
            putExtra("aspectX", 1)
            putExtra("aspectY", 1)
            putExtra("scale", true)
            putExtra("return-data", false)
            putExtra("noFaceDetection", true)

            putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//            putExtra(MediaStore.EXTRA_OUTPUT, outputUri)

            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        }.let {
            startActivityForResult(it, INTENT_CAMERA_CROP_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            INTENT_CAMERA_REQUEST -> {
                if (photoFile?.exists() == true) {
//                    var opt = BitmapFactory.Options()
//                    opt.inJustDecodeBounds = true
//                    val bitmap = BitmapFactory.decodeFile(photoFile!!.absolutePath, opt)
//                    Bitmap.createBitmap(bitmap,0,0,bitmap.width,bitmap.height, Matrix().apply { setScale(200.0f,400.0f) },true)

                    withCrop(photoUri!!, cropUri!!)
                }
            }
            INTENT_CAMERA_CROP_REQUEST -> {
                val cropImageUri = data?.data

                FileUtil.getPathFromUri(activity, cropImageUri).let {
                    Log.w(TAG, it)
                }
            }
            else -> {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            grantResults.forEach {
                if (it == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * 保存到文件
     *
     * @param bitmap    要保存的bitmap
     * @param outFormat 输出的图片格式
     * @param callback  保存完bitmap后回调(包含文件保存地址)
     */
    fun save(bitmap: Bitmap, outFormat: CompressFormat?, savePath: String) {
        val destFile: File = File(savePath)
        if (!destFile.parentFile.exists()) {
            destFile.parentFile.mkdir()
        }
        object : AsyncTask<String?, String?, File?>() {
            override fun doInBackground(vararg strings: String?): File? {
                var bos: BufferedOutputStream? = null
                return try {
                    bos = BufferedOutputStream(FileOutputStream(destFile))
                    bitmap.compress(outFormat, 100, bos)
                    destFile
                } catch (e: FileNotFoundException) {
                    null
                } finally {
                    try {
                        if (bos != null) {
                            bos.flush()
                            bos.close()
                        }
                    } catch (ex: IOException) {
                        ex.printStackTrace()
                    }
                }
            }

            override fun onPostExecute(file: File?) {

            }
        }.execute()
    }
}