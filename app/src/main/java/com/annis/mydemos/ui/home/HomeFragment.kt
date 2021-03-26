package com.annis.mydemos.ui.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.annis.mydemos.databinding.FragmentHomeBinding
import androidx.core.content.FileProvider.getUriForFile
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

open class HomeFragment : Fragment() {
    companion object {
        const val INTENT_CAMERA_REQUEST = 1254
        const val PERMISSION_REQUEST_CAMERA = 2345
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
                    openCamera()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                    openCamera()
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

    private var photoFile: File? = null
    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra("outputFormat", "JPEG")
            val imagePath = File(requireContext().filesDir, "images")
            if (!imagePath.exists()) {
                imagePath.mkdir()
            }

            val format = SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date())
            photoFile = File(imagePath, "$format.jpg")
            val contentUri =
                getUriForFile(requireContext(), "com.annis.mydemos.provider", photoFile!!)

            putExtra(MediaStore.EXTRA_OUTPUT, contentUri)
        }.let {
            startActivityForResult(it, INTENT_CAMERA_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            INTENT_CAMERA_REQUEST -> {
                if (photoFile?.exists() == true) {
                    var opt = BitmapFactory.Options()
                    opt.inJustDecodeBounds = true
                    val bitmap = BitmapFactory.decodeFile(photoFile!!.absolutePath, opt)

                    Bitmap.createBitmap(bitmap,0,0,bitmap.width,bitmap.height, Matrix().apply { setScale(200.0f,400.0f) },true)
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