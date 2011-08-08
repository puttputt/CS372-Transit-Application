package com.basementbobs.transitdemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class RoutePlannerActivity extends Activity {
    
	private static final String[] PLACES = new String[] {
        "Belgium", "France", "Italy", "Germany", "Spain", "North America", "South America", "West-South America", "South America [mall]", "South America [school]", "South America East", "South America Mall", "South America (Mall)",
    };
    
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.routeplanner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, PLACES);
        
        final AutoCompleteTextView originText = (AutoCompleteTextView)
                findViewById(R.id.autocomplete_origin);
        AutoCompleteTextView destinationText = (AutoCompleteTextView)
        	findViewById(R.id.autocomplete_destination);
        
        originText.setCompletionHint("Try a mall, school, or landmark.");
        destinationText.setCompletionHint("Try a mall, school, or landmark.");
        
        originText.setThreshold(0);
        destinationText.setThreshold(0);
        
        originText.setAdapter(adapter);
        destinationText.setAdapter(adapter);
    }


}
