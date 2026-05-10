from enum import Enum
from typing import List

import nltk
from nltk.stem import WordNetLemmatizer


class Grammar:
    class Info(Enum):
        def __str__(self) -> str:
            return self.value

    class Tense(Info):
        PAST = "past"
        PRESENT = "present"
        FUTURE = "future"
    
    class Quantity(Info):
        SINGLE = "single"
        MULTIPLE = "multiple"
    
    class Subject(Info):
        ME = "me"
        YOU = "you"

    def __init__(self):
        self._l = WordNetLemmatizer()

    def extract_keywords(self, sentence: str) -> List[str | Grammar.Info]:
        tokens = nltk.word_tokenize(sentence)

        keywords = []

        for word, pos in nltk.pos_tag(tokens):
            if pos in ("NN", "VB", "VBP", "VBZ", "VBG"):
                keywords.append(word)
            elif pos == "NNS":
                singular = self._l.lemmatize(word, pos="n")
                keywords += [self.Quantity.MULTIPLE, singular]
            elif pos in ("VBD", "VBN"):
                non_conjugated = self._l.lemmatize(word, pos="v")
                keywords += [self.Tense.PAST, non_conjugated]
            elif pos == "MD":
                keywords.append(self.Tense.FUTURE)
            elif pos == "PRP":
                if word in ("I", "me"):
                    keywords.append(self.Subject.ME)
                elif word == "you":
                    keywords.append(self.Subject.YOU)
            elif word == "'m":
                pass

        return keywords