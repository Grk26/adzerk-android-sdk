package com.adzerk.android.sdk.rest;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Contains the key to identify the User in a {@link Response}
 * <p>
 * The response contains the key used to identify the unique user that places the request. All user keys
 * are generated by the API. You can target to a user when you place a {@link Request}.
 *
 * Not supported:
 * - pendingConversions, partnerUserIds, retargettingSegments, cookieMonster, dirtyCookies
 */
public class User  {

    String key;

    boolean isNew;

    List<String> interests;

    @SerializedName("custom")
    UserProperties customProperties;

    boolean optOut;

    Map<String, List<Integer>> blockedItems;

    Map<Integer, List<Long>> flightViewTimes;

    Map<Integer, List<Long>> adViewTimes;

    Map<Integer, List<Long>> siteViewTimes;

    static final String ADVERTISERS = "advertisers";
    static final String CAMPAIGNS = "campaigns";
    static final String CREATIVES = "creatives";
    static final String FLIGHTS = "flights";

    /**
     * Creates User with specified key.
     * @param key identifies user
     */
    public User(String key) {
        this.key = key;
    }

    /**
     * Returns the key that identifies the user placing a {@link Request}
     * @return user key
     */
    public String getKey() {
        return key;
    }

    /**
     * Returns true if this User is newly created by the server
     * @return true if new
     */
    public boolean isNew() {
        return isNew;
    }

    /**
     * List of interest keywords associated with User
     * @return interest keyword list
     */
    public List<String> getInterests() {
        return interests;
    }

    /**
     * Returns true if user has specified interest
     * @param interest keyword
     * @return true if user has interest
     */
    public boolean hasInterest(String interest) {
        if (interests != null) {
            return interests.contains(interest);
        }

        return false;
    }

    /**
     * Returns true if User has opted out of tracking
     * @return true if opted out
     */
    public boolean isOptOut() {
        return optOut;
    }

    /**
     * Returns map of the User custom properties
     * @return key-value paird
     */
    public Map<String, Object> getCustomProperties() {
        if (customProperties != null) {
            return customProperties.getCustomProperties();
        }
        return Collections.EMPTY_MAP;
    }

    /**
     * Retuns value of specified custom property
     *
     * @param key custom property key
     * @return value of property
     */
    public Object getCustomProperty(String key) {
        if (customProperties != null) {
            return customProperties.getCustomProperty(key);
        }
        return null;
    }

    /**
     * Returns the JSON string holding custom properties for User
     *
     * @return string of JSON
     */
    public String getCustomPropertiesAsString() {
        if (customProperties != null) {
            return customProperties.getCustomPropertiesAsString();
        }
        return null;
    }

    /**
     * Returns the JSON object holding custom properties for User
     *
     * @return JSON Object
     */
    public JsonObject getCustomPropertiesAsJson() {
        if (customProperties != null) {
            return customProperties.getCustomPropertiesJson();
        }
        return null;
    }

    /**
     * Map of blocked items for 'advertisers', 'campaigns', 'creatives' and 'flights'
     * @return
     */
    public Map<String, List<Integer>> getBlockedItems() {
        return blockedItems;
    }

    /**
     * List of blocked advertisers
     * @return blocked advertiser ids
     */
    public List<Integer> getBlockedAdvertisers() {
        if (blockedItems != null) {
            return blockedItems.get(ADVERTISERS);
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * Returns a list of blocked campaigns
     * @return blocked campaign ids
     */
    public List<Integer> getBlockedCampaigns() {
        if (blockedItems != null) {
            return blockedItems.get(CAMPAIGNS);
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * Returns a list of blocked creatives
     * @return blocked creative ids
     */
    public List<Integer> getBlockedCreatives() {
        if (blockedItems != null) {
            return blockedItems.get(CREATIVES);
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * Returns a list of blocked flights
     * @return blocked flights ids
     */
    public List<Integer> getBlockedFlights() {
        if (blockedItems != null) {
            return blockedItems.get(FLIGHTS);
        }
        return Collections.EMPTY_LIST;
    }
    /**
     * Map of flight ids to list of Unix epoch timestamps
     * @return
     */
    public Map<Integer, List<Long>> getFlightViewTimes() {
        return flightViewTimes;
    }

    /**
     * Map of ad ids to list of Unix epoch timestamps
     * @return
     */
    public Map<Integer, List<Long>> getAdViewTimes() {
        return adViewTimes;
    }

    /**
     * Map of site ids to list of Unix epoch timestamps
     * @return
     */
    public Map<Integer, List<Long>> getSiteViewTimes() {
        return siteViewTimes;
    }
}
