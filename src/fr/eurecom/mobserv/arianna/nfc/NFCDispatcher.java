package fr.eurecom.mobserv.arianna.nfc;

import fr.eurecom.mobserv.arianna.DashboardActivity;
import fr.eurecom.mobserv.arianna.PointOfInterestDetail;
import fr.eurecom.mobserv.arianna.R;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.content.Intent;

/**
 * 
 * @author uccio
 *
 */

public class NFCDispatcher extends Activity {

	// the activity does not remain into the stack
	// thanks to android:noHistory tag defined in the
	//manifest. That also prevent the intent to be delivered
	// each time the user push the back button, avoiding
	// an infinite loop
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_nfcdispatcher);
	}

	// onResume is called every time the activity come to foreground,
	// even the first time
	public void onResume() {
		super.onResume();

		// retrieve the intent which start the activity
		Intent intent = getIntent();

		// check if the intent is the type we requested
		// TODO support for older version: no NDEF_DISCOVER?
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {

			String payload = readTag(intent);
			if (payload != null) {
				// TODO DEBUG
				//Toast.makeText(this, "Tag detected. Current Payload: " + payload, Toast.LENGTH_LONG ).show();
				
				startActivity(prepareIntent(payload));
			} else {
				// TODO DEBUG
				//Toast.makeText(this, "Tag detected. No Payload", Toast.LENGTH_LONG ).show();
			}
		}
	}

	/**
	 * 
	 * @param payload String version of the content of the NDEF tag
	 * @return Intent to start an activity based on the payload
	 */
	private Intent prepareIntent(String payload) {
		Intent intent = new Intent(this, PointOfInterestDetail.class);
		// the uri of the POI is stored in the intent
		// constants for intent extra are defined in the dashboard
		intent.putExtra(DashboardActivity.EXTRA_URI, payload);
		intent.putExtra(DashboardActivity.EXTRA_LAUNCHER, DashboardActivity.LAUNCHER_NFC);
		// TODO SOLVE DOUBLE ACTIVITY DETAIL ISSUE
		//intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return(intent);
	}

	/**
	 *
	 * @param intent the intent which started the activity with the NFC info
	 * @return String with the payload of the tag, null if it is not defined
	 */
	private String readTag(Intent intent) {
		Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		String payload = null;
		if (rawMsgs != null) {
			// cast to NDEF message format
			NdefMessage msg = (NdefMessage) rawMsgs[0];
			// this method is always safe
			NdefRecord payloadRecord = msg.getRecords()[0];
			payload = new String(payloadRecord.getPayload());
		}
		return payload;
	}

		/* TODO is it necessary?
	@Override
	public void onNewIntent(Intent intent) {
		Toast.makeText(getApplicationContext(), "Discovered tag with intent: " + intent, Toast.LENGTH_LONG).show();
	}
		 */

}
