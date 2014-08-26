package org.shujito.addressbook.test;

import org.shujito.addressbook.activity.AddressBookActivity;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.robotium.solo.Solo;

/**
 * Functional tests for the {@link AddressBookActivity}
 * @author shujito
 */
public class AddressBookFunctionalTests extends ActivityInstrumentationTestCase2<AddressBookActivity>
{
	static final String TAG = AddressBookFunctionalTests.class.getSimpleName();
	private Solo solo = null;
	
	public AddressBookFunctionalTests()
	{
		super(AddressBookActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		Solo.Config config = new Solo.Config();
		config.timeout_large = 5000;
		config.timeout_small = 5000;
		this.solo = new Solo(this.getInstrumentation(), config, this.getActivity());
	}
	
	public void test00CreateContact()
	{
		assertTrue(this.solo.waitForText("Address Book"));
		assertTrue(this.solo.waitForText("New"));
		// hit new
		this.solo.clickOnMenuItem("New");
		// wait for screen
		assertTrue(this.solo.waitForText("Create Contact"));
		assertTrue(this.solo.waitForText("Done"));
		// fill fields
		this.solo.clickOnView(this.solo.getEditText("Name"));
		this.solo.enterText(this.solo.getEditText("Name"), "Alberto");
		this.solo.clickOnView(this.solo.getEditText("Last Name"));
		this.solo.enterText(this.solo.getEditText("Last Name"), "Ramos");
		this.solo.clickOnView(this.solo.getEditText("Address"));
		this.solo.enterText(this.solo.getEditText("Address"), "Sinaloa MX");
		this.solo.clickOnView(this.solo.getEditText("Phone"));
		this.solo.enterText(this.solo.getEditText("Phone"), "55555555");
		this.solo.clickOnView(this.solo.getEditText("Notes"));
		this.solo.enterText(this.solo.getEditText("Notes"), "Jackdaws love my big sphinx of quartz.");
		// save now
		this.solo.clickOnMenuItem("Done");
		// wait for activity
		assertTrue(this.solo.waitForText("New"));
		assertTrue(this.solo.waitForText("Alberto"));
		assertTrue(this.solo.waitForText("Ramos"));
		assertTrue(this.solo.waitForText("55555555"));
		// hit back to exit
		this.solo.goBack();
	}
	
	public void test01ViewContact()
	{
		assertTrue(this.solo.waitForText("Address Book"));
		assertTrue(this.solo.waitForText("New"));
		// hit back to exit
		this.solo.goBack();
	}
	
	public void test02EditContact()
	{
		assertTrue(this.solo.waitForText("Address Book"));
		assertTrue(this.solo.waitForText("New"));
		this.solo.clickLongInList(0);
		this.solo.clickOnText("Edit");
		// wait for screen
		assertTrue(this.solo.waitForText("Edit Contact"));
		assertTrue(this.solo.waitForText("Done"));
		// edit some fields
		this.solo.clickOnView(this.solo.getEditText("Name"));
		this.solo.enterText(this.solo.getEditText("Name"), "Nurit");
		this.solo.clickOnView(this.solo.getEditText("Phone"));
		this.solo.enterText(this.solo.getEditText("Phone"), "88888888");
		// save now
		this.solo.clickOnMenuItem("Done");
		// wait for activity
		assertTrue(this.solo.waitForText("New"));
		assertTrue(this.solo.waitForText("Nurit"));
		assertTrue(this.solo.waitForText("88888888"));
		// hit back to exit
		this.solo.goBack();
	}
	
	public void test03DeleteContact()
	{
		assertTrue(this.solo.waitForText("Address Book"));
		assertTrue(this.solo.waitForText("New"));
		// hit back to exit
		this.solo.goBack();
	}
	
	@Override
	protected void tearDown() throws Exception
	{
		try
		{
			if (solo != null)
				solo.finalize();
		}
		catch (Throwable ex)
		{
			Log.e(TAG, ex.toString());
		}
		super.tearDown();
	}
}
