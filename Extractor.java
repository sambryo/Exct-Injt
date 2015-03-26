import java.io.*;

public class Extractor {

    Runtime runtime; // this programs interface to the operating system. can be used to start a process
    Process proc;    // a process on the operating system.

    /* Contructor. Before everything else get the runtime. */
    public Extractor(){
        runtime = Runtime.getRuntime();
    }

    public static void main(String[] args) {
        if(args.length != 1){
            System.out.println("No arguments.");
            System.exit(1);
        }

        String fileName = args[0];

        Extractor ext = new Extractor();
        ext.resolveCompressionType(fileName);

    }

    /**
     * First check the compression type and call the correct method
     **/
    public void resolveCompressionType(String fileName){

        Process proc = null;

        // first make a new folder
         makeNewFolder(fileName);


        if(fileName.endsWith(".zip"))
            proc = extractZipFile(fileName);
        else if(fileName.endsWith(".tar"))
            proc = extractTarFile(fileName);
        else if(fileName.endsWith(".tar.gz"))
            proc = extractTarGZFile(fileName);
        else
            System.out.println("Unknown fileType");

        showMessage(proc);


    }


    /**
     * First it creates a folder. The folder is where the extracted documents will be put.
     * The name of the folder is the filename-ext 
     * For example: if the file is myfile.zip , the folder will be myfile.zip-ext
     */
    public void  makeNewFolder(String fileName){

        boolean bool = false;
        File f = null;
        try {
            f = new File("./"+fileName+"-ext");
           bool =  f.mkdir();
           System.out.println("FOLDER CREATED ? " + bool);
        }catch (Exception e) {
            System.out.println("Exception in extractZipFiles()");
            e.printStackTrace();
        }
    }

    /**
     * Exatrcts zip files. Very simple method.
     */
    public Process extractZipFile(String fileName){
        try {
            proc = runtime.exec("unzip" + " " + fileName+" -d " + fileName+"-ext/" );
        }catch (IOException e) {
            System.out.println("Exception in extractZipFiles()");
            e.printStackTrace();
        }
        return proc;
    }

    /**
     * Extracts tar files. very Simple method
     */
    public Process extractTarFile( String fileName){
        try {
            proc = runtime.exec("tar -xvf" + " "+ fileName+ " -C " + fileName+"-ext/");
        }catch (IOException e) {
            System.out.println("Exception in extractTarFile()");
            e.printStackTrace();
        }
        return proc;
    }

    /**
     * Extracts tar gz files. very Simple method
     */
    public Process extractTarGZFile( String fileName){
        try {
            proc = runtime.exec("tar -xzvf" + " "+ fileName + " -C "+ fileName+"-ext/");
        }catch (IOException e) {
            System.out.println("Exception in extractTarGZFile()");
            e.printStackTrace();
        }
        return proc;
    }

    /**
     * Prints out information to  the shell during extraction.
     */
    public void showMessage(Process proc) {

        if(proc != null){
            try {
                InputStream is = proc.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                String line;
                while((line = br.readLine()) != null){
                    System.out.println(line);
                }
            }catch(IOException e){
                System.out.println("Something went wrong in showMessage()");
                e.printStackTrace();
            }

        }else{
            System.out.println("The file type is not recognized.");
        }
    }

}
