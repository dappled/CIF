package summerSolution;

import java.io.File;
import java.io.FileWriter;
import java.util.LinkedList;

import utils1309.TextFileReader;

public class Test_TextFileReader extends junit.framework.TestCase {
	
	public void testReadLinesMethod() throws Exception {
		File file = File.createTempFile( "zz1", "zz2" );
		file.deleteOnExit();
		FileWriter fw = new FileWriter( file );
		fw.write( "Hello\n" );
		fw.write( "Goodbye\n" );
		fw.write( "The End" );
		fw.flush();
		fw.close();
		LinkedList<String> lines = TextFileReader.readLineLinkedList( file.getAbsolutePath() );
		assertTrue( lines.get( 0 ).compareTo( "Hello" ) == 0 );
		assertTrue( lines.get( 1 ).compareTo( "Goodbye" ) == 0 );
		assertTrue( lines.get( 2 ).compareTo( "The End" ) == 0 );
		file.delete();
	}

}
