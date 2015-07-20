package com.adzerk.android.sdk.rest;

import com.adzerk.android.sdk.AdzerkSdk;
import com.adzerk.android.sdk.rest.Request.Builder;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;


public class RequestTest {

    AdzerkSdk sdk;

    final static Placement placement = new Placement("div1", 9709, 70464, Arrays.asList(5));
    final static List<Placement> placements = Arrays.asList(placement);

    @Mock NativeAdService api;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        sdk = AdzerkSdk.getInstance(api);
    }

    @Test
    public void itShouldThrowOnEmptyPlacements() {
        try {
            new Builder(new ArrayList<Placement>()).build();
            fail("Failed to throw on empty placements");
        } catch(IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void itShouldBuildUser() {

        try {
            String key = "testUserKey";

            Request request = new Builder(placements)
                  .setUser(new User(key))
                  .build();

            assertThat(request.getUser().getKey()).isEqualTo(key);
        } catch (IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void itShouldBuildKeywords() {

        try {
            Set<String> keywords = new HashSet<>();
            keywords.add("key1");
            keywords.add("key2");
            keywords.add("key3");

            Request request = new Builder(placements)
                  .setKeywords(keywords)
                  .build();

            assertThat(request.getKeywords().containsAll(Arrays.asList("key1", "key2", "key3")));
        } catch (IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void itShouldAddKeywords() {
        try {
            Set<String> keywords = new HashSet<>();
            keywords.add("key1");
            keywords.add("key2");
            keywords.add("key3");

            Request request = new Builder(placements)
                  .setKeywords(keywords)
                  .addKeyword("key4")
                  .addKeyword("key5")
                  .build();

            assertThat(request.getKeywords().containsAll(Arrays.asList("key1", "key2", "key3", "key4", "key5")));

        } catch (IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void itShouldEnforceUniqueKeywords() {
        try {
            Request request = new Builder(placements)
                  .addKeyword("duplicateKey")
                  .addKeyword("duplicateKey")
                  .build();

            assertThat(request.getKeywords().size()).isEqualTo(1);

        } catch (IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void itShouldBuildReferrer() {

        try {
            String referrer = "http://referrer.com";

            Request request = new Builder(placements)
                  .setReferrer(referrer)
                  .build();

            assertThat(request.getReferrer()).isEqualTo(referrer);

        } catch (IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void itShouldBuildUrl() {
        try {
            String url = "http://test.com";

            Request request = new Builder(placements)
                  .setUrl(url)
                  .build();

            assertThat(request.getUrl()).isEqualTo(url);

        } catch (IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void itShouldBuildTime() {
        try {
            long time = 1437425417;

            Request request = new Builder(placements)
                  .setTime(time)
                  .build();

            assertThat(request.getTime()).isEqualTo(time);

        } catch (IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void itShouldBuildIp() {
        try {
            String ip = "192.168.1.1";

            Request request = new Builder(placements)
                  .setIp(ip)
                  .build();

            assertThat(request.getIp()).isEqualTo(ip);

        } catch (IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void itShouldBuildBlockedCreatives() {

        try {
            Set<Integer> blocked = new HashSet(Arrays.asList(1, 2, 3, 4, 5));

            Request request = new Builder(placements)
                  .setBlockedCreatives(blocked)
                  .build();

            assertThat(request.getBlockedCreatives()).containsAll(blocked);

        } catch (IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void itShouldAddBlockedCreatives() {
        try {
            int blocked1 = 1;
            int blocked2 = 2;

            Request request = new Builder(placements)
                  .addBlockedCreative(blocked1)
                  .addBlockedCreative(blocked2)
                  .build();

            assertThat(request.getBlockedCreatives()).contains(blocked1);
            assertThat(request.getBlockedCreatives()).contains(blocked2);
        } catch (IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void itShouldBuildFlightViewTimes() {
        try {
            List<Long> flightViewTimes1 = Arrays.<Long>asList(1401580800L, 1404172800L, 1406851200L);
            List<Long> flightViewTimes2 = Arrays.<Long>asList(1409529600L, 1412121600L);
            List<Long> flightViewTimes3 = Arrays.<Long>asList(1420070400L);

            Request request = new Builder(placements)
                  .setFlightViewTimes(1, flightViewTimes1)
                  .setFlightViewTimes(2, flightViewTimes2)
                  .setFlightViewTimes(3, flightViewTimes3)
                  .build();

            assertThat(request.getAllFlightViewTimes()).containsKeys(1, 2, 3);
            assertThat(request.getFlightViewTimes(1)).containsAll(flightViewTimes1);
            assertThat(request.getFlightViewTimes(2)).containsAll(flightViewTimes2);
            assertThat(request.getFlightViewTimes(3)).containsAll(flightViewTimes3);

        } catch (IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void itShouldBuildIsMobile() {
        try {
            Request request = new Builder(placements)
                  .setMobile(true)
                  .build();

            assertThat(request.isMobile()).isTrue();

        } catch (IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
    
}