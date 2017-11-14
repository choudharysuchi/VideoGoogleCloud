package com.video.main;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.List;

public class UserDefinedFileAttributeHelper {
	
	public static void main(String args[]) throws Exception {
		Path filePath = Paths.get("C://Users/Joon/Downloads/Underwater_Waterfall.mp4");
		UserDefinedFileAttributeView view = Files
		    .getFileAttributeView(filePath, UserDefinedFileAttributeView.class);
		
		// The file attribute
	    final String name1 = "tag1";
	    final String name2 = "tag2";
	    final String value1 = "Custom Value 1";
	    final String value2 = "Custom Value 2";
	    
//		saveFileAttributes(view, name1, value1);
//		saveFileAttributes(view, name2, value2);
	    getBasicFileAttributes(filePath);
		getUserFileAttributes(view);
	}
	
	private static void getBasicFileAttributes(Path filePath) throws Exception {
		BasicFileAttributes attr = Files.readAttributes(filePath, BasicFileAttributes.class);

		System.out.println("creationTime: " + attr.creationTime());
		System.out.println("lastAccessTime: " + attr.lastAccessTime());
		System.out.println("lastModifiedTime: " + attr.lastModifiedTime());

		System.out.println("isDirectory: " + attr.isDirectory());
		System.out.println("isOther: " + attr.isOther());
		System.out.println("isRegularFile: " + attr.isRegularFile());
		System.out.println("isSymbolicLink: " + attr.isSymbolicLink());
		System.out.println("size: " + attr.size());
	}

	public static void saveUserFileAttributes(UserDefinedFileAttributeView view, final String name, final String value)
			 throws Exception{

	    // Write the properties
	    final byte[] bytes = value.getBytes("UTF-8");
	    final ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
	    writeBuffer.put(bytes);
	    writeBuffer.flip();
	    
	    view.write(name, writeBuffer);
		
	}

	public static void getUserFileAttributes(UserDefinedFileAttributeView view)
			 throws Exception{
		
		List<String> attributeList = view.list();
		for (String name : attributeList) {
	    
		    // Read the property
		    final ByteBuffer readBuffer = ByteBuffer.allocate(view.size(name));
		    view.read(name, readBuffer);
		    readBuffer.flip();
		    final String valueFromAttributes = new String(readBuffer.array(), "UTF-8");
		    System.out.println("File Attribute " + name + ": " + valueFromAttributes);
		}

	}

}
