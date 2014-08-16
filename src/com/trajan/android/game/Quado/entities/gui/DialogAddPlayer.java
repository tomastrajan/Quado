package com.trajan.android.game.Quado.entities.gui;

/*

   Created with IntelliJ IDEA.

   Ing. Tomáš Herich
   --------------------------- 
   05. 08. 2014
   21:17

*/

import android.app.Dialog;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import com.trajan.android.game.Quado.Elements;
import com.trajan.android.game.Quado.MainGamePanel;
import com.trajan.android.game.Quado.R;
import com.trajan.android.game.Quado.components.LocalPersistenceService;

public class DialogAddPlayer {

    public DialogAddPlayer(final MainGamePanel game, final DialogListener confirm, final DialogListener cancel) {
        final Dialog dialog = new Dialog(game.getContext(), R.style.DialogSimpleInput);
        dialog.setContentView(R.layout.dialog_simpleinput);
        dialog.setTitle("Add new player (max 10 characters)");

        final EditText input = (EditText) dialog.findViewById(R.id.input);
        input.setBackgroundColor(Color.parseColor("#ffffff"));
        android.widget.Button buttonSave = (android.widget.Button) dialog.findViewById(R.id.dialogButtonOk);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPlayerName = input.getText().toString();
                if (newPlayerName != null && newPlayerName.length() > 0 && newPlayerName.length() <= 10) {
                    LocalPersistenceService storage = (LocalPersistenceService) game.getElements().getComponent(Elements.EXTERNAL_STORAGE_PROVIDER);
                    storage.createNewPlayer(newPlayerName);
                    dialog.hide();
                    executeIfPresent(confirm);
                }
            }
        });
        android.widget.Button buttonCancel = (android.widget.Button) dialog.findViewById(R.id.dialogButtonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.setText(null);
                dialog.hide();
                executeIfPresent(cancel);

            }
        });

        dialog.show();
    }

    private void executeIfPresent(DialogListener listener) {
        if (listener != null) {
            listener.execute();
        }
    }
}
