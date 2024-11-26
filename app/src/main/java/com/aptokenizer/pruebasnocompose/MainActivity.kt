package com.aptokenizer.pruebasnocompose

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aptokenizer.tokenizer.TokenizerConfig
import com.aptokenizer.tokenizer.domain.models.Environment
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        TokenizerConfig.init(environment = Environment.Sandbox, seeLogs = true)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buttonRevealAndroidViewActivity = findViewById<MaterialButton>(R.id.button_reveal_android_view_activity)
        val buttonCollectAndroidViewActivity = findViewById<MaterialButton>(R.id.button_collect_android_view_activity)

        buttonRevealAndroidViewActivity.setOnClickListener {
            startActivity(
                Intent(this, RevealerAndroidViewsActivity::class.java)
            )
        }

        buttonCollectAndroidViewActivity.setOnClickListener {
            startActivity(
                Intent(this, CollectAndroidViewsActivity::class.java)
            )
        }
    }
}