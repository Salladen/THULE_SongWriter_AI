import re, os, string

def main():
    re_path = r"../Data/regexString"
    wlist_path = r"../Data/swe_wordlist_v2.txt"
    songs_path = r"../Data/Songs"

    with open(re_path, "r") as f:
        reg_str = re.compile(
            fr'({f.read()})', re.UNICODE
        )

    with open(wlist_path, "r") as f:
        add_words = ["unknown", "\n", " "]
        words = f.read().lower().split("\n")
        [words.insert(0, word) for word in add_words]

        words = list(set(words))
        words.pop(0)
        words.sort(reverse=True)
        words_2_index = dict(zip(words, range(len(words))))
    print(words[-1])
    words_escapes = [word for word in words if word[0] in string.ascii_letters ]
    reg_str = re.compile(
        fr'({"|".join(words_escapes)})', re.UNICODE
    )

    song_amount = len([name for name in os.listdir(songs_path) if not os.path.isdir(name)])
    song_amount = 3
    for f in (open(f"{songs_path}/{str(i)}.txt", "r+") for i in range(song_amount)):
        print(f.name)
        song_text = f.read().lower()
        a = [match[0] for match in reg_str.finditer(song_text) if song_text[match.start() - 1] in string.ascii_letters]
        print(a)




if __name__ == '__main__':
    main()