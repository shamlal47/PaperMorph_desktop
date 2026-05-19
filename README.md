# PaperMorph Desktop

![PaperMorph Desktop](https://img.shields.io/badge/Status-Active-success)
![Java](https://img.shields.io/badge/Java-21-orange.svg)
![Maven](https://img.shields.io/badge/Build-Maven-blue.svg)

PaperMorph Desktop is a simple, ad-free, and fast Image to PDF converter built with Java. It provides a clean, modern graphical user interface to easily select images and compile them into a single PDF document.

## 🚀 How it Works
PaperMorph provides an intuitive Desktop GUI where you can:
1. **Select Images:** Click the "Browse Images" button to select the images (JPG, PNG) you want to convert.
2. **Convert:** Click "Convert to PDF", choose a destination folder and filename.
3. **Process:** The app instantly compiles the images into a single PDF file, scaling them appropriately to fit standard page dimensions.
4. **Enjoy:** Everything happens locally on your machine—no internet connection required, ensuring complete privacy.

## 🛠 Tech Stack
- **Language:** Java 21
- **GUI Framework:** Java Swing
- **Look and Feel:** [FlatLaf](https://www.formdev.com/flatlaf/) (Modern Dark theme for Java Swing)
- **Core Processing:** [Apache PDFBox](https://pdfbox.apache.org/) (for robust PDF generation)
- **Build Tool:** Maven

## 📦 Installation & Setup

PaperMorph can be run universally via a fat JAR or built into a native installer for your platform.

### Prerequisites (For building from source)
- **Java Development Kit (JDK) 21** or higher.
- **Maven** installed and added to your PATH.

---

### 🐧 Linux

**Option 1: Build the native `.deb` installer**
We provide a handy packaging script that uses `jpackage` to build a native Debian package.
```bash
git clone https://github.com/shamlal47/PaperMorph_desktop.git
cd PaperMorph_desktop
chmod +x package.sh
./package.sh
```
Install the generated package:
```bash
sudo dpkg -i target/installer/papermorph_1.0.0-1_amd64.deb
```
You can then launch PaperMorph from your application menu!

**Option 2: Run directly from source**
```bash
chmod +x run.sh
./run.sh
```

---

### 🪟 Windows

Open Command Prompt or PowerShell in the cloned repository folder:

**Universal Executable JAR**
```cmd
:: Build the project into a fat JAR
mvn clean package -DskipTests

:: Run the application
java -jar target/image-to-pdf-1.0-SNAPSHOT-jar-with-dependencies.jar
```
*Note: To build a native Windows installer (`.exe` or `.msi`), you can run `jpackage --type exe` or `jpackage --type msi` directly if you have the WiX toolset installed.*

---

### 🍎 macOS

Open Terminal in the cloned repository folder:

**Universal Executable JAR**
```bash
# Build the project into a fat JAR
mvn clean package -DskipTests

# Run the application
java -jar target/image-to-pdf-1.0-SNAPSHOT-jar-with-dependencies.jar
```
*Note: To build a native macOS installer (`.dmg` or `.pkg`), you can use `jpackage --type dmg` from your JDK environment.*

## 🤝 Contributing
Contributions, issues, and feature requests are welcome! Feel free to check the issues page.

## 📄 License
This project is open-source. Please check the repository for license details.
