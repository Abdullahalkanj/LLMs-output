# Problem ID: p02703_s941258054
# Fixed version
import heapq
N , M , S = map ( int , input ( ) . split ( ) )
Cost = [ [ ] for _ in range ( N ) ]
Change = [ [ 0 ] * 2 for _ in range ( N ) ]
A_m = 0
for i in range ( M ) :
    u , v , a , b = map ( int , input ( ) . split ( ) )
    A_m = max ( A_m , a )
    Cost [ u - 1 ] . append ( [ v - 1 , a , b ] )
    Cost [ v - 1 ] . append ( [ u - 1 , a , b ] )
for i in range ( N ) :
    c , d = map ( int , input ( ) . split ( ) )
    Change [ i ] [ 0 ] = c
    Change [ i ] [ 1 ] = d
if S > A_m * N :
        S = A_m * N
def dijkstra ( N , start , Cost , Change ) :
    rich = [ [ ] for _ in range ( N + 1 ) ]
    Ans = [ 10 ** 18 ] * N
    Ans [ 0 ] = 0
    minHeap = [ ]
    used = [ [ 10 ** 18 ] * ( A_m * N + 1 ) for _ in range ( N ) ]
    used [ start ] [ S ] = 0
    heapq . heappush ( minHeap , ( 0 , S , start ) )
    while minHeap :
        ( c , hold , current ) = heapq . heappop ( minHeap )
        if rich [ current ] and rich [ current ] [ - 1 ] >= hold :
            continue
        rich [ current ] . append ( hold )
        j = 1
        for i in range ( N ) :
            if Ans [ i ] > c :
                j = 0
                break
        if j == 1 :
            break
        for P in Cost [ current ] :
            road , a , b = P
            if hold >= a :
                if used [ road ] [ hold - a ] > c + b :
                    used [ road ] [ hold - a ] = c + b
                    heapq . heappush ( minHeap , ( c + b , hold - a , road ) )
                    if Ans [ road ] > c + b :
                       Ans [ road ] = c + b
            if hold + Change [ current ] [ 0 ] <= A_m * N :
                if used [ current ] [ hold + Change [ current ] [ 0 ] ] > c + Change [ current ] [ 1 ] :
                    heapq . heappush ( minHeap , ( c + Change [ current ] [ 1 ] , hold + Change [ current ] [ 0 ] , current ) )
    return Ans
T = dijkstra ( N , 0 , Cost , Change )
for i in T [ 1 : ] :
    print ( i )
    