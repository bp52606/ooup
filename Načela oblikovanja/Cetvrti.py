def mymax(iterable, key=None):
    # incijaliziraj maksimalni element i maksimalni ključ
    max_x = max_key = None

    # obiđi sve elemente
    for x in iterable:
        if not key:
            key = lambda x: x
        if not max_key:
            max_key = key(x)
        if not max_x:
            max_x = x
        # ako je key(x) najveći -> ažuriraj max_x i max_key
        if key(x) > max_key:
            max_x = x
            max_key = key(x)

    # vrati rezultat
    return max_x


if __name__ == '__main__':
    D = {'burek': 8, 'buhtla': 5}
    dict = lambda x: D.get(x)

    tup = lambda x: str(x[0] + x[1])

    maxint = mymax([1, 3, 5, 7, 4, 6, 9, 2, 0])
    print(maxint)
    maxchar = mymax("Suncana strana ulice")
    print(maxchar)
    maxstring = mymax([
        "Gle", "malu", "vocku", "poslije", "kise",
        "Puna", "je", "kapi", "pa", "ih", "njise"])
    print(maxstring)

    maxprice = mymax(D, dict)
    print(maxprice)

    tuple1 = ("Ime", "Prezime")
    tuple2 = ("Ime", "Prezimena")

    list = [tuple1, tuple2]

    maxName = mymax(list, tup)
    print(maxName)

