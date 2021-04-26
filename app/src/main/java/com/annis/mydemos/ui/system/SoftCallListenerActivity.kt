package com.annis.mydemos.ui.system

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import com.annis.mydemos.R

class SoftCallListenerActivity : AppCompatActivity(), AudioManager.OnAudioFocusChangeListener {

    val tag = "SoftCallListenerActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soft_call_listener)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setSoftCallListener()
        } else {
            setSoftCallListenerD8()
        }
    }

    lateinit var mediaPlayer: MediaPlayer

    lateinit var focusRequest: AudioFocusRequest
    lateinit var audioManager: AudioManager


    private fun setSoftCallListenerD8() {
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        val result: Int = audioManager.requestAudioFocus(
            this,
            AudioManager.STREAM_MUSIC,
            AudioManager.AUDIOFOCUS_GAIN
        )

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // Start playback
            pausePlayback()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setSoftCallListener() {
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
            .run {
                setAudioAttributes(AudioAttributes.Builder().run {
                    setUsage(AudioAttributes.USAGE_GAME)
                    setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    build()
                })
                setAcceptsDelayedFocusGain(true)
                setOnAudioFocusChangeListener(this@SoftCallListenerActivity, Handler())
                build()
            }
        mediaPlayer = MediaPlayer()

        val res = audioManager.requestAudioFocus(focusRequest)
        synchronized(focusLock) {
            playbackNowAuthorized = when (res) {
                AudioManager.AUDIOFOCUS_REQUEST_FAILED -> false
                AudioManager.AUDIOFOCUS_REQUEST_GRANTED -> {
                    playbackNow()
                    true
                }
                AudioManager.AUDIOFOCUS_REQUEST_DELAYED -> {
                    playbackDelayed = true
                    false
                }
                else -> false
            }
        }
    }

    val focusLock = Any()
    var playbackDelayed = false
    var playbackNowAuthorized = false
    var resumeOnFocusGain = false
    override fun onAudioFocusChange(focusChange: Int) {
        Log.w(tag, "onAudioFocusChange:${focusChange}")
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN ->
                if (playbackDelayed || resumeOnFocusGain) {
                    synchronized(focusLock) {
                        playbackDelayed = false
                        resumeOnFocusGain = false
                    }
                    playbackNow()
                }
            AudioManager.AUDIOFOCUS_LOSS -> {
                synchronized(focusLock) {
                    resumeOnFocusGain = false
                    playbackDelayed = false
                }
                pausePlayback()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                synchronized(focusLock) {
                    resumeOnFocusGain = true
                    playbackDelayed = false
                }
                pausePlayback()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                // ... pausing or ducking depends on your app
                Log.w(tag, "depends your dear")
            }
        }
    }

    private fun playbackNow() {
        Log.w(tag, "playbackNow")
    }

    private fun pausePlayback() {
        Log.w(tag, "pausePlayback")
    }

    private fun playComplete() {
        Log.w(tag, "playComplete")
        audioManager.abandonAudioFocus(this)
    }
}