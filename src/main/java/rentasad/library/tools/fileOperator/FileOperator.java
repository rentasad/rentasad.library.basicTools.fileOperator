package rentasad.library.tools.fileOperator;

import rentasad.library.basicTools.dateTool.DateTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.zip.CRC32;


/**
 * 
 * FileOperator.java
 * rentasad.library.tools.fileOperator
 * @creation 31.08.2021
 * @author mst
 * @version B2.2.3
 * updated 03.03.2022
 */
public class FileOperator
{

    /**
     * Retrieves a URL for a resource file given its path.
     *
     * @param path the path to the resource file
     * @return the URL pointing to the resource file, or null if the resource could not be found
     */
    public static URL getUrlFromResourceFile(final String path)
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return  classLoader.getResource(path);
    }

    /**
     * Retrieves an InputStream from a resource file given its path.
     *
     * @param path the path to the resource file
     * @return the InputStream holding the file content
     * @throws IOException if the file cannot be found or opened
     */
    public static InputStream getInputStreamFromResourceFile(final String path) throws IOException
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(path);
        // the stream holding the file content
        if (inputStream == null) {
            throw new FileNotFoundException("file not found! " + path);
        } else
        {

            return inputStream;
        }
    }

    /**
     * Reads the content of a specified file from the resources and returns it as a String.
     *
     * @param path the path to the resource file
     * @return the content of the file as a String
     * @throws IOException if an I/O error occurs while reading the file
     */
    public static String getFileValueFromResources(final String path) throws IOException
    {
        String fileValue = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(getInputStreamFromResourceFile(path)));
        StringBuffer contentOfFile = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null)
        {
            contentOfFile.append(line + "\r\n");
        }
        fileValue = contentOfFile.toString();

        br.close();
        return fileValue;
    }

    /**
     * Reads the content of a specified file and returns it as a String.
     *
     * @param fileName the name of the file to read
     * @return the content of the file as a String
     * @throws FileNotFoundException if the specified file does not exist
     * @throws IOException if an I/O error occurs while reading the file
     */
	
    public static String getFileValue(String fileName) throws FileNotFoundException, IOException
    {
        String fileValue = null;

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        StringBuffer contentOfFile = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null)
        {
            contentOfFile.append(line + "\r\n");
        }
        fileValue = contentOfFile.toString();

        br.close();
        return fileValue;
    }

    /**
     * Converts an array of file name strings to an array of File objects.
     *
     * @param fileNameArray an array of file name strings to be converted
     * @return an array of File objects corresponding to the provided file name strings
     */
    public static File[] getFilesFromFileNameArray(String[] fileNameArray)
    {
        ArrayList<File> fileList = new ArrayList<>();
        for (String fileName : fileNameArray)
        {
            fileList.add(new File(fileName));
        }
        return fileList.toArray(new File[0]);
    }
    /**
     * Moves a file to a specified directory path. If the file already exists at the target location,
     * an exception will be thrown. If the file cannot be deleted from the source after copying, an exception will be thrown.
     *
     * @param sourceFile the file to be moved
     * @param path the destination directory path where the file will be moved
     * @throws FileCouldNotDeleteException if the source file could not be deleted after copying
     * @throws FileAlreadyExistException if a file with the same name already exists at the destination path
     * @throws IOException if an I/O error occurs during the file operation
     */
    public static void moveFileToPath(File sourceFile, String path) throws FileCouldNotDeleteException, FileAlreadyExistException, IOException
    {
        if (sourceFile.exists() && sourceFile.isFile())
        {
            File targetFile = new File(path + File.separator + sourceFile.getName());
            if (!targetFile.exists())
            {
                if (FileOperator.copyFile(sourceFile, targetFile))
                {
                    if (!sourceFile.delete())
                    {
                        throw new FileCouldNotDeleteException(sourceFile.getAbsolutePath());
                    }
                    
                }else
                {
                    throw new IOException("Error with copy File from " + sourceFile.getAbsoluteFile() + " to " + targetFile.getAbsolutePath());
                }
            }else
            {
                throw new FileAlreadyExistException(targetFile.getAbsolutePath());
            }
            
        } else
        {
            throw new FileNotFoundException(sourceFile.getAbsolutePath());
        }
        
    }

    /**
     * Converts an array of file name strings to an array of File objects, each prepended with an additional path.
     *
     * @param fileNameArray an array of file name strings to be converted
     * @param additionalPath the path to be prepended to each file name
     * @return an array of File objects corresponding to the provided file name strings with additional path
     */
    public static File[] getFilesFromFileNameArray(String[] fileNameArray, String additionalPath)
    {
        ArrayList<File> fileList = new ArrayList<>();
        for (String fileName : fileNameArray)
        {
            String fileNameWithPath = additionalPath + File.separator + fileName;
            fileList.add(new File(fileNameWithPath));
        }
        return fileList.toArray(new File[0]);
    }

    /**
     * Retrieves an array of files from a specified directory that match the given file extension.
     *
     * @param folderPath the directory from which to retrieve the files
     * @param extension the file extension to filter by
     * @return an array of files with the specified extension, or null if the directory does not exist or is not a directory
     */
    public static File[] getFilesFromPathWithFileExtension(File folderPath, final String extension)
    {
 
        FilenameFilter filenameFilter = new FilenameFilter()
        {

            @Override
            public boolean accept(File dir, String name)
            {
                if (dir.exists())
                {
                    if (name.endsWith(extension));
                    return true;
                }
                return false;
            }
        };
        if (folderPath.exists() && folderPath.isDirectory())
        {
            return new File(folderPath.getAbsolutePath()).listFiles(filenameFilter);
        } else
        {
            return null;
        }

    }

    /**
     * Converts an ArrayList of file name strings to an array of File objects,
     * each prepended with an additional path.
     *
     * @param fileNamesToProvideArrayList an ArrayList of file name strings to be converted
     * @param tempDirPath the path to be prepended to each file name
     * @return an array of File objects corresponding to the provided file name strings with additional path
     */
    public static File[] getFilesFromFileNameArray(ArrayList<String> fileNamesToProvideArrayList, String tempDirPath)
    {

        return getFilesFromFileNameArray(fileNamesToProvideArrayList.toArray(new String[0]), tempDirPath);
    }

    /**
     * Extracts the name of the file from the specified path.
     *
     * @param pathName the full path of the file
     * @return the name of the file
     */
    public static String getFileNameFromPath(String pathName)
    {
        File file = new File(pathName);
        return file.getName();
    }

    /**
     * Copies a file from the specified source to the specified destination.
     *
     * @param in the source file to be copied
     * @param out the destination file where the content is to be copied
     * @return true if the file copy is successful and the destination file exists, false otherwise
     * @throws IOException if an I/O error occurs during the file operation
     */
    @SuppressWarnings("resource")
    public static boolean copyFile(File in, File out) throws IOException
    {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try
        {
            inChannel = new FileInputStream(in).getChannel();
            outChannel = new FileOutputStream(out).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e)
        {
            throw e;
        } finally
        {
            try
            {
                if (inChannel != null)
                    inChannel.close();
                if (outChannel != null)
                    outChannel.close();
                System.gc();
            } catch (IOException e)
            {
            }
        }
        return out.exists();
    }

    /**
     * Copies an array of files to the specified destination path.
     *
     * @param files the array of files to be copied
     * @param destinationPath the path where the files will be copied to
     * @return true if all files were copied successfully and exist at the destination, false otherwise
     * @throws IOException if an I/O error occurs during the file copy process or if a file does not exist
     */
    public static boolean copyFilesToDestinationPath(File[] files, String destinationPath) throws IOException
    {
        boolean success = true;
        for (File file : files)
        {

            if (file.exists() == false)
            {
                throw new FileNotFoundException(file.getAbsolutePath());
            } else
            {
                String fileName = file.getName();
                File destinationFile = new File(destinationPath + File.separator + fileName);
                copyFile(file, destinationFile);
                success = success && destinationFile.exists();
            }
        }
        return success;
    }

    /**
     * Creates a directory with a timestamp appended to the specified parent directory.
     *
     * @param parentPath the parent directory to which the timestamped directory will be added
     * @return the full path of the newly created timestamped directory, or null if the directory could not be created
     */
    public static String makeTimeStampDir(String parentPath)
    {

        String timeStampDirName = DateTools.getSQLTimeStampFromDate(GregorianCalendar.getInstance().getTime()).replace(':', '-');
        String dirName = parentPath + File.separator + timeStampDirName;
        File directoryFile = new File(dirName);
        boolean success = directoryFile.mkdir();
        if (success)
        {
            return dirName;
        } else
        {
            System.err.println(String.format("Verzeichnis %s konnte nicht erstellt werden.", dirName));
            return null;
        }
    }

    /**
     * Reads the content of a specified file and returns it as a String.
     *
     * @param file the path to the file to read
     * @return the content of the file as a String
     * @throws IOException if an I/O error occurs while reading the file
     */
    public static String readFile(String file) throws IOException
    {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        try
        {
            while ((line = reader.readLine()) != null)
            {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }

            return stringBuilder.toString();
        } finally
        {
            reader.close();
        }
    }

    /**
     * Checks whether a directory exists at the specified path.
     *
     * @param destinationPath the path of the directory to check
     * @return true if a directory exists at the specified path, false otherwise
     */
    public static boolean existDirectory(String destinationPath)
    {
        File file = new File(destinationPath);
        return (file.exists() && file.isDirectory());
    }
    
    /**
     * Retrieves the CRC (Cyclic Redundancy Check) value for a specified file.
     *
     * @param file the file from which to calculate the CRC value
     * @return the CRC value of the file, or null if the file does not exist or is not a valid file
     * @throws IOException if an I/O error occurs while reading the file
     */
    public static Long getCrcFromFile(File file) throws IOException
    {
        if (file.exists() && file.isFile())
        {
            FileInputStream fileInputStream = new FileInputStream(file);
            return getCRCFromInputStream(fileInputStream);
        }else
        {
            return null;
        }
    }
    
    

    /**
     * Retrieves the timestamp of the last modification for the specified file.
     *
     * @param fileName the name or path of the file
     * @return a Date object representing the last modification timestamp of the file
     */
    public static Date getTimeStampFromFile(String fileName)
    {
        File file = new File(fileName);
        return new Date(file.lastModified());
    }
    
    /**
     * Calculates and returns the CRC (Cyclic Redundancy Check) value for the given input stream.
     *
     * @param inputStream the InputStream from which to calculate the CRC value
     * @return the CRC value of the data read from the input stream
     * @throws IOException if an I/O error occurs while reading the input stream
     */

   public static Long getCRCFromInputStream(InputStream inputStream) throws IOException
   {

       CRC32 crc32 = new CRC32();
       int counter;
       while ((counter = inputStream.read()) != -1)
       {
           crc32.update(counter);
       }
       inputStream.close();
       return crc32.getValue();
   }

    /**
     * Retrieves the character encoding of a specified CSV file.
     *
     * @param csvFile the File object representing the CSV file
     * @return the name of the character encoding used by the file as a String
     * @throws IOException if an I/O error occurs while reading the file
     */
    public static String getEncodingFromFile(File csvFile) throws IOException
    {
        InputStreamReader r = new InputStreamReader(new FileInputStream(csvFile));
        String encoder = r.getEncoding();
        r.close();
        return encoder; 
    }

}
