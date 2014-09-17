package org.shujito.addressbook.test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.shujito.addressbook.AddressBookApplication;
import org.shujito.addressbook.controller.AddressBookApiController;
import org.shujito.addressbook.controller.AddressBookApiController.LoginException;
import org.shujito.addressbook.controller.AddressBookApiController.ServerException;
import org.shujito.addressbook.model.Contact;
import org.shujito.addressbook.model.Result;
import org.shujito.addressbook.model.Session;
import org.shujito.addressbook.model.User;

import android.test.InstrumentationTestCase;

public class AddressBookServerTests extends InstrumentationTestCase
{
	public static final String TAG = AddressBookServerTests.class.getSimpleName();
	private AddressBookApiController mController = null;
	
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		this.mController = new AddressBookApiController(AddressBookApplication.getInstance());
		Thread.sleep(1000);
	}
	
	public void testAsyncLoginMissingUsername()
	{
		Result result = this.mController.login(null, "only password", null);
		assertEquals(AddressBookApiController.STATUS_NO_USERNAME, result.status);
		assertEquals("Username required", result.message);
	}
	
	public void testAsyncLoginMissingPassword()
	{
		Result result = this.mController.login("only username", null, null);
		assertEquals(AddressBookApiController.STATUS_NO_PASSWORD, result.status);
		assertEquals("Password required", result.message);
	}
	
	public void testAsyncLoginRequiredFields()
	{
		Result result = this.mController.login(null, null, null);
		assertEquals(AddressBookApiController.STATUS_NO_USERNAME | AddressBookApiController.STATUS_NO_PASSWORD, result.status);
		assertEquals("Username and password required", result.message);
	}
	
	public void testAsyncLoginFailed() throws Exception
	{
		final CountDownLatch latch = new CountDownLatch(1);
		Result result = this.mController.login("unregistered username", "bad password", new AddressBookApiController.LoginCallback()
		{
			public void onLoginSuccess(AddressBookApiController controller, Session login)
			{
				fail("The test should not get here");
			}
			
			public void onLoginFailure(AddressBookApiController controller, Result result)
			{
				assertEquals("bad credentials", result.message);
				assertEquals(401, result.status);
				assertEquals(1, latch.getCount());
				latch.countDown();
			}
		});
		assertNull(result);
		assertTrue(latch.await(10000, TimeUnit.MILLISECONDS));
		assertEquals(0, latch.getCount());
	}
	
	public void testAsyncLoginSuccess() throws Exception
	{
		final CountDownLatch latch = new CountDownLatch(1);
		Result result = this.mController.login("shujito", "shujito", new AddressBookApiController.LoginCallback()
		{
			public void onLoginSuccess(AddressBookApiController controller, Session login)
			{
				assertNotNull(login.id);
				assertNotNull(login.uid);
				assertEquals("/users", login.path);
				assertEquals(1, latch.getCount());
				latch.countDown();
			}
			
			public void onLoginFailure(AddressBookApiController controller, Result result)
			{
				fail("The test should not get here");
			}
		});
		assertNull(result);
		assertTrue(latch.await(10000, TimeUnit.MILLISECONDS));
		assertEquals(0, latch.getCount());
	}
	
	public void testSyncLoginMissingUsername() throws ServerException
	{
		try
		{
			this.mController.login(null, "only password");
			fail("The test should not reach this line");
		}
		catch (LoginException le)
		{
			assertNotNull(le);
			assertEquals("Username required", le.getMessage());
		}
	}
	
	public void testSyncLoginMissingPassword() throws ServerException
	{
		try
		{
			this.mController.login("only username", null);
			fail("The test should not reach this line");
		}
		catch (LoginException le)
		{
			assertNotNull(le);
			assertEquals("Password required", le.getMessage());
		}
	}
	
	public void testSyncLoginRequiredFields() throws ServerException
	{
		try
		{
			this.mController.login(null, null);
			fail("The test should not reach this line");
		}
		catch (LoginException le)
		{
			assertNotNull(le);
			assertEquals("Username and password required", le.getMessage());
		}
	}
	
	public void testSyncLoginFailed() throws LoginException
	{
		try
		{
			this.mController.login("bad username", "wrong password");
			fail("The test should not reach this line");
		}
		catch (ServerException se)
		{
			assertNotNull(se);
			assertEquals("bad credentials", se.getMessage());
		}
	}
	
	public void testSyncLoginSuccess() throws Exception
	{
		Session session = this.mController.login("shujito", "shujito");
		assertNotNull(session);
		assertNotNull(session.id);
		assertNotNull(session.uid);
		assertEquals("/users", session.path);
	}
	
	public void testListUsers()
	{
		List<User> usersList = this.mController.getUsers();
		assertNotNull(usersList);
	}
	
	public void testListEmptyContacts()
	{
		Session dummy = new Session();
		List<Contact> contactsList = this.mController.getContacts(dummy);
		assertNotNull(contactsList);
		assertEquals(0, contactsList.size());
	}
	
	public void testListContacts() throws Exception
	{
		Session session = this.mController.login("shujito", "shujito");
		assertNotNull(session);
		assertNotNull(session.id);
		assertNotNull(session.uid);
		assertEquals("/users", session.path);
		List<Contact> contactsList = this.mController.getContacts(session);
		assertNotNull(contactsList);
		assertTrue(contactsList.size() > 0);
	}
	
	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}
}
