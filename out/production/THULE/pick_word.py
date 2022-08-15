"""
I think I'm going to match strings using \w+ & [^\w]+ and find the string in my list with the closest Levenshtein distance then largest Ratcliff-Obershelp similarity
And adding to the input which indices of that string (from the list) has to be shifted, and how much (character set wise relative to the previous character left-to-right) to create the matched string
So that the AI get the sense of "editing" words
"""
import numbers
import os, sys, time, re, textdistance
import string

import numpy as np
from textdistance import Levenshtein, RatcliffObershelp


# Gets the most similar word from the wordlist to the string s1
def getMostSimilarWord(s1, wordlist):
    foundWord = ""


    return foundWord

def main():
    re_path = r"../Data/regexString"
    wlist_path = r"../Data/swe_wordlist.txt"

    with open(re_path, "r") as f:
        reg_str = re.compile(
            fr'({f.read()})', re.UNICODE
        )

    with open(wlist_path, "r") as f:
        words = f.read().lower().split("\n")
        words = list(set(words))
        words.sort()
        print(string.punctuation)
        words_2_index = dict(zip(words, range(len(words))))
        words_2_index.update({"unknown": len(words_2_index), "\n": len(words_2_index) + 1})
    s1 = "riksdag"
    t_list = []
    iter_sum = 0
    for i, x in enumerate(words):
        start = time.perf_counter_ns()
        t_list.append((Levenshtein().distance(x, s1),RatcliffObershelp().similarity(x, s1), x))
        iter_sum += time.perf_counter_ns() - start

        if (i + 1) % int(len(words) * 0.05) == 0:
            os.system('cls')
            print(f"{i / len(words)=}",
                  f"avg iter_time={iter_sum / 10 ** 9 / (i + 1)=}seconds",
                  f"ETA:{(iter_sum / 10 ** 9 / (i + 1))*(len(words) - i - 1)} seconds", sep="\n")
    t_list.sort(key=lambda x: (x[0], 1 - x[1]))
    [print(f"{i}: {e}") for i, e in enumerate(t_list[0:10])]

if __name__ == '__main__':
    main()
