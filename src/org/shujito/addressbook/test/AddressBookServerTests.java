package org.shujito.addressbook.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.shujito.addressbook.AddressBookApplication;
import org.shujito.addressbook.controller.AddressBookApiController;
import org.shujito.addressbook.model.Result;
import org.shujito.addressbook.model.Session;

import android.test.ActivityTestCase;

public class AddressBookServerTests extends ActivityTestCase
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
	
	public void testLoginMissingUsername()
	{
		assertEquals("Username required", this.mController.login(null, "no password"));
	}
	
	public void testLoginMissingPassword()
	{
		assertEquals("Password required", this.mController.login("no username", null));
	}
	
	public void testLoginRequiredFields()
	{
		assertEquals("Username and password required", this.mController.login(null, null));
	}
	
	public void testLoginFailed() throws Exception
	{
		final CountDownLatch latch = new CountDownLatch(1);
		String message = this.mController.login("unregistered username", "bad password", new AddressBookApiController.LoginCallback()
		{
			public void onLoginSuccess(Session login)
			{
				fail("The test should not get here");
			}
			
			public void onLoginFailure(Result result)
			{
				assertEquals("bad credentials", result.message);
				assertEquals(401, result.status);
				assertEquals(1, latch.getCount());
				latch.countDown();
			}
		});
		assertNull(message);
		assertTrue(latch.await(10000, TimeUnit.MILLISECONDS));
		assertEquals(0, latch.getCount());
	}
	
	public void testLoginSuccess() throws Exception
	{
		final CountDownLatch latch = new CountDownLatch(1);
		String message = this.mController.login("shujito", "shujito", new AddressBookApiController.LoginCallback()
		{
			public void onLoginSuccess(Session login)
			{
				assertNotNull(login.id);
				assertNotNull(login.uid);
				assertEquals("/users", login.path);
				assertEquals(1, latch.getCount());
				latch.countDown();
			}
			
			public void onLoginFailure(Result result)
			{
				fail("The test should not get here");
			}
		});
		assertNull(message);
		assertTrue(latch.await(10000, TimeUnit.MILLISECONDS));
		assertEquals(0, latch.getCount());
	}
	
	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}
}
