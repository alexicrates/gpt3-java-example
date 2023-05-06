#!/usr/bin/python

from flask import Flask, abort, request
import os
import torch
from playsound import playsound

device = torch.device('cpu')
torch.set_num_threads(4)
local_file = 'model.pt'

if not os.path.isfile(local_file):
    torch.hub.download_url_to_file('https://models.silero.ai/models/tts/ru/v3_1_ru.pt'
                                   , local_file)

model = torch.package.PackageImporter(local_file).load_pickle('tts_models', 'model')
model.to(device)

app = Flask(__name__)

@app.route('/')
def hello():
    return 'Silero Hello World!'


@app.route('/tts', methods=['GET'])
def handler():

    text = request.args.get('text')

    if text is None:
        abort(400)

    sample_rate = 48000
    speaker = 'baya'

    audio_paths = model.save_wav(text=text, speaker=speaker,
                                 sample_rate=sample_rate)

    playsound(audio_paths, block = False)

    # This will be automatically converted to JSON.
    return {'response': 'accepted'}

