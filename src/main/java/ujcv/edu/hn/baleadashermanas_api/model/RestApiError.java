/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ujcv.edu.hn.baleadashermanas_api.model;

/**
 *
 * @author Carlos
 */

public class RestApiError {
   public String httpStatus;
   public String errorMessage;
   public String errorDetails;
   public RestApiError(){
       super();
   }

   public String getErrorDetails() {
      return errorDetails;
   }
   public String getErrorMessage() {
      return errorMessage;
   }
   public String getHttpStatus() {
      return httpStatus;
   }
   public void setErrorDetails(String errorDetails) {
      this.errorDetails = errorDetails;
   }
   public void setErrorMessage(String errorMessage) {
      this.errorMessage = errorMessage;
   }
   public void setHttpStatus(String httpStatus) {
      this.httpStatus = httpStatus;
   }

}

