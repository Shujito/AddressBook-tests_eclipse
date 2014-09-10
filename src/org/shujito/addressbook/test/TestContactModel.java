package org.shujito.addressbook.test;

import java.util.UUID;

import org.shujito.addressbook.model.Contact;

import android.test.ActivityTestCase;

import com.activeandroid.Model;
import com.activeandroid.query.Delete;

public class TestContactModel extends ActivityTestCase
{
	static final String TAG = TestContactModel.class.getSimpleName();
	
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
	}
	
	public void testSaveEmptyContact()
	{
		Contact contact = new Contact();
		assertEquals(Long.valueOf(-1), contact.save());
	}
	
	public void testSaveAllFieldsButRequired()
	{
		Contact contact = new Contact();
		contact.lastname = "Kirisame";
		contact.address = "Forest of Magic";
		contact.notes = "Ordinary Magician";
		assertEquals(Long.valueOf(-1), contact.save());
	}
	
	public void testSaveRequiredFields()
	{
		Contact contact = new Contact();
		contact.name = "Marisa";
		contact.phone = "54747263";
		assertTrue(contact.save() != -1);
	}
	
	public void testDelete()
	{
		Contact koa = new Contact();
		koa.name = "Koakuma";
		koa.phone = "5685868";
		Long idKoa = koa.save();
		assertTrue(idKoa != -1);
		Contact deleteKoa = Model.load(Contact.class, idKoa);
		deleteKoa.delete();
		// shouldn't exist
		Contact findKoa = Model.load(Contact.class, idKoa);
		assertNull(findKoa);
	}
	
	public void testUpdate()
	{
		// make new with required fields
		Contact contact = new Contact();
		contact.name = "Cirno";
		contact.phone = "999999999";
		// save, keep id for future use
		Long mId = contact.save();
		// assure it is saved
		assertTrue(mId != -1);
		// load contact from id
		Contact edit1 = Model.load(Contact.class, mId);
		// assure it has the same values
		assertEquals("Cirno", edit1.name);
		assertEquals("999999999", edit1.phone);
		// edit values
		edit1.name = "Daiyousei";
		edit1.phone = "7777777";
		// assure it is saved with the same id
		assertEquals(mId, edit1.save());
		// load again
		Contact edit2 = Model.load(Contact.class, mId);
		// assure it has the same values
		assertEquals("Daiyousei", edit2.name);
		assertEquals("7777777", edit2.phone);
		// remove values
		edit2.name = null;
		edit2.phone = null;
		// assure it doesn't save
		Long idEdit2 = edit2.save();
		assertEquals(mId, idEdit2);
		Contact findEdit2 = Model.load(Contact.class, idEdit2);
		assertEquals("Daiyousei", findEdit2.name);
		assertEquals("7777777", findEdit2.phone);
		// load again, last time I promise
		Contact edit3 = Model.load(Contact.class, mId);
		// assure it has the same values
		assertEquals("Daiyousei", edit3.name);
		assertEquals("7777777", edit3.phone);
	}
	
	public void testInsertDuplicateId()
	{
		// define unique id
		String uuid = UUID.randomUUID().toString();
		// make and save
		Contact reimu = new Contact();
		reimu.id = uuid;
		reimu.name = "Reimu";
		reimu.lastname = "Hakurei";
		reimu.phone = "4258734";
		Long idReimu = reimu.save();
		// make another
		Contact sanae = new Contact();
		sanae.id = uuid;
		sanae.name = "Sanae";
		sanae.lastname = "Kochiya";
		sanae.phone = "5624492";
		Long idSanae = sanae.save();
		// load reimu
		Contact loadReimu = Model.load(Contact.class, idReimu);
		// shouldn't exist
		assertNull(loadReimu);
		// load sanae
		Contact loadSanae = Model.load(Contact.class, idSanae);
		// should have all of the following
		assertEquals(uuid, loadSanae.id);
		assertEquals("Sanae", loadSanae.name);
		assertEquals("Kochiya", loadSanae.lastname);
		assertEquals("5624492", loadSanae.phone);
	}
	
	@Override
	protected void tearDown() throws Exception
	{
		new Delete().from(Contact.class).execute();
		super.tearDown();
	}
}
