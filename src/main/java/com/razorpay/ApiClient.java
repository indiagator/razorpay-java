package com.razorpay;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.HttpUrl;
import okhttp3.Response;

class ApiClient {

  String auth;

  private final String ENTITY = "entity";

  private final String COLLECTION = "collection";

  private final String ERROR = "error";

  private final String DESCRIPTION = "description";

  private final String STATUS_CODE = "code";

  private final int STATUS_OK = 200;

  private final int STATUS_MULTIPLE_CHOICE = 300;

  ApiClient(String auth) {
    this.auth = auth;
  }

  public <T extends Entity> T get(String path, JSONObject requestObject) throws RazorpayException {
    Response response = ApiUtils.getRequest(path, requestObject, auth);
    return processResponse(response);
  }

  public <T extends Entity> T post(String path, JSONObject requestObject) throws RazorpayException {
    Response response = ApiUtils.postRequest(path, requestObject, auth);
    return processResponse(response);
  }

  public <T extends Entity> T put(String path, JSONObject requestObject) throws RazorpayException {
    Response response = ApiUtils.putRequest(path, requestObject, auth);
    return processResponse(response);
  }

  public <T extends Entity> T patch(String path, JSONObject requestObject) throws RazorpayException {
    Response response = ApiUtils.patchRequest(path, requestObject, auth);
    return processResponse(response);
  }

  public <T extends Entity> T delete(String path, JSONObject requestObject) throws RazorpayException {
    Response response = ApiUtils.deleteRequest(path, requestObject, auth);
    return processDeleteResponse(response);
  }

  <T extends Entity> ArrayList<T> getCollection(String path, JSONObject requestObject)
          throws RazorpayException {
    Response response = ApiUtils.getRequest(path, requestObject, auth);
    return processCollectionResponse(response);
  }

  private <T extends Entity> T parseResponse(JSONObject jsonObject) throws RazorpayException {
    if (jsonObject.has(ENTITY)) {
      Class<T> cls = getClass(jsonObject.getString(ENTITY));
      try {
        return cls.getConstructor(JSONObject.class).newInstance(jsonObject);
      } catch (Exception e) {
        throw new RazorpayException("Unable to parse response because of " + e.getMessage());
      }
    }

    throw new RazorpayException("Unable to parse response");
  }

  private <T extends Entity> ArrayList<T> parseCollectionResponse(JSONArray jsonArray)
      throws RazorpayException {

   ArrayList<T> modelList = new ArrayList<T>();
    try {
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject jsonObj = jsonArray.getJSONObject(i);
        T t = parseResponse(jsonObj);
        modelList.add(t);
      }
      return modelList;
    } catch (RazorpayException e) {
      throw e;
    }
  }

  /*
   * this method will take http url as : https://api.razorpay.com/v1/invocies
   * and will return entity name with the help of @EntityNameURLMapping class
   */
  private String getEntityNameFromURL(HttpUrl url) {
    String param = url.pathSegments().get(1);
    return EntityNameURLMapping.getEntityName(param);
  }


  <T extends Entity> T processResponse(Response response) throws RazorpayException {
    if (response == null) {
      throw new RazorpayException("Invalid Response from server");
    }

    int statusCode = response.code();
    String responseBody = null;
    JSONObject responseJson = null;
    try {
      responseBody = response.body().string();
      responseJson = new JSONObject(responseBody);
    } catch (IOException e) {
      throw new RazorpayException(e.getMessage());
    }

    if (statusCode >= STATUS_OK && statusCode < STATUS_MULTIPLE_CHOICE) {
      populateEntityInResponse(responseJson, response);
      return parseResponse(responseJson);
    }

    throwException(statusCode, responseJson);
    return null;
  }

  private void populateEntityInResponse(JSONObject responseJson, Response response) {
      if(!responseJson.has(ENTITY)) {
        String entityName = getEntityNameFromURL(response.request().url());
        responseJson.put("entity",entityName);
      }else if(responseJson.get("entity").toString().equals("settlement.ondemand")){
        responseJson.put("entity","settlement");
      }
  }

  <T extends Entity> ArrayList<T> processCollectionResponse(Response response)
          throws RazorpayException {
    if (response == null) {
      throw new RazorpayException("Invalid Response from server");
    }

    int statusCode = response.code();
    String responseBody = null;
    JSONObject responseJson = null;

    try {
      responseBody = response.body().string();
      responseJson = new JSONObject(responseBody);
    } catch (IOException e) {
      throw new RazorpayException(e.getMessage());
    }

    String collectionName  = null;
    collectionName = responseJson.has("payment_links")?
            "payment_links": "items";

    JSONArray collection = responseJson.getJSONArray(collectionName);
    populateEntityInCollection(response, collection);

    if (statusCode >= STATUS_OK && statusCode < STATUS_MULTIPLE_CHOICE) {
      return parseCollectionResponse(collection);
    }

    throwException(statusCode, responseJson);
    return null;
  }

  private void populateEntityInCollection(Response response, JSONArray jsonArray) {
    for (int i = 0; i < jsonArray.length(); i++) {
      populateEntityInResponse(jsonArray.getJSONObject(i),response);
    }
  }


  private <T extends Entity> T processDeleteResponse(Response response) throws RazorpayException {
    if (response == null) {
      throw new RazorpayException("Invalid Response from server");
    }

    int statusCode = response.code();
    String responseBody = null;
    JSONObject responseJson = null;

    try {
      responseBody = response.body().string();
      responseJson = new JSONObject(responseBody);
    } catch (IOException e) {
      throw new RazorpayException(e.getMessage());
    }

    if (statusCode < STATUS_OK || statusCode >= STATUS_MULTIPLE_CHOICE) {
      throwException(statusCode, responseJson);
    }
    return parseResponse(responseJson);
  }

  private void throwException(int statusCode, JSONObject responseJson) throws RazorpayException {
    if (responseJson.has(ERROR)) {
      JSONObject errorResponse = responseJson.getJSONObject(ERROR);
      String code = errorResponse.getString(STATUS_CODE);
      String description = errorResponse.getString(DESCRIPTION);
      throw new RazorpayException(code + ":" + description);
    }
    throwServerException(statusCode, responseJson.toString());
  }

  private void throwServerException(int statusCode, String responseBody) throws RazorpayException {
    StringBuilder sb = new StringBuilder();
    sb.append("Status Code: ").append(statusCode).append("\n");
    sb.append("Server response: ").append(responseBody);
    throw new RazorpayException(sb.toString());
  }

  private Class getClass(String entity) {
    try {
      String entityClass = "com.razorpay." + WordUtils.capitalize(entity, '_').replaceAll("_", "");
      return Class.forName(entityClass);
    } catch (ClassNotFoundException e) {
      return null;
    }
  }
}