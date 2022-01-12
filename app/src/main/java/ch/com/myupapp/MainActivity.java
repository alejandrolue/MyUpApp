package ch.com.myupapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;

public class MainActivity extends AppCompatActivity {
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;

    private SuggestService suggest;
    private boolean isSelectedFromList;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

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


            mSearchAutoComplete.setText(suggest.getObject(position));
            isSelectedFromList = true;
        });
        mSearchAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                isSelectedFromList = false;
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!isSelectedFromList){

                    return false;
                }
                String[] input = query.split("-");
                redirectToDetails(input[0]);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                isSelectedFromList = false;
                return false;
            }
        });

        handler = new Handler(msg -> {
            if (msg.what == TRIGGER_AUTO_COMPLETE) {

                String input = mSearchAutoComplete.getText().toString();

                if (!TextUtils.isEmpty(mSearchAutoComplete.getText()) && input.length() >= 3) {

                    sendApiCall(input);
                }
            }
            return false;
        });

        return true;
    }
}
