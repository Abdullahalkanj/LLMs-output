M. A. Weiss, Data Structures and Algorithm Analysis in Java, 3rd ed. Boston, MA: Pearson Education, Inc., 2012.  [Book]

public static void countingRadixSort( String [ ] arr, int stringLen )
{
    final int BUCKETS = 256;

    int N = arr.length;
    String [ ] buffer = new String [ N ];
    String [ ] in = arr;
    String [ ] out = buffer;

    for( int pos = stringLen - 1; pos >= 0; pos-- )
    {
        int [ ] count = new int [ BUCKETS + 1 ];
        for( int i = 0; i < N; i++ )
            count[ in[ i ].charAt( pos ) + 1 ]++;
        for( int b = 1; b <= BUCKETS; b++ )
            count[ b ] += count[ b - 1 ];
        for( int i = 0; i < N; i++ )
            out[ count[ in[ i ].charAt( pos ) ]++ ] = in[ i ];

        String [ ] tmp = in;
        in = out;
        out = tmp;
    }

    if( stringLen % 2 == 1 )
        for( int i = 0; i < arr.length; i++ )
            out[ i ] = in[ i ];
}