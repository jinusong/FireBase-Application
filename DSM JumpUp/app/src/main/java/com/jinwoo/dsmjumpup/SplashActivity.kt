package com.jinwoo.dsmjumpup

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import java.util.*

class SplashActivity: AppCompatActivity() {

    lateinit var mFirebaseRemoteConfig: FirebaseRemoteConfig
    var versionCode: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var pInfo: PackageInfo? = null
        try {
            pInfo = packageManager.getPackageInfo(this@SplashActivity.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        versionCode = pInfo!!.longVersionCode.toInt()

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

        var configSettings: FirebaseRemoteConfigSettings = FirebaseRemoteConfigSettings
            .Builder().setDeveloperModeEnabled(BuildConfig.DEBUG).build()

        var defaultConfigMap: HashMap<String, Any?> = HashMap()
        defaultConfigMap.put("version", versionCode)

        mFirebaseRemoteConfig.setConfigSettings(configSettings)
        mFirebaseRemoteConfig.setDefaults(defaultConfigMap)

        mFirebaseRemoteConfig.fetch(0)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mFirebaseRemoteConfig.activateFetched()
                    displayMessage()
                } else {
                    Log.w("SplashActivity", "Fetch failed")
                }
            }
    }

    fun displayMessage() {
        var newVersion: Int = Integer.parseInt(mFirebaseRemoteConfig.getString("versionCode"))
        if(versionCode < newVersion) {
            alert(title = "경고!", message = "최신 버전이 아닙니다!") {
                positiveButton("확인") { finish() }
            }.show()
        } else {
            startActivity<SignInActivity>()
            finish()
        }
    }
}
