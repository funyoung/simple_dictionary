package appulse.simple.dictionary;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;

public class BaseActivity extends SherlockActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// make a spinner in the actionbar and create the activity
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#43484A")));
	}

    protected final void setActionBarIcon(@DrawableRes int icon) {
        getSupportActionBar().setIcon(icon);
    }

    protected final void setActionBarIcon(@DrawableRes int icon, String title, boolean homeAsUp) {
        setActionBarIcon(icon);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUp);
    }

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
