package com.baixingkuaizu.live.android.busiess.permission

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class Baixing_PermissionManager(private val mBaixing_activity: AppCompatActivity) {
    private var mBaixing_permissionCallback: ((Boolean) -> Unit)? = null
    
    private val mBaixing_permissionLauncher: ActivityResultLauncher<Array<String>> =
        mBaixing_activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allGranted = permissions.all { it.value }
            mBaixing_permissionCallback?.invoke(allGranted)
        }

    private val mBaixing_requiredPermissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.INTERNET
    )

    fun baixing_checkAndRequestPermissions(callback: ((Boolean) -> Unit)? = null) {
        mBaixing_permissionCallback = callback
        
        val permissionsToRequest = mBaixing_requiredPermissions.filter {
            ContextCompat.checkSelfPermission(mBaixing_activity, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (permissionsToRequest.isEmpty()) {
            if (callback != null)
                callback(true)
            return
        }

        mBaixing_permissionLauncher.launch(permissionsToRequest)
    }
}