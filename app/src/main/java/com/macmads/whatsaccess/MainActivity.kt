package com.macmads.whatsaccess

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.macmads.whatsaccess.databinding.ActivityMainBinding
import com.macmads.whatsaccess.services.WhatsappAccessibilityService

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.sendBtn.setOnClickListener {
            if (!isAccessibilityOn(this@MainActivity, WhatsappAccessibilityService::class.java)) {
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                this@MainActivity.startActivity(intent)
            } else {
                val smsNumber = "917875648090"
                val sendIntent = Intent(Intent.ACTION_SEND)
                sendIntent.type = "text/plain"
                sendIntent.putExtra(Intent.EXTRA_TEXT, "mysuffix")
                sendIntent.putExtra("jid", "$smsNumber@s.whatsapp.net")
                sendIntent.setPackage("com.whatsapp")
                startActivity(sendIntent)
            }
        }
    }

    private fun isAccessibilityOn(
        context: Context,
        clazz: Class<out AccessibilityService?>
    ): Boolean {
        var accessibilityEnabled = 0
        val service: String = context.packageName + "/" + clazz.canonicalName
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                context.applicationContext.contentResolver,
                Settings.Secure.ACCESSIBILITY_ENABLED
            )
        } catch (ignored: Settings.SettingNotFoundException) {
        }
        val colonSplitter = TextUtils.SimpleStringSplitter(':')
        if (accessibilityEnabled == 1) {
            val settingValue: String = Settings.Secure.getString(
                context.applicationContext.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            colonSplitter.setString(settingValue)
            while (colonSplitter.hasNext()) {
                val accessibilityService = colonSplitter.next()
                if (accessibilityService.equals(service, ignoreCase = true)) {
                    return true
                }
            }
        }
        return false
    }
}