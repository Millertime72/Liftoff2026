from typing import Dict, List, Tuple

import requests
import os
from bs4 import BeautifulSoup

from et_translator.grammar import Grammar

DEFAULT_IMAGES = {
    Grammar.Tense.PAST: "https://as2.ftcdn.net/v2/jpg/05/39/45/75/1000_F_539457536_pdrj6L3zGmknvekELr3FNxAKECOLWm6b.jpg",
    Grammar.Tense.FUTURE: "https://cdn-icons-png.flaticon.com/512/60/60635.png",
    Grammar.Quantity.MULTIPLE: "https://static.vecteezy.com/system/resources/previews/022/167/800/non_2x/asterisk-icon-asterisk-sign-isolated-on-white-background-illustration-vector.jpg",
    Grammar.Subject.ME: "https://static.vecteezy.com/system/resources/previews/049/614/310/non_2x/human-body-anatomy-transparent-background-free-png.png",
    Grammar.Subject.YOU: "https://dejpknyizje2n.cloudfront.net/processing/raster/8e6740516ca74065b7304c6a501b358b.png"
}
DELIMITER = "~"

class ImageScraper:
    def __init__(self, n_images: int):
        self._n_images = n_images

    @staticmethod
    def _get_data(url: str) -> str:
        r = requests.get(url)
        
        return r.text
    
    def keywords_to_message(self, keywords: List[Tuple[str | Grammar.Info, bool]]) -> str:
        message = ""
        keep_searching = True

        for keyword, corner_term in keywords:
            images = []
            
            if keyword in Grammar.QUESTIONS:
                images.append("https://png.pngtree.com/recommend-works/png-clipart/20241224/ourmid/pngtree-red-question-mark-symbol-png-image_14540396.png")

            elif not isinstance(keyword, Grammar.Info):
                search_string = "https://unsplash.com/s/photos/" + keyword.lower()
            
                html_data = self._get_data(search_string) 
                soup = BeautifulSoup(html_data, "html.parser")

                # Webscrape & store relevent data
                for src in [item["src"] for item in soup.find_all("img")]:
                    if "photo" in src:
                        # Hacky-ass fix
                        src = src.replace("w=3000", "w=512")
                        # Add image link to list
                        images.append(src)
                        
                        # Stop storing if enough images have been stored
                        if len(images) >= self._n_images:
                            break

            else:
                image = DEFAULT_IMAGES.get(keyword, None)

                if image is not None:
                    images.append(image)
            
            if len(images) > 0:
                message += DELIMITER.join([str(int(corner_term)), str(keyword)] + images) + "\n"
        
        return message