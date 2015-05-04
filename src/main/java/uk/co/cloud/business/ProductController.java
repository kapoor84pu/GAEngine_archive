package uk.co.cloud.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.cloud.beans.RequestParameter;
import uk.co.cloud.service.JPAPersistenceService;
import uk.co.cloud.beans.MetaData;
import uk.co.cloud.beans.ResponseParameter;
import uk.co.cloud.beans.xml.IngestionConfiguration;
import uk.co.cloud.service.GCSPersistenceService;
import uk.co.cloud.util.AppHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeUtility;
import javax.xml.bind.JAXB;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

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
public class ProductController {
  private final static Logger logger = LoggerFactory.getLogger(ProductController.class);
  JPAPersistenceService jpaPersistenceService = new JPAPersistenceService();
  GCSPersistenceService gcsPersistenceService = new GCSPersistenceService();

  /**
   * Take fromDate, toDate and clientId, and give back a list of products/pdf between two dates.
   * @param request (fromDate, toDate and clientId)
   * @return ResponseParameter
   */
  public ResponseParameter getHistoricalProduct(RequestParameter request){
    List<MetaData> list;
    ResponseParameter responseParameter = new ResponseParameter();

    try {
      //TODO use Date util class instead of AppHelper
      Date dateFrom = AppHelper.convertStringIntoDate(request.getFromDate());
      Date dateTo = AppHelper.convertStringIntoDate(request.getToDate());

      String clientId = request.getClientId();

      logger.debug("printing fromDate and toDate after conversion" + dateFrom + dateTo + clientId);

      list = jpaPersistenceService.getProductBetweenDates(dateFrom, dateTo, clientId);

      logger.debug("list of historical products is" + list.toString());
      responseParameter = new ResponseParameter.ResponseParameterBuilder().setMetaDataList(list).build();

    } catch (Exception ex) {
      logger.error("error in getHistoricalProduct class", ex);
    }
    return responseParameter;
  }

  /**
   * take the request with set file name and forward to GCSPersistenceService
   * to read the file in bucket
   * @param request
   * @return stream of pdf file set in ResponseParameter object.
   */
  public ResponseParameter getDocumentStream(RequestParameter request){
    return gcsPersistenceService.getDocumentStream(request);
  }




  /**
   * convert XML file into java objects
   * @param file
   * @return MetaData objects
   * @throws javax.mail.MessagingException
   */
    MetaData generateMetadata(IngestionConfiguration file)
    throws MessagingException {

    String encodedMetaData = file.getIngestionEntries().getIngestionEntry().get(0).getMetaData();
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(encodedMetaData.getBytes());
    InputStream b64is = MimeUtility.decode(byteArrayInputStream, "base64");

    //converting byte stream into metadata object
    MetaData metaData = JAXB.unmarshal(b64is, MetaData.class);
    logger.debug("MataData XML" + metaData);

    return metaData;
  }




  /**
   * Create a unique key for product/pdf metadata so that later can retrieve data from bucket
   * using this key.
   * @param metaData (MetaData object)
   * @return String (key for mataData in google store)
   */
    String getKey(MetaData metaData){
    MetaData metaData1 = new MetaData();
    String key;

    String clientId = metaData.getClientId();
    String fileType = metaData.getFileType();
    String category = metaData.getCategory();

    Date date = metaData.getValidityDate();

    String  validDate = AppHelper.convertDateIntoString(date);

    key = metaData1.createCompositeKey(clientId, category, validDate, fileType);
    logger.info("The key generated is " + key);
    return key;
  }



}
