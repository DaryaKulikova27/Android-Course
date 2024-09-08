package com.example.androidbeginner

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidbeginner.databinding.ActivityMainBinding
import com.example.androidbeginner.exampleservice.PlaybackService

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, PlaybackService::class.java)
        window.navigationBarColor = resources.getColor(R.color.white)

        binding.startServiceBtn.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        }

        binding.stopServiceBtn.setOnClickListener {
//            stopService(intent)
//            startSecondActivity()
            startSite()
        }
    }

    private fun startSecondActivity() {
        val intent = Intent(this, SecondActivity::class.java)
        intent.putExtra(SecondActivity.KEY, "abc")
        startActivity(intent)
    }

    private fun startSite() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/"))
        startActivity(intent)
    }
}