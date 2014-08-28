package org.shujito.addressbook.test;

import java.math.BigInteger;
import java.util.Random;

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
	// mock information
	private String name = null;
	private String lastname = null;
	private String address = null;
	private String phone = null;
	private String notes = null;
	
	public AddressBookFunctionalTests()
	{
		super(AddressBookActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		// make some mock data
		Random random = new Random();
		this.name = new BigInteger(8 * 8, random).toString(32);
		this.lastname = new BigInteger(8 * 10, random).toString(32);
		this.address = new BigInteger(8 * 25, random).toString(32);
		this.address = this.address.replaceAll("[0-4]", " ");
		this.address = this.address.replaceAll("\\s", " ");
		this.phone = new BigInteger(8 * 4, random).toString();
		this.notes = new BigInteger(8 * 75, random).toString(32);
		this.notes = this.notes.replaceAll("[5-9]", " ");
		this.notes = this.notes.replaceAll("\\s+", " ");
		Solo.Config config = new Solo.Config();
		// take 10 seconds for big instructions
		config.timeout_large = 10000;
		// take 5 seconds for smaller instructions
		config.timeout_small = 5000;
		this.solo = new Solo(this.getInstrumentation(), config, this.getActivity());
	}
	
	public void test00CreateContact()
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
		this.solo.enterText(this.solo.getEditText("Name"), this.name);
		this.solo.clickOnView(this.solo.getEditText("Last Name"));
		this.solo.enterText(this.solo.getEditText("Last Name"), this.lastname);
		this.solo.clickOnView(this.solo.getEditText("Address"));
		this.solo.enterText(this.solo.getEditText("Address"), this.address);
		this.solo.clickOnView(this.solo.getEditText("Phone"));
		this.solo.enterText(this.solo.getEditText("Phone"), this.phone);
		this.solo.clickOnView(this.solo.getEditText("Notes"));
		this.solo.enterText(this.solo.getEditText("Notes"), this.notes);
		// save now
		this.solo.clickOnText("Done");
		// wait for activity
		assertTrue(this.solo.waitForText("Address Book"));
		assertTrue(this.solo.waitForText("New"));
		// wait for values
		assertTrue(this.solo.waitForText(this.name));
		assertTrue(this.solo.waitForText(this.lastname));
		assertTrue(this.solo.waitForText(this.phone));
		// hit back to exit
		this.solo.goBack();
	}
	
	public void test01RequiredFields()
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
		this.solo.clickOnText("Done");
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
	
	public void test02ViewContact()
	{
		assertTrue(this.solo.waitForText("Address Book"));
		assertTrue(this.solo.waitForText("New"));
		// hit a contact
		this.solo.clickOnText(this.name);
		// wait for screen
		assertTrue(this.solo.waitForText("View Contact"));
		// see field headers
		assertTrue(this.solo.waitForText("Name"));
		assertTrue(this.solo.waitForText("Address"));
		assertTrue(this.solo.waitForText("Phone"));
		assertTrue(this.solo.waitForText("Notes"));
		// see actual data
		assertTrue(this.solo.waitForText(this.name));
		assertTrue(this.solo.waitForText(this.lastname));
		assertTrue(this.solo.waitForText(this.phone));
		// go back to main
		this.solo.goBack();
		// assure we're on main
		assertTrue(this.solo.waitForText("Address Book"));
		assertTrue(this.solo.waitForText("New"));
		// hit back to exit
		this.solo.goBack();
	}
	
	public void test03EditContact()
	{
		assertTrue(this.solo.waitForText("Address Book"));
		assertTrue(this.solo.waitForText("New"));
		// hold and edit
		this.solo.clickLongOnText(this.name);
		this.solo.clickOnText("Edit");
		// wait for screen
		assertTrue(this.solo.waitForText("Edit Contact"));
		assertTrue(this.solo.waitForText("Done"));
		// edit some fields
		this.solo.clickOnView(this.solo.getEditText("Name"));
		this.solo.enterText(this.solo.getEditText("Name"), "Nurit");
		this.solo.clickOnView(this.solo.getEditText("Last Name"));
		this.solo.enterText(this.solo.getEditText("Last Name"), "Ramos");
		// save now
		this.solo.clickOnText("Done");
		// wait for activity
		assertTrue(this.solo.waitForText("Address Book"));
		assertTrue(this.solo.waitForText("New"));
		// wait for values
		assertTrue(this.solo.waitForText("Nurit"));
		assertTrue(this.solo.waitForText("Ramos"));
		// hit back to exit
		this.solo.goBack();
	}
	
	public void test04DeleteContact()
	{
		assertTrue(this.solo.waitForText("Address Book"));
		assertTrue(this.solo.waitForText("New"));
		// hold and delete
		this.solo.clickLongOnText(this.address);
		this.solo.clickOnText("Delete");
		// wait for dialog
		assertTrue(this.solo.waitForDialogToOpen());
		// read the text
		assertTrue(this.solo.waitForText("Are you sure you want to delete this contact?"));
		// delete it please
		this.solo.clickOnText("Yes");
		assertTrue(this.solo.waitForDialogToClose());
		// don't see the value
		assertFalse("I still can see the same phone", this.solo.waitForText(this.phone));
		assertFalse("I still can see the same address", this.solo.waitForText(this.address));
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
