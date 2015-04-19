# Suit #

An enum consisting of:
Clubs, Diamonds, Hearts, Spades


# Rank #

An enum consisting of:
2, 3, 4, 5, 6, 7, 8, 9, 10, Jack, Queen, King, Ace


# Card #

Rank x Suit, there are 52 cards in total

  * You can create a new card with Card.valueOf(Rank, Suit)
  * You can also use Card.valueOfCard(String name) – the “Card” after “valueOf” was added because Card.valueOf(String) is already defined in enums. This method works with Strings such as: 5h, Qd, 6c, Ac, 6s.
  * There are two ways that a Card is indexed, Card.index():
|2c =  1|2d = 14|2h = 27|2s = 40|
|:------|:------|:------|:------|
|3c =  2|3d = 15|3h = 28|3s = 41|
|4c =  3|4d = 16|4h = 29|4s = 42|
|5c =  4|5d = 17|5h = 30|5s = 43|
|6c =  5|6d = 18|6h = 31|6s = 44|
|7c =  6|7d = 19|7h = 32|7s = 45|
|8c =  7|8d = 20|8h = 33|8s = 46|
|9c =  8|9d = 21|9h = 34|9s = 47|
|Tc =  9|Td = 22|Th = 35|Ts = 48|
|Jc = 10|Jd = 23|Jh = 36|Js = 49|
|Qc = 11|Qd = 24|Qh = 37|Qs = 50|
|Kc = 12|Kd = 25|Kh = 38|Ks = 51|
|Ac = 13|Ad = 26|Ah = 39|As = 52|

  * Card.invertedIndex() is:
|2c =  1|2d =  2|2h =  3|2s =  4|
|:------|:------|:------|:------|
|3c =  5|3d =  6|3h =  7|3s =  8|
|4c =  9|4d = 10|4h = 11|4s = 12|
|5c = 13|5d = 14|5h = 15|5s = 16|
|6c = 17|6d = 18|6h = 19|6s = 20|
|7c = 21|7d = 22|7h = 23|7s = 24|
|8c = 25|8d = 26|8h = 27|8s = 28|
|9c = 29|9d = 30|9h = 31|9s = 32|
|Tc = 33|Td = 34|Th = 35|Ts = 36|
|Jc = 37|Jd = 38|Jh = 39|Js = 40|
|Qc = 41|Qd = 42|Qh = 43|Qs = 44|
|Kc = 45|Kd = 46|Kh = 47|Ks = 48|
|Ac = 49|Ad = 50|Ah = 51|As = 52|