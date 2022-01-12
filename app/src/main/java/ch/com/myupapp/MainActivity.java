package ch.com.myupapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

public class MainActivity extends AppCompatActivity {
    private SuggestService suggest;
    private boolean isSelectedFromList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        SearchView.SearchAutoComplete mSearchAutoComplete = searchView.findViewById(R.id.search_src_text);

        suggest = new SuggestService(this,
                android.R.layout.simple_dropdown_item_1line);

        mSearchAutoComplete.setAdapter(suggest);
        mSearchAutoComplete.setDropDownHeight(1300);
        mSearchAutoComplete.setOnItemClickListener((parent, view, position, id) -> {

//            String query = (String)parent.getItemAtPosition(position);

            mSearchAutoComplete.setText(suggest.getObject(position));
            isSelectedFromList = true;
        });
}
}