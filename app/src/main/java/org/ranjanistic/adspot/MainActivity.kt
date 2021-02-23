package org.ranjanistic.adspot

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.ranjanistic.adspot.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.status.text = this.status()
    }

    private fun status(): String {
        return "Stopping"
    }
}