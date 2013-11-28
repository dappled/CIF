package dBReader;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

public class Test_GZFileUtils extends junit.framework.TestCase {
	
	public void test1() throws IOException {

		// Create a temporary file
		
			File tempFile = File.createTempFile( "testDataInputStream", ".tmp", new File("C:\\users\\Zhenghong Dong"));
			tempFile.deleteOnExit();

		// Write a file of ints and floats
		
			DataOutputStream dos = GZFileUtils.getGZippedFileOutputStream( tempFile.getAbsolutePath() );
			for( int i = 0; i < 1000; i++ ) {
				dos.writeInt(i);
				dos.writeFloat( (float) ((float)i * 1000.0) );
			}
			dos.flush();
			dos.close();
			
		// Read the file back
			
			DataInputStream dis = GZFileUtils.getGZippedFileInputStream( tempFile.getAbsolutePath() );
			for( int i = 0; i < 1000; i++ ) {
				int j = dis.readInt();
				assertTrue( i == j );
				float f = dis.readFloat();
				assertTrue( 
					( f > ((float)( i * 1000) - 0.001 )) 
					&& 
					( f < ((float)( i * 1000) + 0.001 )) 
				);
			}
			dis.close();
			
		// Delete this file
			
			tempFile.delete();
			
	}
	
}
