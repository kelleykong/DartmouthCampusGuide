package com.example.campuswiki;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

//MyRunsDialogFragment handles all the customized dialog boxes in our project.
//Differentiated by dialog id.
// 
// Ref: http://developer.android.com/reference/android/app/DialogFragment.html

public class MyDialogFragment extends DialogFragment {

	// Different dialog IDs
	public static final int DIALOG_ID_ERROR = -1;
	public static final int DIALOG_ID_PHOTO_PICKER = 1;
	public static final int DIALOG_ID_MANUAL_INPUT_DATE = 2;
	public static final int DIALOG_ID_MANUAL_INPUT_TIME = 3;
	public static final int DIALOG_ID_MANUAL_INPUT_DURATION = 4;
	public static final int DIALOG_ID_MANUAL_INPUT_DISTANCE = 5;
	public static final int DIALOG_ID_MANUAL_INPUT_CALORIES = 6;
	public static final int DIALOG_ID_MANUAL_INPUT_HEARTRATE = 7;
	public static final int DIALOG_ID_MANUAL_INPUT_COMMENT = 8;
	
	public static final int DIALOG_ID_MANUAL_INPUT_NAME = 9;
	public static final int DIALOG_ID_MANUAL_INPUT_RANKING = 10;
	public static final int DIALOG_ID_MANUAL_INPUT_DESC = 11;

	// For photo picker selection:
	public static final int ID_PHOTO_PICKER_FROM_CAMERA = 0;
	public static final int ID_PHOTO_PICKER_FROM_ALBUM = 1;
	private static final String DIALOG_ID_KEY = "dialog_id";

	public static MyDialogFragment newInstance(int dialog_id) {
		MyDialogFragment frag = new MyDialogFragment();
		Bundle args = new Bundle();
		args.putInt(DIALOG_ID_KEY, dialog_id);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int dialog_id = getArguments().getInt(DIALOG_ID_KEY);

		final Activity parent = getActivity();
		final Calendar now = Calendar.getInstance();

		// Setup dialog appearance and onClick Listeners
		switch (dialog_id) {
		case DIALOG_ID_PHOTO_PICKER:
			// Build picture picker dialog for choosing from camera or gallery
			AlertDialog.Builder builder = new AlertDialog.Builder(parent);
			builder.setTitle(R.string.ui_profile_photo_picker_title);
			// Set up click listener, firing intents open camera
			DialogInterface.OnClickListener dlistener = new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					// Item is ID_PHOTO_PICKER_FROM_CAMERA
					// Call the onPhotoPickerItemSelected in the parent
					// activity, i.e., ameraControlActivity in this case
					((ManualInputPlace) parent)
							.onPhotoPickerItemSelected(item);
				}
			};
			// Set the item/s to display and create the dialog
			builder.setItems(R.array.ui_profile_photo_picker_items, dlistener);
			return builder.create();
		

		case DIALOG_ID_MANUAL_INPUT_NAME:
			final EditText textName = new EditText(parent);
			//textComment.setHint(R.string.ui_manual_input_comment_hint);
			textName.setInputType(InputType.TYPE_CLASS_TEXT);

			return new AlertDialog.Builder(parent)
					//.setTitle(R.string.ui_manual_input_comment_title)
					.setView(textName)
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int whichButton) {

									String name = textName.getText()
											.toString();

									((ManualInputPlace) parent)
											.onNameSet(name);
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// Cancelled.
									dismiss();
								}
							}).create();
			
		case DIALOG_ID_MANUAL_INPUT_RANKING:
			final EditText textRanking = new EditText(parent);
			//textComment.setHint(R.string.ui_manual_input_comment_hint);
			textRanking.setInputType(InputType.TYPE_CLASS_NUMBER);

			return new AlertDialog.Builder(parent)
					//.setTitle(R.string.ui_manual_input_comment_title)
					.setView(textRanking)
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int whichButton) {

									int ranking = Integer.parseInt(textRanking.getText()
											.toString());

									((ManualInputPlace) parent)
											.onRankingSet(ranking);
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// Cancelled.
									dismiss();
								}
							}).create();
			
		case DIALOG_ID_MANUAL_INPUT_DESC:
			final EditText textDesc = new EditText(parent);
			//textComment.setHint(R.string.ui_manual_input_comment_hint);
			textDesc.setInputType(InputType.TYPE_CLASS_TEXT);
			textDesc.setHeight(500);
			textDesc.setHint("What do you think about this place?");
			return new AlertDialog.Builder(parent)
					//.setTitle(R.string.ui_manual_input_comment_title)
					.setView(textDesc)
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int whichButton) {

									String desc = textDesc.getText()
											.toString();

									((ManualInputPlace) parent)
											.onDescSet(desc);
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// Cancelled.
									dismiss();
								}
							}).create();

		default:
			return null;
		}
	}
}
