package org.shujito.addressbook.test.samples;

import org.shujito.addressbook.R;
import org.shujito.addressbook.SimpleButtonDialogActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.Suppress;
import android.view.ContextThemeWrapper;

/**
 * Simple unit test that tests for content in an activity.<br>
 * {@link Suppress}'ed so it doesn't actually executes.
 * @author shujito
 */
@Suppress
public class SimpleButtonDialogActivityUnitTests extends ActivityUnitTestCase<SimpleButtonDialogActivity>
{
	private SimpleButtonDialogActivity mActivity = null;
	
	public SimpleButtonDialogActivityUnitTests()
	{
		super(SimpleButtonDialogActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		Context context = this.getInstrumentation().getTargetContext();
		ContextWrapper wrapper = new ContextThemeWrapper(context, org.shujito.addressbook.R.style.Theme_AppCompat);
		this.setActivityContext(wrapper);
		Intent intent = new Intent(context, SimpleButtonDialogActivity.class);
		this.startActivity(intent, null, null);
		this.mActivity = this.getActivity();
	}
	
	public void testHasContent()
	{
		assertNotNull("this activity has no content", this.mActivity.findViewById(android.R.id.content));
		assertNotNull("button not found!", this.mActivity.findViewById(R.id.btn_hello));
	}
	
	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}
}
