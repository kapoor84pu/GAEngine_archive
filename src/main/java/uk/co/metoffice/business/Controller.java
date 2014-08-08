package uk.co.metoffice.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.metoffice.beans.MetaData;
import uk.co.metoffice.beans.RequestParameter;
import uk.co.metoffice.beans.ResponseParameter;
import uk.co.metoffice.beans.xml.IngestionConfiguration;
import uk.co.metoffice.service.GCSPersistenceService;
import uk.co.metoffice.service.JPAPersistenceService;
import uk.co.metoffice.util.StreamProcessor;

import javax.mail.internet.MimeUtility;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * <p>[Pattern] to provide the following to {@link Object}:</p>
 * <ul>
 * <li></li>
 * </ul>
 * <p>Example:</p>
 * <pre>
 * </pre>
 *
 * @since 0.0.1
 *  
 */
public class Controller {
  private final static Logger logger = LoggerFactory.getLogger(Controller.class);
  JPAPersistenceService jpaPersistenceService = new JPAPersistenceService();
  GCSPersistenceService gcsPersistenceService = new GCSPersistenceService();

  /**
   * This method receives a request and extracts file from it and according
   * to file type send them to persistence service class.
   * @param request (includes a IngestionConfiguration file)
   * @return ResponseParameter object
   */
  public ResponseParameter ingestDocument(RequestParameter request){
    String fileKey;
    MetaData metaData;
    ResponseParameter responseParameter = null;
    IngestionConfiguration file = request.getIngestionConfiguration();
    ProductController productController = new ProductController();
    CSVDataController csvDataController = new CSVDataController();
    logger.info("invoking ingestDocument method");

    try{
      //receive blob data and metadata from Ingestion configuration XML file
      String encodedData = file.getIngestionEntries().getIngestionEntry().get(0).getBlobData();
      metaData = productController.generateMetadata(file);

      if(metaData.getFileType().equalsIgnoreCase("CSV")){
        //if file type is CSV decode it and sent to postOnly CSV
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(encodedData.getBytes());
        InputStream b64is = MimeUtility.decode(byteArrayInputStream, "base64");

        String str = StreamProcessor.getStringFromInputStream(b64is);
        responseParameter = csvDataController.persistCSVData(str, metaData.getClientId());
        responseParameter = new ResponseParameter.ResponseParameterBuilder().setFileKey(metaData.getFileType()).build();
      }
      else if(metaData.getFileType().equalsIgnoreCase("PDF")){
        fileKey = productController.getKey(metaData);

        // Persist metadata object for pdf/product listing
        metaData.setId(fileKey);
        jpaPersistenceService.persistMetadata(metaData);
        responseParameter = gcsPersistenceService.uploadDocumentStream(fileKey, encodedData);
      }
      else{
        logger.warn("Doesn't accept " + metaData.getFileType() + "file Format");
      }
    }catch (Exception e) {
      logger.error("Exception during ingesting file into Google store" , e);

    }
    return responseParameter;
  }
}
