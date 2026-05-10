from enum import Enum
from typing import List, Tuple

import re

import contractions
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

    QUESTIONS = ["who", "what", "where", "when", "why", "how"]

    def __init__(self):
        self._l = WordNetLemmatizer()

    def extract_keywords(self, sentence: str) -> List[Tuple[str | Grammar.Info, bool]]:
        sentence = re.sub("(?![A-Za-z\\s'])", "", sentence)
        sentence = contractions.fix(sentence)
        tokens = re.split("\\s+", sentence)
        tokens = [t.lower() if t != "I" else t for t in tokens]
        
        keywords = []
        tags = nltk.pos_tag(tokens)
        previous_was_adj = False

        for word, pos in tags:
            if pos in ("NN", "VB", "VBP", "VBZ", "VBG"):
                keywords.append((word, False))
            elif pos == "NNS":
                singular = self._l.lemmatize(word, pos="n")
                
                idx = -1 if previous_was_adj else len(keywords)

                keywords.insert(idx, (self.Quantity.MULTIPLE, False))
                keywords.append((singular, False))
            elif pos in ("VBD", "VBN"):
                non_conjugated = self._l.lemmatize(word, pos="v")
                keywords.append((self.Tense.PAST, True))
                keywords.append((non_conjugated, False))
            elif pos == "MD":
                keywords.append((self.Tense.FUTURE, True))
            elif pos == "JJ" and not previous_was_adj:
                keywords.append((word, True))
                previous_was_adj = True
            elif word in ("I", "me"):
                keywords.append((self.Subject.ME, False))
            elif word in ("you", "your"):
                keywords.append((self.Subject.YOU, False))
            elif word in self.QUESTIONS:
                keywords.append((word, False))
            
            if pos != "JJ" and previous_was_adj:
                previous_was_adj = False

        return keywords