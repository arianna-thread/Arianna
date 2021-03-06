package fr.eurecom.mobserv.arianna;

import com.slidingmenu.lib.SlidingMenu;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MapActivity extends BaseDrawerActivity {

	WebView myBrowser;
	EditText Msg;
	Button btnSendMsg;

	/** Called when the activity is first created. */
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar ab = getActionBar();
		ab.setTitle(R.string.title_map);
		ab.setDisplayShowTitleEnabled(true);
		myBrowser = (WebView) findViewById(R.id.mybrowser);

		final JavaScriptInterface myJavaScriptInterface = new JavaScriptInterface(
				this);
		myBrowser.addJavascriptInterface(myJavaScriptInterface,
				"AndroidFunction");

		myBrowser.getSettings().setJavaScriptEnabled(true);
		myBrowser.loadUrl("file:///android_asset/web.html");
		myBrowser.getSettings().setSupportZoom(true);
		myBrowser.getSettings().setBuiltInZoomControls(true);
		myBrowser.getSettings().setDisplayZoomControls(false);
		myBrowser.getSettings().setLightTouchEnabled(false);

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		// myBrowser.loadUrl("file:///android_asset/web.html");
	}

	public void setNavigationControlVisibility(int visibility) {
		setNextVisibility(visibility);
		setPrevVisibility(visibility);
		return;
	}

	public void nextClicked(View v) {
		myBrowser.loadUrl("javascript:arianna.onClickNext()");
	}

	public void prevClicked(View v) {
		myBrowser.loadUrl("javascript:arianna.onClickPrev()");
	}

	public void setNextVisibility(int visibility) {
		ImageView button = (ImageView) findViewById(R.id.button_next);
		button.setVisibility(visibility);
		return;
	}

	public void setPrevVisibility(int visibility) {
		ImageView button = (ImageView) findViewById(R.id.button_prev);
		button.setVisibility(visibility);
	}

	public int toggleNavigationControl() {
		ImageView button = (ImageView) findViewById(R.id.button_next);
		if (button.getVisibility() == View.VISIBLE) {
			button.setVisibility(View.INVISIBLE);
		} else {
			button.setVisibility(View.VISIBLE);
		}
		button = (ImageView) findViewById(R.id.button_prev);
		if (button.getVisibility() == View.VISIBLE) {
			button.setVisibility(View.INVISIBLE);
		} else {
			button.setVisibility(View.VISIBLE);
		}
		return button.getVisibility();
	}

	@Override
	protected int getContentViewResource() {
		return R.layout.activity_map;
	}
	@Override
	protected void onResume() {
		super.onResume();
		myBrowser.loadUrl("file:///android_asset/web.html");
	}

	@Override
	protected int getTouchModeAbove() {
		return SlidingMenu.TOUCHMODE_NONE;
	}
}
