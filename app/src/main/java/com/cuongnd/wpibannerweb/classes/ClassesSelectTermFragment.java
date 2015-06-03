package com.cuongnd.wpibannerweb.classes;

import android.app.ListFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cuongnd.wpibannerweb.helper.Utils;

import java.util.ArrayList;

/**
 * Created by Cuong Nguyen on 5/29/2015.
 */
public class ClassesSelectTermFragment extends ListFragment {

    private GetTermsTask mGetTermsTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGetTermsTask = new GetTermsTask();
        mGetTermsTask.execute();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Utils.TermValue selectedTerm = (Utils.TermValue) getListAdapter().getItem(position);
        Intent i = new Intent(getActivity(), ClassesActivity.class);
        i.putExtra(ClassesFragment.EXTRA_TERM_ID, selectedTerm.getValue());
        startActivity(i);
    }

    @Override
    public void onStop() {
        if (mGetTermsTask != null) {
            mGetTermsTask.cancel(false);
            mGetTermsTask = null;
        }
        super.onStop();
    }

    private class GetTermsTask extends AsyncTask<Void, Void, ArrayList<Utils.TermValue>> {
        @Override
        protected ArrayList<Utils.TermValue> doInBackground(Void... params) {
            return ClassesPage.getTerms(getActivity());
        }

        @Override
        protected void onPostExecute(ArrayList<Utils.TermValue> termValues) {
            ArrayAdapter<Utils.TermValue> adapter =
                    new ArrayAdapter<>(getActivity(),
                            android.R.layout.simple_list_item_1, termValues);
            ClassesSelectTermFragment.this.setListAdapter(adapter);
        }
    }
}
