package shop.smartbazaar.smartbazaar.public_classes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import shop.smartbazaar.smartbazaar.R;
import shop.smartbazaar.smartbazaar.user.User;

public class SelectLangDialog extends DialogFragment {


    String SelectedLang;
    String langKey = "";
    int SelectedLangPos;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final String[] langs = getActivity().getResources().getStringArray(R.array.langsArray);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.selectLangTxt));

        switch (new User(getActivity()).getLang()) {
            case "en":
                SelectedLangPos = 1;
                break;
            case "ar":
                SelectedLangPos = 0;
                break;
            default:
                SelectedLangPos = -1;
                break;
        }

        builder.setSingleChoiceItems(langs, SelectedLangPos, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                SelectedLang = langs[which];


                if (SelectedLang.equals("English")){
                    langKey = "en";
                }else if (SelectedLang.equals("العربية")){
                    langKey = "ar";
                }



            }
        });

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });


        builder.setPositiveButton(getString(R.string.save),new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (SelectedLang !=null){

                    if (!langKey.equals("")){
                        if (SelectedLang.equals("English")){
                            langKey = "en";
                        }else if (SelectedLang.equals("العربية")){
                            langKey = "ar";
                        }
                    }

                    if (getActivity() != null) {
                        new User(getActivity()).saveLang(langKey);
                        Utiles.setAppDirection(getActivity(),langKey);

                        Utiles.restartApp(getActivity());



                    } else {
                        if (getContext() != null) {
                            new User(getContext()).saveLang(langKey);
                        }

                    }

                }
                dialog.dismiss();

            }
        });


        return builder.create();



    }
}
