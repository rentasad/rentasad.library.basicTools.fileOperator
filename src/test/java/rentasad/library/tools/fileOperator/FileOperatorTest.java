package rentasad.library.tools.fileOperator;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class FileOperatorTest
{

	@Test
	void getUrlFromResourceFile()
	{
		final String filePath = "testGetUrlFromResourceFile.txt";
		URL url = FileOperator.getUrlFromResourceFile(filePath);
		assertNotNull(url);

	}

	@Test
	void getFileValueFromResources() throws IOException
	{
		final String filePath = "testGetUrlFromResourceFile.txt";
		String value = FileOperator.getFileValueFromResources(filePath)
								   .trim();
		assertEquals("testGetUrlFromResourceFile.txt", value);

	}

	@Test
	void testCopyFileSuccess() throws IOException
	{
		// Arrange
		File sourceFile = new File("source.txt");
		File targetFile = new File("target.txt");

		// Ensure the source file exists
		Path path = Paths.get("source.txt");
		Files.write(path, "Sample content".getBytes(), StandardOpenOption.CREATE);

		// Act
		boolean result = FileOperator.copyFile(sourceFile, targetFile);

		// Assert
		assertTrue(result);
		assertTrue(targetFile.exists());
		assertEquals(Files.readString(path), Files.readString(Paths.get("target.txt")));

		// Clean up
		Files.deleteIfExists(sourceFile.toPath());
		Files.deleteIfExists(targetFile.toPath());
	}

	@Test
	void testCopyFileNonExistentSource()
	{
		// Arrange
		File sourceFile = new File("nonExistentSource.txt");
		File targetFile = new File("target.txt");

		// Act & Assert
		assertThrows(FileNotFoundException.class, () -> FileOperator.copyFile(sourceFile, targetFile));
	}

	@Test
	void testCopyFileIOException() throws IOException
	{
		// Arrange
		File sourceFile = new File("source.txt");
		File targetFile = new File("/unauthorized/target.txt");

		// Ensure the source file exists
		Files.write(Paths.get("source.txt"), "Sample content".getBytes(), StandardOpenOption.CREATE);

		// Act & Assert
		assertThrows(IOException.class, () -> FileOperator.copyFile(sourceFile, targetFile));

		// Clean up
		Files.deleteIfExists(sourceFile.toPath());
	}

	@Test
	void testGetCrcFromFile() throws IOException
	{
		// Arrange
		File file = new File("testCrcFile.txt");
		Files.write(Paths.get("testCrcFile.txt"), "Sample content".getBytes(), StandardOpenOption.CREATE);

		// Act
		Long crc = FileOperator.getCrcFromFile(file);

		// Assert
		assertNotNull(crc);

		// Clean up
		Files.deleteIfExists(file.toPath());
	}

	@Test
	void testGetCrcFromFileNonExistent() throws IOException
	{
		// Arrange
		File file = new File("nonExistentFile.txt");

		// Act
		Long crc = FileOperator.getCrcFromFile(file);

		// Assert
		assertNull(crc);
	}

	@Test
	void testGetCrcFromFileEmpty() throws IOException
	{
		// Arrange
		File file = new File("emptyFile.txt");
		Files.write(Paths.get("emptyFile.txt"), new byte[0], StandardOpenOption.CREATE);

		// Act
		Long crc = FileOperator.getCrcFromFile(file);

		// Assert
		assertEquals(0L, crc);

		// Clean up
		Files.deleteIfExists(file.toPath());
	}

	@Test
	void testGetTimeStampFromFileExists() throws IOException
	{
		// Arrange
		File file = new File("testTimestampFile.txt");
		Files.write(Paths.get("testTimestampFile.txt"), "Sample content".getBytes(), StandardOpenOption.CREATE);
		long beforeTimestamp = file.lastModified();

		// Act
		Date timeStamp = FileOperator.getTimeStampFromFile("testTimestampFile.txt");

		// Assert
		assertNotNull(timeStamp);
		assertEquals(new Date(beforeTimestamp), timeStamp);

		// Clean up
		Files.deleteIfExists(file.toPath());
	}

	@Test
	void testGetTimeStampFromFileNonExistent()
	{
		// Act
		Date timeStamp = FileOperator.getTimeStampFromFile("nonExistentFile.txt");

		// Assert
		assertNotNull(timeStamp);
		assertEquals(0L, timeStamp.getTime());
	}

	@Test
	void testGetTimeStampFromFileUpdates() throws IOException, InterruptedException
	{
		// Arrange
		File file = new File("testTimestampFile.txt");
		Path path = Paths.get("testTimestampFile.txt");
		Files.write(path, "Sample content".getBytes(), StandardOpenOption.CREATE);
		long initialTimestamp = file.lastModified();

		// Wait for a bit to ensure the file modification time changes
		Thread.sleep(1000);

		// Modify the file
		Files.write(path, "Updated content".getBytes(), StandardOpenOption.APPEND);

		// Act
		Date updatedTimestamp = FileOperator.getTimeStampFromFile("testTimestampFile.txt");

		// Assert
		assertNotNull(updatedTimestamp);
		assertTrue(updatedTimestamp.getTime() > initialTimestamp);

		// Clean up
		Files.deleteIfExists(file.toPath());
	}
}