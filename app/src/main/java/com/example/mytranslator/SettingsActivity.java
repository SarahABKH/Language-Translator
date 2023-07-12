package com.example.mytranslator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.nl.translate.TranslateRemoteModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class SettingsActivity extends AppCompatActivity {


    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        listView = findViewById(R.id.list);

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //getting the downloaded models
        RemoteModelManager modelManager = RemoteModelManager.getInstance();
        modelManager.getDownloadedModels(TranslateRemoteModel.class)
                .addOnSuccessListener(new OnSuccessListener<Set<TranslateRemoteModel>>() {
                    @Override
                    public void onSuccess(Set<TranslateRemoteModel> models) {
                        // Convert the set of models to a list
                        List<TranslateRemoteModel> modelList = new ArrayList<>(models);

                        // Set up ListView and adapter
                        ListView listView = findViewById(R.id.list);
                        ModelListAdapter adapter = new ModelListAdapter(SettingsActivity.this, modelList);
                        listView.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error.
                    }
                });



    }


    public class ModelListAdapter extends ArrayAdapter<TranslateRemoteModel> {

        public ModelListAdapter(Context context, List<TranslateRemoteModel> modelList) {
            super(context, 0, modelList);
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(android.R.layout.simple_list_item_1, parent, false);
            }

            TranslateRemoteModel model = getItem(position);

            TextView modelNameTextView = convertView.findViewById(android.R.id.text1);
            String modelName = model.getModelName();
            if (modelName != null) {
                modelNameTextView.setText(modelName);
            } else {
                String bcp47Code = model.getLanguage();
                Locale locale = Locale.forLanguageTag(bcp47Code);
                String languageName = locale.getDisplayName();
                modelNameTextView.setText(languageName + " (" + bcp47Code + ")");
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Delete Model");
                    builder.setMessage("Do you want to delete this model?");
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteModel(model);
                        }
                    });
                    builder.setNegativeButton("Cancel", null);
                    builder.show();
                }
            });

            return convertView;
        }

        private void deleteModel(TranslateRemoteModel model) {
            RemoteModelManager modelManager = RemoteModelManager.getInstance();
            modelManager.deleteDownloadedModel(model)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getContext(), "Model deleted successfully", Toast.LENGTH_SHORT).show();
                            SettingsActivity.this.recreate();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Error deleting model: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }


    }




}