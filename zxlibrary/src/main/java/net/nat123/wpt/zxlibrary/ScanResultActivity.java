package net.nat123.wpt.zxlibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ScanResultActivity extends Activity {

	private TextView scan_tip_message;
	private Button btn_login_web;
	private TextView btn_cancel;
	private String scantipmessage;
	private Boolean result;
	private RelativeLayout scan_success;
	private TextView scan_result;
	private LinearLayout cancel_ll;
	private String appName;
	private Button wap_scanR;
	private View copy_layout;
	private View share_layout;
	private List<String> shareLists = new ArrayList<String>();
	
	Handler hand = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			ScanResultActivity.this.finish();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan_result);

		appName = getResources().getString(R.string.app_name);
		initview();
		Intent intent = getIntent();
		// String username = intent.getStringExtra("");
		result = intent.getBooleanExtra(MipcaActivityCapture.RESULT, false);
		scantipmessage = intent
				.getStringExtra(MipcaActivityCapture.SCAN_RESULT);
		// user_name.setText(username);
		// if(scantipmessage.indexOf("bg://")>=0){
		if (result) {
			scan_tip_message.setText(appName);
		} else {
			scan_success.setVisibility(View.GONE);
			scan_result.setText(scantipmessage);

		}
		
	}

	private void initview() {
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
}
