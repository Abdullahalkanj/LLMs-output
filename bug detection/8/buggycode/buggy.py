# Problem ID: p02703_s941258054
# Buggy version
import heapq
N , M , S = map ( int , input ( ) . split ( ) )
Cost = [ [ [ - 1 , - 1 ] for _ in range ( N ) ] for _ in range ( N ) ]
Change = [ [ 0 ] * 2 for _ in range ( N ) ]
A_m = 0
for i in range ( M ) :
u , v , a , b = map ( int , input ( ) . split ( ) )
    A_m = max ( A_m , a )
    Cost [ u - 1 ] [ v - 1 ] [ 0 ] = a
    Cost [ u - 1 ] [ v - 1 ] [ 1 ] = b
    Cost [ v - 1 ] [ u - 1 ] [ 0 ] = a
    Cost [ v - 1 ] [ u - 1 ] [ 1 ] = b
    for i in range ( N ) :
c , d = map ( int , input ( ) . split ( ) )
    Change [ i ] [ 0 ] = c
    Change [ i ] [ 1 ] = d
    if S > A_m * N :
S = A_m * N
    def dijkstra ( N , start , Cost , Change ) :
Ans = [ 10 ** 12 ] * N
    Ans [ 0 ] = 0
    minHeap = [ ]
    used = [ [ 10 ** 12 ] * ( A_m * N + 1 ) for _ in range ( N ) ]
    used [ start ] [ S ] = 0
    heapq . heappush ( minHeap , ( 0 , S , start ) )
    while minHeap :
    ( c , hold , current ) = heapq . heappop ( minHeap )
        j = 1
        for i in range ( N ) :
        if Ans [ i ] > c :
            j = 0
                break
                if j == 1 :
        break
            for road in range ( N ) :
        if Cost [ current ] [ road ] [ 0 ] != - 1 :
            if hold >= Cost [ current ] [ road ] [ 0 ] :
                if used [ road ] [ hold - Cost [ current ] [ road ] [ 0 ] ] > c + Cost [ current ] [ road ] [ 1 ] :
                    used [ road ] [ hold - Cost [ current ] [ road ] [ 0 ] ] = c + Cost [ current ] [ road ] [ 1 ]
                        heapq . heappush ( minHeap , ( c + Cost [ current ] [ road ] [ 1 ] , hold - Cost [ current ] [ road ] [ 0 ] , road ) )
                        if Ans [ road ] > c + Cost [ current ] [ road ] [ 1 ] :
                    Ans [ road ] = c + Cost [ current ] [ road ] [ 1 ]
                        if hold + Change [ current ] [ 0 ] <= A_m * N :
        if used [ current ] [ hold + Change [ current ] [ 0 ] ] > c + Change [ current ] [ 1 ] :
            heapq . heappush ( minHeap , ( c + Change [ current ] [ 1 ] , hold + Change [ current ] [ 0 ] , current ) )
                return Ans
    T = dijkstra ( N , 0 , Cost , Change )
for i in T [ 1 : ] :
print ( i )
    