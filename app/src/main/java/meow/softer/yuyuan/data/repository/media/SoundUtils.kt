package meow.softer.yuyuan.data.repository.media

fun String.toWordAudioPath(): String {
    return WordAudioFolder + this + WordAudioExtension
}

fun String.toSentenceAudioPath(): String {
    return SentenceAudioFolder + this + SentenceAudioExtension
}