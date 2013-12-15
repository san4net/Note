package com.copypaste;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class FileWritePerfTest {
	private static final int ITERATIONS = 1;
	private static final double MEG = (Math.pow(1024, 2));
	private static final int RECORD_COUNT = 4000000;
	private static final String RECORD = "Help I am trapped in a fortune cookie factory\n";
	private static final int RECSIZE = RECORD.getBytes().length;

	static {
		System.out.println("sting size in byte" + RECSIZE);
	}

	public static void main(String[] args) throws Exception {
		 List<String> records = new ArrayList<String>(RECORD_COUNT);
		 int size = 0;
		 for (int i = 0; i < 6*RECORD_COUNT; i++) {
		 records.add(RECORD);
		 size += RECSIZE;
		 }
		 System.out.println(records.size() + " 'records'");
		 System.out.println(size / MEG + " MB");
		 System.out.println("Writing file ");

		  for (int i = 0; i < ITERATIONS; i++) {
		  System.out.println("\nIteration " + i);
		    File inFile = new File("C:/non_os/notes/writingtest/foo.txt");
			System.out.println("file size " + (inFile.length() / (1024 * 1024))
					+ "mb");
		  FileReader reader = new FileReader(inFile);
		  writeRaw(reader);
//		  writeRaw(records);
//		  writeBuffered(records, 8192); writeBuffered(records, (int) MEG);
//		  writeBuffered(records, 4 * (int) MEG); 
		  
		  writeMemoryMapped();
		  
		  }
	}

	private static void writeRaw(List<String> records) throws IOException {

		File file = File.createTempFile("foo", ".txt");
		try {
			FileWriter writer = new FileWriter(file);
			System.out.print("Writing raw... ");
			write(records, writer);
		} finally {
			// comment this out if you want to inspect the files afterward
			 file.delete();
		}
	}
   private static void writeRaw(FileReader reader) throws IOException{
	    File file = File.createTempFile("foo", ".txt");
		try {
			FileWriter writer = new FileWriter(file);
			System.out.print("Writing raw... ");
			write(reader, writer);
		} finally {
			// comment this out if you want to inspect the files afterward
			 file.delete();
		}
   }

	private static void writeMemoryMapped() throws IOException {
		File inFile = new File("C:/non_os/notes/writingtest/foo.txt");
		System.out.println("file size " + (inFile.length() / (1024 * 1024))
				+ "mb");
		System.out.print("Writing memory mapped..");
		FileChannel channel = new FileInputStream(inFile).getChannel();
		write(channel);
	}

	private static void writeBuffered(List<String> records, int bufSize)
			throws IOException {
		File file = File.createTempFile("foo", ".txt");
		try {
			FileWriter writer = new FileWriter(file);
			BufferedWriter bufferedWriter = new BufferedWriter(writer, bufSize);

			System.out.print("Writing buffered (buffer size: " + bufSize
					+ ")... ");
			write(records, bufferedWriter);
		} finally {
			// comment this out if you want to inspect the files afterward
			file.delete();
		}
	}

	private static void write(FileChannel channel) throws IOException {
		long start = System.currentTimeMillis();
		MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0,
				channel.size());

		FileChannel outChannel = new FileOutputStream(
				"C:/non_os/notes/writingtest/foo1.txt").getChannel();

		outChannel.write(buffer);

		outChannel.close();
		channel.close();
		long end = System.currentTimeMillis();
		System.out.println((end - start) / 1000f + " seconds");
	}

	private static void write(List<String> records, Writer writer) throws IOException {
		long start = System.currentTimeMillis();
		  for (String record : records) {
			  writer.write(record);
		  }
		 
		writer.flush();
		writer.close();
		long end = System.currentTimeMillis();
		System.out.println((end - start) / 1000f + " seconds");
	}
	
	private static void write(FileReader reader, Writer writer) throws IOException {
		long start = System.currentTimeMillis();
//		CharBuffer target  = CharBuffer.allocate(1024);
		char[] buffer = new char[1024];
		while(reader.read(buffer) >0){
			writer.write(buffer);
		}
		writer.flush();
		writer.close();
		reader.close();
		long end = System.currentTimeMillis();
		System.out.println((end - start) / 1000f + " seconds");
	}
}
