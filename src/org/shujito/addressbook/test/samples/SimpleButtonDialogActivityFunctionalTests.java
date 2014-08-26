package org.shujito.addressbook.test.samples;

import org.shujito.addressbook.R;
import org.shujito.addressbook.SimpleButtonDialogActivity;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.Suppress;
import android.util.Log;

import com.robotium.solo.Solo;

/**
 * Simple functional test for an activity with a single button
 * that pops up a dialog.<br>
 * {@link Suppress}'ed so it doesn't actually executes
 * @author shujito
 */
@Suppress
public class SimpleButtonDialogActivityFunctionalTests extends ActivityInstrumentationTestCase2<SimpleButtonDialogActivity>
{
	static final String TAG = SimpleButtonDialogActivityFunctionalTests.class.getSimpleName();
	private Solo solo = null;
	
	public SimpleButtonDialogActivityFunctionalTests()
	{
		super(SimpleButtonDialogActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		this.solo = new Solo(this.getInstrumentation(), this.getActivity());
	}
	
	public void testButtonAndAlert()
	{
		this.solo.clickOnButton(this.solo.getString(R.string.app_name));
		this.solo.waitForDialogToOpen();
		this.solo.waitForText("Hi there");
		this.solo.clickOnButton(this.solo.getString(android.R.string.ok));
		this.solo.waitForDialogToClose();
		this.solo.waitForText(this.solo.getString(R.string.app_name));
	}
	
	@Override
	protected void tearDown() throws Exception
	{
		try
		{
			solo.finalize();
		}
		catch (Throwable ex)
		{
			Log.e(TAG, ex.toString());
		}
		super.tearDown();
	}
}
