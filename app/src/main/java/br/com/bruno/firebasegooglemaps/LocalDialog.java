package br.com.bruno.firebasegooglemaps;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import br.com.bruno.firebasegooglemaps.model.Local;

/**
 * Created by bruno on 9/23/16.
 */

public class LocalDialog extends DialogFragment
{
    private static final String TAG = LocalDialog.class.getCanonicalName();
    private Activity activity = null;
    private OnAddMarker listner;

    public static LocalDialog getInstance(OnAddMarker listner)
    {
        LocalDialog fragmentDialog = new LocalDialog();
        fragmentDialog.listner = listner;
        return fragmentDialog;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Nome Usuario");

        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.dialog_local, null);
        final EditText edtNome = (EditText) view.findViewById(R.id.edtNome);
        final EditText edtLatitude = (EditText) view.findViewById(R.id.edtLatitude);
        final EditText edtLongitude = (EditText) view.findViewById(R.id.edtLongitude);

        builder.setView(view);

        builder.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("locais");

                Local local = new Local();
                local.setNome(edtNome.getText().toString());
                local.setLatitude(Double.parseDouble(edtLatitude.getText().toString()));
                local.setLongitude(Double.parseDouble(edtLongitude.getText().toString()));

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(myRef.push().getKey(), local.toMap());
                myRef.updateChildren(childUpdates);
                listner.onAddMarker();

            }
        });
        return builder.create();

    }
    public interface OnAddMarker{
        void onAddMarker();
    }
}
