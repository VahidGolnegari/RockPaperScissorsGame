package com.vahidgolnegari.rpcgame

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var playButton: Button
    private lateinit var boardGame: BoardGame
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        playButton = this.findViewById(R.id.btn_play)
        boardGame = this.findViewById(R.id.board_game)
        playButton.setOnClickListener {
            boardGame.playPauseGame()
        }
        boardGame.setListener(object : IBoardGameListener {

            override fun onPlayStatusChanged(isPlaying: Boolean) {
                playButton.text = if (isPlaying) "Pause" else "Play"
            }
        })
    }

    override fun onPause() {
        super.onPause()
        boardGame.forcePause()
    }
}
