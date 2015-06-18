package com.dieese.agenda;

import android.content.Context;
import android.widget.Toast;

abstract class ContatoUtil {

	public static void showMessage(Context context, String msg){
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

}
