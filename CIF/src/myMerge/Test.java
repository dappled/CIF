package myMerge;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.zip.GZIPInputStream;
  
public class Test { 
  
 /** 
  * Checks if an input stream is gzipped. 
  *  
  * @param in 
  * @return 
  */
 public static boolean isGZipped(InputStream in) { 
  if (!in.markSupported()) { 
   in = new BufferedInputStream(in); 
  } 
  in.mark(2); 
  int magic = 0; 
  try { 
   magic = in.read() & 0xff | ((in.read() << 8) & 0xff00); 
   in.reset(); 
  } catch (IOException e) { 
   e.printStackTrace(System.err); 
   return false; 
  } 
  return magic == GZIPInputStream.GZIP_MAGIC; 
 } 
  
 /** 
  * Checks if a file is gzipped. 
  *  
  * @param f 
  * @return 
  */
 public static boolean isGZipped(File f) { 
  int magic = 0; 
  try { 
   RandomAccessFile raf = new RandomAccessFile(f, "r"); 
   magic = raf.read() & 0xff | ((raf.read() << 8) & 0xff00); 
   raf.close(); 
  } catch (Throwable e) { 
   e.printStackTrace(System.err); 
  } 
  return magic == GZIPInputStream.GZIP_MAGIC; 
 } 
 
	public static byte[] readTicker(ByteBuffer _buf) {
		int start = _buf.position();
		_buf.mark();
		byte b;
		do {
			b = _buf.get();
		} while (b!='\n');
		int end = _buf.position();
		_buf.reset();
		byte[] ticker = new byte[end-start];
		_buf.get( ticker );
		_buf.position( end );
		return ticker;
	}
 public static void main(String[] args) throws IOException { 
  //File gzf = new File("C:\\Users\\Zhenghong Dong\\SkyDrive\\dappled's sky\\cs\\eclipse\\CIF\\src\\dBReader\\sampleTAQ\\quotes\\20070621\\IBM_quotes.binRQ"); 
  
  // Check if a file is gzipped. 
  //System.out.println(isGZipped(gzf)); 
  
  // Check if a input stream is gzipped. 
  //System.out.println(isGZipped(new FileInputStream(gzf))); 
/*	File tempFile = File.createTempFile("dummyFile", ".tmp", new File( "C:\\users\\Zhenghong Dong" ) );
	String name = tempFile.getAbsolutePath();
	RandomAccessFile file = new RandomAccessFile( tempFile, "rw" );
	FileChannel channel = file.getChannel();
	
	ByteBuffer buf = ByteBuffer.allocate( 8192 );
	// header
	buf.putInt( 30 );
	buf.putInt( 1 );
	// first record
	buf.putInt( 0 );
	buf.putInt( 15 );
	buf.putFloat( 1000 );
	buf.putInt( 3 );
	buf.putFloat( 1030 );
	byte[] a = "IBM".getBytes();
	buf.put( a );
	buf.putChar( '\n' );
	// second record
	buf.putInt( 1 );
	buf.putInt( 33 );
	buf.putFloat( 15 );
	buf.putInt( 43 );
	buf.putFloat( 17 );
	a = "MSFT".getBytes();
	buf.put( a );
	buf.putChar( '\n' );
	
	buf.flip();
	while (buf.hasRemaining()) channel.write( buf );
	buf.clear();
	
	channel.close();
	
	FileChannel readC = new RandomAccessFile(name, "r").getChannel();
	
	buf= ByteBuffer.allocate( 40 );
	readC.read( buf );
	long position = readC.position();
	buf.flip();
	// header
	System.out.println(buf.getInt());
	System.out.println(buf.getInt());
	// first record
	System.out.println(buf.getInt());
	System.out.println(buf.getInt());
	System.out.println(buf.getFloat());
	System.out.println(buf.getInt());
	System.out.println(buf.getFloat());
	System.out.println(Test.readTicker( buf ).toString());
	System.out.println(buf.remaining());
	System.out.println(readC.position( ));

	readC.position( position - buf.remaining());
	System.out.println(readC.position( ));
	buf.clear();
	readC.read( buf );
	buf.flip();
	
	// second record
	System.out.println(buf.getInt());
	System.out.println(buf.getInt());
	System.out.println(buf.getFloat());
	System.out.println(buf.getInt());
	System.out.println(buf.getFloat());
	System.out.println(Test.readTicker( buf ).toString());
	System.out.println(buf.remaining());
	System.out.println(readC.position( ));
	1250884400
  */
	 long a = 1182312000;
	 System.out.println((a<<1) + 57596000);
	 System.out.println(Long.MAX_VALUE);
 }
}