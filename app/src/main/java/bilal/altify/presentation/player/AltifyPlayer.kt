package bilal.altify.presentation.player

import android.os.Looper
import androidx.media3.common.Player
import androidx.media3.common.SimpleBasePlayer
import androidx.media3.common.util.UnstableApi

@UnstableApi class AltifyPlayer: SimpleBasePlayer(Looper.getMainLooper()) {

    private val state = State.Builder()
        .setAvailableCommands(
            Player.Commands.Builder().addAllCommands().build()
        )
        .setPlayWhenReady(true, PLAY_WHEN_READY_CHANGE_REASON_USER_REQUEST)
        .setCurrentMediaItemIndex(0)
        .setContentPositionMs(0)
        .setVolume(0f)
        .setPlaybackState(Player.STATE_READY)

    override fun getState(): State = state.build()

}