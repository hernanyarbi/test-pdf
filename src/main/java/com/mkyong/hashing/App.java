package com.mkyong.hashing;

import java.io.File;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
       // System.out.println( "Hello World!" );
        //System.out.println("ejecuto la clase de los pdf");
        // load pdf files to be mergeds
        File file1 = new File("D:\\pdf\\j1.pdf");
        File file2 = new File("D:\\pdf\\j2.pdf");
        File file3 = new File("D:\\pdf\\j3.pdf");
          
        // instantiatE PDFMergerUtility class
        PDFMergerUtility pdfMerger = new PDFMergerUtility();

        // set destination file path
        pdfMerger.setDestinationFileName("D:\\completo.pdf");

        // add all source files, to be merged, to pdfMerger
        pdfMerger.addSource(file1);
        pdfMerger.addSource(file2);
        pdfMerger.addSource(file3);

        // merge documents
        pdfMerger.mergeDocuments(null);

        System.out.println("PDF Documents merged to a single file");
    }
}
