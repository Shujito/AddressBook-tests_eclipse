package org.shujito.addressbook.test;

import org.shujito.addressbook.activity.AddressBookActivity;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.robotium.solo.Solo;

/**
 * Functional tests for the {@link AddressBookActivity}.
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
	
	/* life fiber */
	@Override
	/* kamui junketsu */
	protected void setUp() throws Exception
	{
		super.setUp();
		Solo.Config config = new Solo.Config();
		// take 10 seconds for big instructions
		config.timeout_large = 10000;
		// take 5 seconds for smaller instructions
		config.timeout_small = 5000;
		this.solo = new Solo(this.getInstrumentation(), config, this.getActivity());
	}
	
	/**
	 * Tests creating a contact
	 */
	public void test000CreateContact()
	{
		assertTrue(this.solo.waitForText("Address Book"));
		assertTrue(this.solo.waitForText("New"));
		// hit new
		this.solo.clickOnText("New");
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
		assertTrue(this.solo.waitForText("Address Book"));
		assertTrue(this.solo.waitForText("New"));
		// wait for values
		assertTrue(this.solo.waitForText("Alberto"));
		assertTrue(this.solo.waitForText("Ramos"));
		assertTrue(this.solo.waitForText("55555555"));
		// hit back to exit
		this.solo.goBack();
	}
	
	/**
	 * Tests required fields
	 */
	public void test001RequiredFields()
	{
		assertTrue(this.solo.waitForText("Address Book"));
		assertTrue(this.solo.waitForText("New"));
		// hit new
		this.solo.clickOnText("New");
		// wait for screen
		assertTrue(this.solo.waitForText("Create Contact"));
		assertTrue(this.solo.waitForText("Done"));
		// see fields
		assertTrue(this.solo.waitForText("Name"));
		assertTrue(this.solo.waitForText("Last Name"));
		assertTrue(this.solo.waitForText("Address"));
		assertTrue(this.solo.waitForText("Phone"));
		assertTrue(this.solo.waitForText("Notes"));
		// hit done
		this.solo.clickOnMenuItem("Done");
		// see messages
		assertTrue(this.solo.waitForText("Required field"));
		// go back to main
		this.solo.goBack();
		// assure we're on main
		assertTrue(this.solo.waitForText("Address Book"));
		assertTrue(this.solo.waitForText("New"));
		// hit back to exit
		this.solo.goBack();
	}
	
	/**
	 * Tests viewing a previously added contact
	 */
	public void test002ViewContact()
	{
		assertTrue(this.solo.waitForText("Address Book"));
		assertTrue(this.solo.waitForText("New"));
		// hit a contact
		this.solo.clickOnText("Alberto");
		// wait for screen
		assertTrue(this.solo.waitForText("View Contact"));
		// see field headers
		assertTrue(this.solo.waitForText("Name"));
		assertTrue(this.solo.waitForText("Address"));
		assertTrue(this.solo.waitForText("Phone"));
		assertTrue(this.solo.waitForText("Notes"));
		// see actual data
		assertTrue(this.solo.waitForText("Alberto"));
		assertTrue(this.solo.waitForText("Ramos"));
		assertTrue(this.solo.waitForText("55555555"));
		// go back to main
		this.solo.goBack();
		// assure we're on main
		assertTrue(this.solo.waitForText("Address Book"));
		assertTrue(this.solo.waitForText("New"));
		// hit back to exit
		this.solo.goBack();
	}
	
	/**
	 * Tests editing a previously added contact
	 */
	public void test003EditContact()
	{
		assertTrue(this.solo.waitForText("Address Book"));
		assertTrue(this.solo.waitForText("New"));
		// hold and edit
		this.solo.clickLongOnText("Alberto Ramos");
		this.solo.clickOnMenuItem("Edit");
		// wait for screen
		assertTrue(this.solo.waitForText("Edit Contact"));
		assertTrue(this.solo.waitForText("Done"));
		// wait for matching data
		assertTrue(this.solo.waitForText("Alberto"));
		assertTrue(this.solo.waitForText("Ramos"));
		assertTrue(this.solo.waitForText("55555555"));
		// the 'Name' field occupies the 'Alberto' string at this time
		this.solo.clickOnView(this.solo.getEditText("Alberto"));
		this.solo.clearEditText(this.solo.getEditText("Alberto"));
		this.solo.enterText(this.solo.getEditText("Name"), "Nurit");
		// and the 'Phone' field occupies the '55555555' string at this time
		this.solo.clickOnView(this.solo.getEditText("55555555"));
		this.solo.clearEditText(this.solo.getEditText("55555555"));
		this.solo.enterText(this.solo.getEditText("Phone"), "88881234");
		// save now
		this.solo.clickOnMenuItem("Done");
		// wait for activity
		assertTrue(this.solo.waitForText("Address Book"));
		assertTrue(this.solo.waitForText("New"));
		// expect for these two not to exist
		assertFalse("I still can see the same name", this.solo.waitForText("Alberto"));
		assertFalse("I still can see the same phone", this.solo.waitForText("55555555"));
		// wait for values
		assertTrue(this.solo.waitForText("Nurit"));
		assertTrue(this.solo.waitForText("88881234"));
		// hit back to exit
		this.solo.goBack();
	}
	
	/**
	 * Tests deleting a previously added contact
	 */
	public void test004DeleteContact()
	{
		assertTrue(this.solo.waitForText("Address Book"));
		assertTrue(this.solo.waitForText("New"));
		// hold and delete
		this.solo.clickLongOnText("Nurit");
		this.solo.clickOnMenuItem("Delete", true);
		// wait for dialog
		assertTrue(this.solo.waitForDialogToOpen());
		// read the text
		assertTrue(this.solo.waitForText("Are you sure you want to delete the selected contacts?"));
		// delete it please
		this.solo.clickOnText("OK");
		assertTrue(this.solo.waitForDialogToClose());
		// don't see the value
		assertFalse("I still can see the same name", this.solo.waitForText("Nurit"));
		assertFalse("I still can see the same phone", this.solo.waitForText("88881234"));
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
