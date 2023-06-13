from flask import Flask, abort, request
import os
import torch
from playsound import playsound
import threading
from transliterate import translit

device = torch.device('cpu')
torch.set_num_threads(4)
local_file = 'model.pt'

if not os.path.isfile(local_file):
    torch.hub.download_url_to_file('https://models.silero.ai/models/tts/ru/v3_1_ru.pt', local_file)

model = torch.package.PackageImporter(local_file).load_pickle('tts_models', 'model')
model.to(device)

app = Flask(__name__)


@app.route('/')
def hello():
    return 'Silero Hello World!'


isPlaying = False
speaker = 'baya'


def play_audio(audio_paths):
    global isPlaying
    isPlaying = True
    playsound(audio_paths)
    isPlaying = False


def transliterate_to_russian(text):
    # Transliterate the text to Russian
    transliterated_text = translit(text, 'ru', reversed=False)
    print(transliterated_text)
    return transliterated_text


@app.route('/tts', methods=['GET'])
def handler():
    text = request.args.get('text')

    if text is None:
        abort(400)

    text = transliterate_to_russian(text)

    sample_rate = 48000

    global speaker
    audio_paths = model.save_wav(text=text, speaker=speaker, sample_rate=sample_rate)

    # Start audio playback in a separate thread
    thread = threading.Thread(target=play_audio, args=(audio_paths,), daemon=True)
    thread.start()

    # This will be automatically converted to JSON.
    return {'response': 'accepted'}


@app.route('/status', methods=['GET'])
def status():
    global isPlaying
    return {'isPlaying': isPlaying}


@app.route('/speaker/set', methods=['GET'])
def set_speaker():
    needed_speaker = request.args.get('speaker')

    if needed_speaker is None:
        abort(400)

    global speaker
    speaker = needed_speaker

    return {'speaker': speaker}


if __name__ == '__main__':
    app.run()
