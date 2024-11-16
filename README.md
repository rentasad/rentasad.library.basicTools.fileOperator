# FileOperator Project

## Overview
The `FileOperator` project is a Java-based utility designed to perform various file operations, including reading, writing, deleting, copying, moving, and appending to files. It aims to provide a simple, intuitive API for managing file-related tasks in an efficient manner.

## Features
- **Read a File**: Reads the content of a specified file into a list of strings.
- **Write to a File**: Writes a list of strings to a specified file, overwriting any existing content.
- **Delete a File**: Deletes a specified file from the filesystem.
- **Move a File**: Moves a file from a source path to a destination path.
- **Copy a File**: Copies a file from a source path to a destination path.
- **Append to a File**: Appends new lines of text to an existing file.

## Requirements
- **Java**: Version 11 or later
- **JUnit**: For running unit tests (included in the project)
- **Mockito** (optional): For mocking dependencies in unit tests

## Setup Instructions
1. **Clone the repository**:
   ```sh
   git clone <repository_url>
   cd FileOperator
   ```

2. **Compile the project**:
   ```sh
   javac -d out src/com/example/FileOperator.java
   ```

3. **Run the application**:
   You can use the main class to interact with files.

4. **Run Tests**:
   To run unit tests using JUnit, execute the following command:
   ```sh
   java -cp .:out:lib/junit-5.7.0.jar org.junit.runner.JUnitCore com.example.FileOperatorTest
   ```

## Usage Examples

### Reading a File
```java
FileOperator fileOperator = new FileOperator();
List<String> lines = fileOperator.readFile("example.txt");
lines.forEach(System.out::println);
```

### Writing to a File
```java
List<String> content = List.of("Hello, World!", "This is a test file.");
fileOperator.writeFile("example.txt", content);
```

### Deleting a File
```java
fileOperator.deleteFile("example.txt");
```

### Moving a File
```java
fileOperator.moveFile("source.txt", "destination.txt");
```

### Copying a File
```java
fileOperator.copyFile("source.txt", "copy.txt");
```

## Running Unit Tests
The project includes a comprehensive set of unit tests for each operation of the `FileOperator` class.

To run the tests, you can use an IDE like IntelliJ or Eclipse, or use the command line as shown in the setup instructions.

## Contributing
Feel free to open issues or submit pull requests if you find any bugs or have ideas for new features.

## License
This project is licensed under the MIT License. See the LICENSE file for details.

