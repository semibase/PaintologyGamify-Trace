package com.paintology.lite.trace.drawing.challenge.enums

import com.paintology.lite.trace.drawing.challenge.TutorialChallengeMode

sealed class ChallengeEvent {
    object OnLevelMeterClick : ChallengeEvent()
    object OnGalleryClick : ChallengeEvent()
    class OnDetailClick(val data: TutorialChallengeMode) : ChallengeEvent()

    data class OnLikeClick(val data: TutorialChallengeMode,val pos:Int):ChallengeEvent()
    class OnOpenTutorial(val data: TutorialChallengeMode): ChallengeEvent()
}
