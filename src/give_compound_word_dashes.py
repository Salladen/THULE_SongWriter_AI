import re, os, string
import sys
from textdistance import Levenshtein, RatcliffObershelp


def choose_split(origin_word, split_suggestions: list[str]):
    split_suggestions = [
        (sum([RatcliffObershelp.similarity(origin_word, part) for part in compound.split("-")]), compound)
        for compound in split_suggestions
    ]
    split_suggestions.sort(key= lambda x: 1 - x[0])
    print(split_suggestions)

    return split_suggestions[0][1]


def main():
    re_path = r"../Data/regexString"
    wlist_path = r"../Data/swe_wordlist_v2.txt"
    songs_path = r"../Data/Songs"

    with open(re_path, "r") as f:
        reg_str = re.compile(
            fr'({f.read()})', re.UNICODE
        )

    with open(wlist_path, "r", encoding="UTF-8") as f:
        add_words = ["unknown", "\n", " "]
        words = f.read().lower().split("\n")
        [words.insert(0, word) for word in add_words]

        words = list(words)
        words.sort(reverse=True)
        words_2_index = dict(zip(words, range(len(words))))

    words_escapes = [word for word in words if word[0] in string.ascii_letters ]
    words_escapes_reverse = reversed(words_escapes.copy())

    reg_str = re.compile(
        fr'({"|".join(words_escapes)})', re.UNICODE
    )
    reg_str_reversed = re.compile(
        fr'({"|".join(words_escapes_reverse)})', re.UNICODE
    )

    song_amount = len([name for name in os.listdir(songs_path) if not os.path.isdir(name)])

    iter_generator = (open(f"{songs_path}/{str(i)}.txt", "r+", encoding="UTF-8") for i in range(song_amount))
    for f in iter_generator:
        print(f.name)
        songLines = f.readlines()
        meta_data = "".join(songLines[:4])
        song_text = "".join(songLines[4:]).lower()
        reg_str_2 = re.compile(
            r"-|(\\i\\)|(\\b\\)|[åäöÅÄÖ\w]+|[\W]", re.UNICODE
        )
        for match in reg_str_2.finditer(song_text):
            if not match[0] in words:
                print(match)

                with_dash = choose_split(match[0],
                                         ["-".join((m[0] for m in reg_str.finditer(match[0]))),
                                          "-".join((m[0] for m in reg_str_reversed.finditer(match[0])))])
                song_text = song_text.replace(match[0], with_dash)

        with open(f"{f.name[:7]}/Song_Compound_Dashes/{f.name[14:]}", "w", encoding="UTF-8") as g:
            g.write(meta_data + song_text)
        f.close()




if __name__ == '__main__':
    main()