package com.example.myapplication;

import static com.example.myapplication.allConstant.Allconstant.KEY_API_ROUTE;

import org.junit.Test;
import org.mapsforge.core.model.LatLong;

import com.example.myapplication.outile.route.DirectionsServiceGETRequest;
import com.example.myapplication.outile.route.DirectionsServiceGETResult;

import java.io.IOException;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws IOException {
        List<LatLong> request = DirectionsServiceGETRequest.getChemainCoordonne(
                "driving-car", // profile
                -19.8659, // latA (Antsirabe)
                47.0335, // lonA (Antsirabe)
                -18.8792, // latB (Antananarivo)
                47.5079// lonB (Antananarivo) // apiKey
        );
        // Print the result
        System.out.println(request);
        //System.out.println(result.getFeatures().get(1).getGeometry());
    }

}