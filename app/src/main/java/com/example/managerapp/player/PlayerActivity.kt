package com.example.managerapp.player

import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.exoplayer.ExoPlayer
import com.example.managerapp.databinding.ActivityPlayerBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.util.MimeTypes

class PlayerActivity : AppCompatActivity() {

    lateinit var binding :ActivityPlayerBinding
    var mediaControls: MediaController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val videoUri = intent.getStringExtra("videoUri")
        val playerView = binding.playerView

        if (mediaControls == null) {
            // creating an object of media controller class
            mediaControls = MediaController(this)

            // set the anchor view for the video view
            mediaControls!!.setAnchorView(playerView)
        }

        playerView!!.setMediaController(mediaControls)

        // set the absolute path of the video file which is going to be played
        playerView!!.setVideoURI(Uri.parse(videoUri))

        playerView!!.requestFocus()

        // starting the video
        playerView!!.start()

        playerView!!.setOnCompletionListener {
            Toast.makeText(applicationContext, "Video completed",
                Toast.LENGTH_LONG).show()
            finish()
            true
        }

        // display a toast message if any
        // error occurs while playing the video
        playerView!!.setOnErrorListener { mp, what, extra ->
            Toast.makeText(applicationContext, "An Error Occurred " +
                    "While Playing Video !!!", Toast.LENGTH_LONG).show()
            finish()
            false
        }

    }


}