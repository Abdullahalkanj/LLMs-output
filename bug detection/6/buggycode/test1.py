# Problem ID: p03244_s908501993
# Buggy version
def main ( n : int , lst : list ) -> int :
b = True
    lst1 = [ ]
    lst2 = [ ]
    for e in lst :
    if b :
        lst1 . append ( e )
            else :
        lst2 . append ( e )
            b = not b
        lst1 . sort ( )
    lst2 . sort ( )
    c1 = [ [ " " , 0 ] , 0 ]
    c2 = [ [ " " , 0 ] , 0 ]
    last = lst1 [ 0 ]
    a = 0
    for e in lst1 :
    if last == e :
        a += 1
            else :
        if a >= c1 [ 0 ] [ 1 ] :
            c1 [ 1 ] = c1 [ 0 ] [ 1 ]
                c1 [ 0 ] [ 0 ] = e
                c1 [ 0 ] [ 1 ] = a
                elif a > c1 [ 1 ] :
            c1 [ 1 ] = a
                last = e
            a = 1
            if a >= c1 [ 0 ] [ 1 ] :
    c1 [ 1 ] = c1 [ 0 ] [ 1 ]
        c1 [ 0 ] [ 0 ] = e
        c1 [ 0 ] [ 1 ] = a
        elif a > c1 [ 1 ] :
    c1 [ 1 ] = a
        last = lst2 [ 0 ]
    a = 0
    for e in lst2 :
    if last == e :
        a += 1
            else :
        if a >= c2 [ 0 ] [ 1 ] :
            c2 [ 1 ] = c2 [ 0 ] [ 1 ]
                c2 [ 0 ] [ 0 ] = e
                c2 [ 0 ] [ 1 ] = a
                elif a > c2 [ 1 ] :
            c2 [ 1 ] = a
                last = e
            a = 1
            if a >= c2 [ 0 ] [ 1 ] :
    c2 [ 1 ] = c2 [ 0 ] [ 1 ]
        c2 [ 0 ] [ 0 ] = e
        c2 [ 0 ] [ 1 ] = a
        elif a > c2 [ 1 ] :
    c2 [ 1 ] = a
        if c1 [ 0 ] [ 0 ] == c2 [ 0 ] [ 0 ] :
    if c1 [ 0 ] [ 1 ] > c2 [ 0 ] [ 1 ] or ( c1 [ 0 ] [ 1 ] == c2 [ 0 ] [ 1 ] and c2 [ 1 ] > c1 [ 1 ] ) :
        return n - c1 [ 0 ] [ 1 ] - c2 [ 1 ]
            else :
        return n - c2 [ 0 ] [ 1 ] - c1 [ 1 ]
            else :
    return n - c1 [ 0 ] [ 1 ] - c2 [ 0 ] [ 1 ]
        n = int ( input ( ) )
lst = input ( ) . split ( )
print ( main ( n , lst ) )
