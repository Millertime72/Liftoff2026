from typing import Optional

import sounddevice as sd
import speech_recognition as sr
from scipy.io.wavfile import write as write_as_wav


SAMPLE_RATE = 16000

class Recorder:
    def __init__(self):
        self._r = sr.Recognizer()
    
    def record(self, duration: int) -> sr.AudioData:
        audio = sd.rec(
            int(duration * SAMPLE_RATE),
            samplerate=SAMPLE_RATE,
            channels=1,
            dtype="int16"
        )

        sd.wait()

        audio_bytes = audio.tobytes()
        audio_data = sr.AudioData(
            frame_data=audio_bytes,
            sample_rate=SAMPLE_RATE,
            sample_width=2 # int16 = 2 bytes
        )

        return audio_data
    
    def speech_to_text(self, audio_data: sr.AudioData) -> Optional[str]:
        try:
            return self._r.recognize_google(audio_data)
        except (sr.UnknownValueError, sr.RequestError):
            return None