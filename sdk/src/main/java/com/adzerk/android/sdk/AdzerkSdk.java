package com.adzerk.android.sdk;

import android.support.annotation.Nullable;
import android.util.Log;

import com.adzerk.android.sdk.rest.ContentData;
import com.adzerk.android.sdk.rest.NativeAdService;
import com.adzerk.android.sdk.rest.Request;
import com.adzerk.android.sdk.rest.Response;
import com.adzerk.android.sdk.rest.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.RestAdapter;
import retrofit.RestAdapter.Builder;
import retrofit.RetrofitError;
import retrofit.client.Client;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedString;

/**
 * The Adzerk SDK provides the API for requesting native ads for you app.
 * <p>
 * To request Native Ads, you create and submit an ad Request using the SDK. Adzerk's ad engine will return decision
 * data and creative contents that can be used to serve ads in your application.
 * <p>
 * <pre>
 * {@code
 * // Get instance of the SDK
 * AdzerkSdk sdk = AdzerkSdk.getInstance();
 *
 * // Build the Request
 * Request request = new Request.Builder()
 *     .addPlacement(new Placement(name, networkId, siteId, adTypes))
 *     .build();
 *
 * // Issue the Request
 * sdk.request(request, listener);
 * }
 * </pre>
 * @see com.adzerk.android.sdk.rest.Request.Builder
 */
public class AdzerkSdk {
    static final String TAG = AdzerkSdk.class.getSimpleName();

    static final String ADZERK_ENDPOINT = "https://engine.adzerk.net";

    static AdzerkSdk instance;

    NativeAdService service;

    Client client;

    /**
     * Listener for the Response to an ad Request
     */
    public interface ResponseListener<T> {
        //TODO: Fine for a starting place, but we should use generic args so that we aren't
        //TODO: leaking retrofit abstractions through the sdk.
        public void success(@Nullable T response);
        public void error(RetrofitError error);
    }

    /**
     * Returns the SDK instance for making Adzerk API calls.
     *
     * @return sdk instance
     */
    public static AdzerkSdk getInstance() {
        if (instance == null) {
            instance = new AdzerkSdk();
        }

        return instance;
    }

    /**
     * Injection point for tests only. Not intended for public consumption.
     *
     * @param nativeAds service api
     * @return sdk instance
     */
    public static AdzerkSdk createInstance(NativeAdService nativeAds) {
        return new AdzerkSdk(nativeAds, null);
    }


    /**
     * Injection point for tests only. Not intended for public consumption.
     *
     * @param client - Inject http client
     * @return sdk instance
     */
    public static AdzerkSdk createInstance(Client client) {
        return new AdzerkSdk(null, client);
    }

    private AdzerkSdk() {
        service = getNativeAdsService();
    }

    private AdzerkSdk(NativeAdService service, Client client) {
        this.service = service;
        this.client = client;
    }

