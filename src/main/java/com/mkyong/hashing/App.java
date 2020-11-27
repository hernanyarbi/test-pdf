package com.mkyong.hashing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;

import java.io.InputStreamReader;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDPushButton;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws IOException {

        PDDocument pdfDocument;
        try {
            pdfDocument = PDDocument.load(new File("/home/work/Documents/Personal/INFAR/2011001768.pdf"));

            PDPage blankPage = new PDPage();
            pdfDocument.addPage(blankPage); // agregamos una pagina alpdf

            PDDocumentCatalog docCatalog = pdfDocument.getDocumentCatalog();
            PDAcroForm acroForm = docCatalog.getAcroForm();

            List<PDField> fields = acroForm.getFields();
            System.out.println(fields.size() + "\n" + "Se encontraron campos de nivel superior en el formulario.");

            // inspect field values
            for (PDField field : fields) {
                System.out.println(field + "|--" + field.getPartialName());
            }

            PDFMergerUtility pdfMerger = new PDFMergerUtility();

            InputStream inpdf = new URL("http://www.jossoft.com.ar/ARCHIVOS/DolarHistorico.pdf").openStream();
            //File file1 = new File("/home/work/Documents/Personal/INFAR/2011001768.pdf");
            //pdfMerger.addSource(file1);
            pdfMerger.addSource(inpdf);
            pdfMerger.setDestinationFileName("/home/work/Documents/Personal/INFAR/2011001768.pdf");
            // merge documents
            // pdfMerger.mergeDocuments(null);
            pdfMerger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
            System.out.println("PDF Documents merged to a single file");

            PDField field = acroForm.getField("firma");
            PDPage page = pdfDocument.getPage(0);

            PDPushButton pdPushButton = (PDPushButton) field;
            List<PDAnnotationWidget> widgets = pdPushButton.getWidgets();
            byte[] imageFile = readImageToByte();
            PDAnnotationWidget annotationWidget = widgets.get(0);
            PDImageXObject pdImage = PDImageXObject.createFromByteArray(pdfDocument,imageFile, field.getClass().getSimpleName());

            float imageScaleRatio = (float) pdImage.getHeight() / (float) pdImage.getWidth();
            PDRectangle buttonPosition = getFieldArea(pdPushButton);
            float height = buttonPosition.getHeight();
            float width = height / imageScaleRatio;
            float x = buttonPosition.getLowerLeftX();
            float y = buttonPosition.getLowerLeftY();

            PDAppearanceStream pdAppearanceStream = new PDAppearanceStream(pdfDocument);
            pdAppearanceStream.setResources(new PDResources());

            // creating the PDPageContentStream object
            PDPageContentStream contents = new PDPageContentStream(pdfDocument, pdAppearanceStream);

            // Drawing the image in the PDF document
            // contents.drawImage(pdImage, 70, 250);
            contents.drawImage(pdImage, x, y, width, height);

            pdAppearanceStream.setBBox(new PDRectangle(x, y, width, height));
            System.out.println("Image inserted");

            PDAppearanceDictionary pdAppearanceDictionary = annotationWidget.getAppearance();
            if (pdAppearanceDictionary == null) {
                pdAppearanceDictionary = new PDAppearanceDictionary();
                annotationWidget.setAppearance(pdAppearanceDictionary);
            }
            pdAppearanceDictionary.setNormalAppearance(pdAppearanceStream);

            // Closing the PDPageContentStream object
            contents.close();

            rellenarCampo(acroForm, "puntoVenta", "puntoventa");
            rellenarCampo(acroForm, "dir_puntoVenta", "dir-puntoventa");
            rellenarCampo(acroForm, "ciudad1", "ciudada1");
            rellenarCampo(acroForm, "pais", "pais");
            rellenarCampo(acroForm, "marca", "marca");
            rellenarCampo(acroForm, "referenciaEquipo", "referencia de equipo");
            rellenarCampo(acroForm, "imei1", "imei1");
            rellenarCampo(acroForm, "ciudadExp", "ciudadExp");
            rellenarCampo(acroForm, "referenciaEquipo1", "referenci de equipo");
            rellenarCampo(acroForm, "imei2", "imei2");

            rellenarCampo(acroForm, "ciudadExp", "ciudad expp");

            pdfDocument.save("/home/work/Documents/Personal/INFAR/2011001768.pdf");
            pdfDocument.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        /*
         * Map<String, String> data = new HashMap<String, String>();
         * data.put("ciudadExp", "jujuy"); data.put("firma", "D:\\pdf\\firma.png");
         *
         * populateAndCopy("", data); System.out.println("main");
         */

        /*
         * // load pdf files to be mergeds File file1 = new File("D:\\pdf\\j1.pdf");
         * File file2 = new File("D:\\pdf\\j2.pdf"); File file3 = new
         * File("D:\\pdf\\j3.pdf");
         *
         * // Aquí cargamos el documento pdf desde la ruta indicada //PDDocument
         * document = PDDocument.load(new File("./documento.pdf")); //PDDocument
         * document = PDDocument.load(file1);
         *
         * //PDPage page = new PDPage();//agregamos un pagina al documento
         * //document.addPage(page);
         *
         * // instantiatE PDFMergerUtility class PDFMergerUtility pdfMerger = new
         * PDFMergerUtility();
         *
         * // set destination file path
         * pdfMerger.setDestinationFileName("D:\\completo.pdf");
         *
         * // add all source files, to be merged, to pdfMerger
         * pdfMerger.addSource(file1); pdfMerger.addSource(file2);
         * pdfMerger.addSource(file3);
         *
         * // merge documents pdfMerger.mergeDocuments(null);
         *
         * System.out.println("PDF Documents merged to a single file");
         *
         *
         *
         *
         * try { PDDocument pDDocument = PDDocument.load(new
         * File("D:\\pdf\\2011001768.pdf")); PDAcroForm acroForm =
         * pDDocument.getDocumentCatalog().getAcroForm(); for (Map.Entry<String, String>
         * item : data.entrySet()) { String key = item.getKey(); PDField field =
         * acroForm.getField(key); if (field != null) { if (field instanceof
         * PDTextField) { field.setValue(item.getValue()); } else if (field instanceof
         * PDPushButton) { try { PDPushButton pdPushButton = (PDPushButton) field;
         * List<PDAnnotationWidget> widgets = pdPushButton.getWidgets(); if (widgets !=
         * null && widgets.size() > 0) { PDAnnotationWidget annotationWidget =
         * widgets.get(0); String filePath = item.getValue(); byte[] imageFile =
         * readImageToByte(filePath); if (imageFile != null) { PDImageXObject
         * pdImageXObject = PDImageXObject.createFromByteArray(pDDocument, imageFile,
         * field.getClass().getSimpleName()); float imageScaleRatio = (float)
         * pdImageXObject.getHeight() / (float) pdImageXObject.getWidth(); PDRectangle
         * buttonPosition = getFieldArea(pdPushButton); float height =
         * buttonPosition.getHeight(); float width = height / imageScaleRatio; float x =
         * buttonPosition.getLowerLeftX(); float y = buttonPosition.getLowerLeftY();
         * PDAppearanceStream pdAppearanceStream = new PDAppearanceStream(pDDocument);
         * pdAppearanceStream.setResources(new PDResources()); try (PDPageContentStream
         * pdPageContentStream = new PDPageContentStream(pDDocument,
         * pdAppearanceStream)) { pdPageContentStream.drawImage(pdImageXObject, x, y,
         * width, height); } pdAppearanceStream.setBBox(new PDRectangle(x, y, width,
         * height)); PDAppearanceDictionary pdAppearanceDictionary =
         * annotationWidget.getAppearance(); if (pdAppearanceDictionary == null) {
         * pdAppearanceDictionary = new PDAppearanceDictionary();
         * annotationWidget.setAppearance(pdAppearanceDictionary); }
         * pdAppearanceDictionary.setNormalAppearance(pdAppearanceStream); } else {
         * log.error("Valor " + filePath + " no corresponde a una imagen."); } } else {
         * log.error("Missconfiguration of placeholder: '" + key +
         * "' - no widgets(actions) found"); } } catch (Exception e) {
         * log.error("Terminado con error", e); } } else {
         * log.error("Unexpected form field type found with placeholder name: '" + key +
         * "'"); } } else { log.debug("No field found with name:" + key); } } try
         * (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
         * pDDocument.save(byteArrayOutputStream); try (InputStream inputStream = new
         * ByteArrayInputStream(byteArrayOutputStream.toByteArray())) { return
         * inputStream; } } } catch (URISyntaxException e) {
         * log.error("Terminado con error", e); return null; }
         */
    }

    public static byte[] readImageToByte() {
        byte[] imagen = null;

        try {
            InputStream in = new URL("https://sm.mashable.com/mashable_in/seo/default/8-years-of-the-avengers-how-joss-whedons-risk-became-crucial_5z9w.jpg").openStream();
            //InputStream in = new FileInputStream(new File("D:\\pdf\\firma.png"));
            imagen = IOUtils.toByteArray(in);
        } catch (IOException e) {
            System.out.println(e);
        }

        return imagen;
    }

    private static PDRectangle getFieldArea(PDField field) {
        COSDictionary fieldDict = field.getCOSObject();
        COSArray fieldAreaArray = (COSArray) fieldDict.getDictionaryObject(COSName.RECT);
        return new PDRectangle(fieldAreaArray);
    }

    private static void rellenarCampo(PDAcroForm acroForm, String nombreCampo, String valor) throws IOException {
        PDField field = acroForm.getField(nombreCampo);
        if (field != null) {
            field.setValue(valor);
        } else {
            System.err.println("No se ha encontrado el campo " + nombreCampo + "!");
        }
    }
    /*
     * public static InputStream populateAndCopy(String originalPdf, Map<String,
     * String> data) throws IOException { try {// crea el pdf q viene desde alfresco
     * PDDocument pDDocument = PDDocument.load(new File("D:\\pdf\\2011001768.pdf"));
     * PDAcroForm acroForm = pDDocument.getDocumentCatalog().getAcroForm();
     *
     * List<PDField> fields = acroForm.getFields(); System.out.println(fields.size()
     * + "\n" + "Se encontraron campos de nivel superior en el formulario.");
     *
     * // inspect field values for (PDField field : fields) {
     * System.out.println(field + "|--" + field.getPartialName()); }
     * rellenarCampo(acroForm, "ciudadExp", "jujuy-ledesma");
     *
     * for (Map.Entry<String, String> item : data.entrySet()) { String key =
     * item.getKey(); // System.out.println("key" + key); PDField field =
     * acroForm.getField(key); if (field != null) { // System.out.println(field); if
     * (field instanceof PDTextField) { field.setValue(item.getValue()); } else if
     * (field instanceof PDPushButton) { try { PDPushButton pdPushButton =
     * (PDPushButton) field; List<PDAnnotationWidget> widgets =
     * pdPushButton.getWidgets(); if (widgets != null && widgets.size() > 0) {
     * PDAnnotationWidget annotationWidget = widgets.get(0); String filePath =
     * item.getValue(); byte[] imageFile = readImageToByte(filePath); if (imageFile
     * != null) { PDImageXObject pdImageXObject =
     * PDImageXObject.createFromByteArray(pDDocument, imageFile,
     * field.getClass().getSimpleName()); float imageScaleRatio = (float)
     * pdImageXObject.getHeight() / (float) pdImageXObject.getWidth(); PDRectangle
     * buttonPosition = getFieldArea(pdPushButton); float height =
     * buttonPosition.getHeight(); float width = height / imageScaleRatio; float x =
     * buttonPosition.getLowerLeftX(); float y = buttonPosition.getLowerLeftY();
     * PDAppearanceStream pdAppearanceStream = new PDAppearanceStream(pDDocument);
     * pdAppearanceStream.setResources(new PDResources()); try { PDPageContentStream
     * pdPageContentStream = new PDPageContentStream(pDDocument,
     * pdAppearanceStream);
     *
     * pdPageContentStream.drawImage(pdImageXObject, x, y, width, height); } catch
     * (Error e) { System.out.println(e); } pdAppearanceStream.setBBox(new
     * PDRectangle(x, y, width, height)); PDAppearanceDictionary
     * pdAppearanceDictionary = annotationWidget.getAppearance(); if
     * (pdAppearanceDictionary == null) { pdAppearanceDictionary = new
     * PDAppearanceDictionary();
     * annotationWidget.setAppearance(pdAppearanceDictionary); }
     * pdAppearanceDictionary.setNormalAppearance(pdAppearanceStream); } else {
     * System.out.println("Valor " + filePath + " no corresponde a una imagen."); }
     * } else { System.out.println( "Missconfiguration of placeholder: '" + key +
     * "' - no widgets(actions) found"); } } catch (Exception e) {
     * System.out.println("Terminado con error" + e); } } else { System.out.
     * println("Unexpected form field type found with placeholder name: '" + key +
     * "'"); } } else { System.out.println("No field found with name:" + key); } }
     *
     * try { ByteArrayOutputStream byteArrayOutputStream = new
     * ByteArrayOutputStream(); pDDocument.save(byteArrayOutputStream); try {
     * InputStream inputStream = new
     * ByteArrayInputStream(byteArrayOutputStream.toByteArray()); return
     * inputStream; } catch (Exception e) { // TODO: handle exception } } catch
     * (Exception e) { // TODO: handle exception }
     *
     * } catch (Error e) { System.out.println("Terminado con error" + e); return
     * null; } return null;
     *
     * }
     */

}