package revertHashByPartition;

import java.math.RoundingMode;
import java.math.BigDecimal;

import static calculatePartition.calculatePartition.calculatePartition;
import static revertHashAtRange.revertHashAtRange.revertHashAtRange;
import static convertToSequenceIndex.convertToSequenceIndex.convertToSequenceIndex;

/*:
	@module-license:
		The MIT License (MIT)

		Copyright (c) 2014 Richeve Siodina Bebedor

		Permission is hereby granted, free of charge, to any person obtaining a copy
		of this software and associated documentation files (the "Software"), to deal
		in the Software without restriction, including without limitation the rights
		to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
		copies of the Software, and to permit persons to whom the Software is
		furnished to do so, subject to the following conditions:

		The above copyright notice and this permission notice shall be included in all
		copies or substantial portions of the Software.

		THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
		IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
		FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
		AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
		LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
		OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
		SOFTWARE.
	@end-module-license

	@module-configuration:
		{
			"packageName": "revertHashByPartition",
			"fileName": "revertHashByPartition.java",
			"moduleName": "revertHashByPartition",
			"className": "revertHashByPartition",
			"staticPath": "revertHashByPartition.revertHashByPartition.revertHashByPartition",
			"authorName": "Richeve S. Bebedor",
			"authorEMail": "richeve.bebedor@gmail.com",
			"repository": "git@github.com:volkovasystems/revertHashByPartition.git",
			"isCommand": true
		}
	@end-module-configuration

	@command-configuration:
		[
			{
				"hash:required": "string"
			},
			{
				"dictionary:required": "string"
			},
			{
				"length:required": "number"
			},
			{
				"rootFactor:required": "number"
			},
			{
				"index:optional": "number"
			},
			{
				"size:optional": "number",
				"@dependent-to-parameter": "index"
			},
			{
				"separator:optional": "string"
			}
		]
	@end-command-configuration
*/
public class revertHashByPartition{
	private static final int DEFAULT_LENGTH = 1;
	private static final String EMPTY_STRING = "";
	private static final String DEFAULT_ROOT_FACTOR = "2";
	private static final String DEFAULT_STARTING_INDEX = "0";
	private static final String DEFAULT_PARTITION_SIZE = "0";

	public static void main( String... parameterList ){
		int parameterListLength = parameterList.length;
		if( parameterListLength == 0 ||
			parameterListLength > 7 )
		{
			Exception exception = new Exception( "invalid parameter list" );
			System.err.print( exception.getMessage( ) );

			return;
		}

		String hash = parameterList[ 0 ];
		String dictionary = parameterList[ 1 ];
		
		int length = DEFAULT_LENGTH;
		try{
			length = Integer.parseInt( parameterList[ 2 ] );
		}catch( Exception exception ){
			System.err.print( exception.getMessage( ) );

			return;
		}

		String rootFactor = DEFAULT_ROOT_FACTOR;
		if( parameterListLength >= 4 ){
			rootFactor = parameterList[ 3 ];
		}

		String separator = EMPTY_STRING;

		String index = DEFAULT_STARTING_INDEX;
		if( parameterListLength >= 5 && 
			parameterList[ 4 ].matches( "\\d+" ) )
		{
			index = parameterList[ 4 ];

		}else if( parameterListLength == 5 ){
			separator = parameterList[ 4 ];	
		}

		String size = DEFAULT_PARTITION_SIZE;
		if( parameterListLength >= 6 &&
			parameterList[ 5 ].matches( "\\d+" ) )
		{
			size = parameterList[ 5 ];

		}else if( parameterListLength == 6 ){
			separator = parameterList[ 5 ];
		}
		
		if( parameterListLength == 7 ){
			separator = parameterList[ 6 ];
		}

		try{
			revertHashByPartition( hash, dictionary, length, rootFactor, index, size, separator );	

		}catch( Exception exception ){
			System.err.print( exception.getMessage( ) );
		}
	}

	public static final void revertHashByPartition( String hash, String dictionary, int length, String rootFactor, String index, String size, String separator )
		throws Exception
	{
		System.out.println( "Hash: " + hash + "\nDictionary: " + dictionary + "\nLength: " + length + "\nRoot factor: " + rootFactor + "\nIndex: " + index + "\nSize: " + size + "\nSeparator: " + separator );
		//: Split the dictionary using the separator to get the dictionary list which is badly needed.
		String[ ] dictionaryList = dictionary.split( separator );
		int dictionaryListLength = dictionaryList.length;

		//: Based from the length, get the ending sequence by repeatedly appending the last character in the dictionary.
		String endingSequence = ( new String( new char[ length ] ) ).replace( "\0", dictionaryList[ dictionaryListLength - 1 ] ); 
		System.out.println( "endingSequence: " + endingSequence );

		//: Initial get the total sequence count.
		BigDecimal totalSequenceCount = new BigDecimal( convertToSequenceIndex( endingSequence, dictionary, separator ).toString( ) );
		System.out.println( "totalSequenceCount: " + totalSequenceCount.toString( ) );

		//: Try to check if there is a given starting index.
		BigDecimal startingIndex = BigDecimal.ONE;
		if( !index.equals( DEFAULT_STARTING_INDEX ) ){
			startingIndex = new BigDecimal( index );
		}

		//: If the starting index is greater than the total sequence count stop the execution.
		if( startingIndex.compareTo( totalSequenceCount ) > 0 ){
			Exception exception = new Exception( "starting index is greater thant the total sequence count" );
			System.err.print( exception.getMessage( ) );			

			return;
		}

		//: Calculate the partition count, size and the last size based on the total sequence count.
		BigDecimal partitionCount = calculatePartition( totalSequenceCount.toString( ), rootFactor );
		
		BigDecimal partitionSize = totalSequenceCount.divide( partitionCount, 0, RoundingMode.FLOOR );
		
		BigDecimal lastPartitionSize = totalSequenceCount.subtract( partitionCount.subtract( BigDecimal.ONE ).multiply( partitionSize ) );
		
		//: If there is a given size, override the calculated partition count, size and last size based on the given partition size.
		if( !size.equals( DEFAULT_PARTITION_SIZE ) ){			
			partitionCount = calculatePartition( size, rootFactor );

			partitionSize = ( new BigDecimal( size ) ).divide( partitionCount, 0, RoundingMode.FLOOR );

			lastPartitionSize = partitionSize.subtract( partitionCount.subtract( BigDecimal.ONE ).multiply( partitionSize ) );
		}
		
		System.out.println( "partitionCount: " + partitionCount.toString( ) );
		System.out.println( "partitionSize: " + partitionSize.toString( ) );
		System.out.println( "lastPartitionSize: " + lastPartitionSize.toString( ) );
		

		return;

	}
}