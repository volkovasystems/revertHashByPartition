package revertHashByPartition;

import static calculatePartition.calculatePartition.calculatePartition;

public class revertHashByPartition{

    public static void main( String... parameterList ){
        if( parameterList.length != 3 ){
            Exception exception = new Exception( "invalid parameter list" );
            System.err.print( exception.getMessage( ) );

            return;
        }

        String hash = parameterList[ 0 ];
        String dictionary = parameterList[ 1 ];

        int length = 1;
        try{
            length = parameterList[ 2 ];
        }catch( Exception exception ){
            System.err.print( exception.getMessage( ) );

            return;
        }
    }

    public static final String revertHashByPartition( String hash, String dictionary, int length ){

    }
}