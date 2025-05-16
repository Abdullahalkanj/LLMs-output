# Problem ID: p02680_s289730333
# Buggy version
g = lambda : map ( int , input ( ) . split ( ) )
N , M = g ( )
sortx = [ ]
sorty = [ ]
y_line = [ ]
x_line = [ ]
for _ in [ 0 ] * N :
a , b , c = g ( )
    sortx . append ( a )
    sortx . append ( b )
    sorty . append ( c )
    y_line . append ( ( a , b , c ) )
    for _ in [ 0 ] * M :
d , e , f = g ( )
    sortx . append ( d )
    sorty . append ( e )
    sorty . append ( f )
    x_line . append ( ( d , e , f ) )
    from bisect import bisect_left as bl
sortx = sorted ( list ( set ( sortx ) ) )
sorty = sorted ( list ( set ( sorty ) ) )
zip_x_line = [ ]
zip_y_line = [ ]
for a , b , c in y_line :
a = bl ( sortx , a )
    b = bl ( sortx , b )
    c = bl ( sorty , c )
    zip_y_line . append ( ( a , b , c ) )
    for d , e , f in x_line :
d = bl ( sortx , d )
    e = bl ( sorty , e )
    f = bl ( sorty , f )
    zip_x_line . append ( ( d , e , f ) )
    W = len ( sortx ) * 2 - 1
H = len ( sorty ) * 2 - 1
G = [ 1 ] * W * H + [ 0 ] * ( W + 1 )
def make_graph ( i , j , s = 0 ) :
G [ i + j * W ] = s
    for a , b , c in zip_y_line :
for i in range ( 2 * a , 2 * b + 1 ) :
    make_graph ( i , 2 * c )
        for d , e , f in zip_x_line :
for j in range ( 2 * e , 2 * f + 1 ) :
    make_graph ( 2 * d , j )
        def get_score ( i , j ) :
if i <= 0 or j <= 0 or i >= W or j >= H :
    return float ( ' inf ' )
        elif i % 2 and j % 2 :
    dx = sortx [ ( i + 1 ) // 2 ] - sortx [ ( i - 1 ) // 2 ]
        dy = sorty [ ( j + 1 ) // 2 ] - sorty [ ( j - 1 ) // 2 ]
        return dx * dy
        else :
    return 0
        dic = [ ( - 1 , 0 ) , ( 1 , 0 ) , ( 0 , 1 ) , ( 0 , - 1 ) ]
def search ( i , j ) :
res = [ ]
    for dx , dy in dic :
    if G [ ( i + dx ) + ( j + dy ) * W ] == 1 :
        res . append ( ( i + dx , j + dy ) )
            return res
    stack = [ ( 2 * bl ( sortx , 0 ) - 1 , 2 * bl ( sorty , 0 ) - 1 ) ]
res = 0
while stack :
x , y = stack . pop ( )
    if G [ x + y * W ] != 1 :
    continue
        res += get_score ( x , y )
    stack += search ( x , y )
    make_graph ( x , y )
    print ( res if res < float ( ' inf ' ) else ' INF ' )
