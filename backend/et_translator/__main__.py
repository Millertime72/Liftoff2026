from argparse import ArgumentParser, Namespace

from et_translator.grammar import Grammar
from et_translator.image_scraper import ImageScraper
from et_translator.recorder import Recorder


def speech_to_text(args: Namespace) -> str:
    r = Recorder()

    print("Speak now!", flush=True)

    audio_data = r.record(args.duration)
    text = r.speech_to_text(audio_data)

    return text

def text_to_images(args: Namespace) -> str:
    g = Grammar()
    s = ImageScraper(args.n_images)

    keywords = g.extract_keywords(args.text)
    message = s.keywords_to_message(keywords)

    return message

parser = ArgumentParser(prog="et_translator")
subparsers = parser.add_subparsers()

speech_parser = subparsers.add_parser("speech_to_text")
speech_parser.add_argument("--duration", type=int, default=5)
speech_parser.set_defaults(func=speech_to_text)

text_parser = subparsers.add_parser("text_to_images")
text_parser.add_argument("text")
text_parser.add_argument("--n_images", type=int, default=3)
text_parser.set_defaults(func=text_to_images)

args = parser.parse_args()
output = args.func(args)

print(output, flush=True)