    /**
     * Send an ad request to the Native Ads API.
     *
     * @param request ad Request specifying one or more Placements
     * @param listener Can be null, but caller will never get notifications.
     */
    public void request(Request request, @Nullable final ResponseListener listener) {
        getNativeAdsService().request(request, new Callback() {
            @Override
            public void success(Object o, retrofit.client.Response response2) {
                if (listener != null) {
                    listener.success(null);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (listener != null) {
                    listener.error(error);
                }
            }
        });
    }

    /**
     * Send a synchronous request to the Native Ads API.
     *
     * @param request Request specifying one or more Placements
     */
    public Response requestSynchronous(Request request) {
        return getNativeAdsService().request(request);
    }

    /**
     * Set custom properties for User, specifying properties via JSON string.
     * <p/>
     * @param networkId unique network id
     * @param userKey   unique User key
     * @param json      a JSON String representing the custom properties, ie. { "age": 27, "gender": "male }
     * @param listener  callback listener
     */
    public void setUserProperties(long networkId, String userKey, String json, @Nullable final ResponseListener listener) {

        TypedJsonString body = new TypedJsonString(json);

        getNativeAdsService().postUserProperties(networkId, userKey, body, new ResponseCallback() {

            @Override
            public void success(retrofit.client.Response response) {
                if (listener != null) {
                    listener.success(null);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (listener != null) {
                    listener.error(error);
                }
            }
        });
    }

    /**
     * Set custom properties for User, specifying properties via a Map object
     * <p/>
     * @param networkId     unique network id
     * @param userKey       unique User key
     * @param properties    map of key-value pairs
     * @param listener      callback listener
     */
    public void setUserProperties(long networkId, String userKey, Map<String, Object> properties, @Nullable final ResponseListener listener) {


        getNativeAdsService().postUserProperties(networkId, userKey, properties, new ResponseCallback() {

            @Override
            public void success(retrofit.client.Response response) {
                if (listener != null) {
                    listener.success(null);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (listener != null) {
                    listener.error(error);
                }
            }
        });
    }

    /**
     * Returns information about the User specified by userKey.
     * <p/>
     * @param networkId     unique network id
     * @param userKey       unique User key
     * @param listener      callback listener
     */
    public void readUser(long networkId, String userKey, @Nullable final ResponseListener<User> listener) {

        getNativeAdsService().readUser(networkId, userKey, new Callback<User>() {

            @Override
            public void success(User user, retrofit.client.Response response2) {
                if (listener != null) {
                    listener.success(user);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (listener != null) {
                    listener.error(error);
                }
            }
        });
    }


    /**
     * Sets an interest for a User. The User object contains a list of user interest keywords.
     * <p/>
     * @param networkId     unique network id
     * @param userKey       unique User key
     * @param interest      name of interest
     * @param listener      callback listener
     */
    public void setUserInterest(long networkId, String userKey, String interest, @Nullable final ResponseListener listener) {


        getNativeAdsService().setUserInterest(networkId, userKey, interest, new ResponseCallback() {

            @Override
            public void success(retrofit.client.Response response) {
                if (listener != null) {
                    listener.success(null);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (listener != null) {
                    listener.error(error);
                }
            }
        });
    }

    /**
     * Sets ad retargeting for brand and segment.
     * <p/>
     * @param networkId     unique network id
     * @param brandId       unique brand id
     * @param segment       segment identifier
     * @param userKey       unique User key
     * @param listener      callback listener
     */
    public void setUserRetargeting(long networkId, long brandId, String segment, String userKey, @Nullable final ResponseListener listener) {


        getNativeAdsService().setUserRetargeting(networkId, brandId, segment, userKey, new ResponseCallback() {

            @Override
            public void success(retrofit.client.Response response) {
                if (listener != null) {
                    listener.success(null);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (listener != null) {
                    listener.error(error);
                }
            }
        });
    }

    /**
     * Converts the given String to an impression URL.
     *
     * @param urlString
     * @return - false if it is malformed
     */
    public boolean impression(final String urlString) {
        try {
            impression(new URL(urlString));
            return true;
        } catch (MalformedURLException e) {
            Log.e(TAG, "Failed to impress on url: " + urlString, e);
            return false;
        }
    }

    protected void impression(final URL url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    url.openConnection().getContent();
                } catch(IOException e) {
                    Log.e(TAG, "Failed to impress ", e);
                }
            }
        }).start();
    }

    // Create service for the Native Ads API
    private NativeAdService getNativeAdsService() {
        if (service == null ) {
            Gson gson = new GsonBuilder()
                  .registerTypeAdapter(ContentData.class, new ContentDataDeserializer())
                  .create();

            Builder builder = new RestAdapter.Builder()
                    .setEndpoint(ADZERK_ENDPOINT)
                    .setConverter(new GsonConverter(gson))
                    .setLogLevel(RestAdapter.LogLevel.FULL);

            // test client
            if (client != null) {
                builder.setClient(client);
            }

            service = builder.build().create(NativeAdService.class);
        }

        return service;
    }

    // Capture the default deserialization and JsonObject for the 'data.customData' element
    private static class ContentDataDeserializer implements JsonDeserializer<ContentData> {

        @Override
        public ContentData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject dataObject = json.getAsJsonObject();
            Map<String, Object> map = context.deserialize(dataObject, Map.class);
            JsonObject customDataObject = dataObject.getAsJsonObject("customData");

            return new ContentData(map, customDataObject);
        }
    }

    private class TypedJsonString extends TypedString {
        public TypedJsonString(String body) {
            super(body);
        }

        @Override public String mimeType() {
            return "application/json";
        }
    }
}
