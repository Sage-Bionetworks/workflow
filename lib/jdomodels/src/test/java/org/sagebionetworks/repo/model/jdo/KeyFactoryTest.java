package org.sagebionetworks.repo.model.jdo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.sagebionetworks.repo.model.DatastoreException;


/**
 * @author deflaux
 *
 */
public class KeyFactoryTest {
	
	@Test
	public void testKeyToString() throws Exception {
		assertEquals("syn123", KeyFactory.keyToString(123L));
		assertEquals("syn0", KeyFactory.keyToString(0L));
		
		// We don't anticipate negative keys, but this class does not prevent them
		assertEquals("syn-9", KeyFactory.keyToString(-9L));
	}

	@Test
	public void testStringToKey() throws Exception {
		assertEquals(Long.valueOf(123L), KeyFactory.stringToKey("syn123"));
		assertEquals(Long.valueOf(0L), KeyFactory.stringToKey("syn0"));
		
		// Case should not matter
		assertEquals(Long.valueOf(123L), KeyFactory.stringToKey("SYN123"));
		assertEquals(Long.valueOf(0L), KeyFactory.stringToKey("SYN0"));
		
		// Whitespace should be ignored
		assertEquals(Long.valueOf(123L), KeyFactory.stringToKey("\tsyn123"));
		assertEquals(Long.valueOf(0L), KeyFactory.stringToKey("syn0   "));
		assertEquals(Long.valueOf(123L), KeyFactory.stringToKey(" syn123\n"));
		assertEquals(Long.valueOf(0L), KeyFactory.stringToKey("\nsyn0 \t"));

		// URLEncoded whitespace should be ignored
		assertEquals(Long.valueOf(123L), KeyFactory.stringToKey("+syn123+"));
		assertEquals(Long.valueOf(0L), KeyFactory.stringToKey("syn0%0d%0a"));
	}

	@Test(expected=DatastoreException.class)
	public void testStringToKeyInvalidPrefix() throws Exception {
		KeyFactory.stringToKey("foo123");
	}

	@Test(expected=DatastoreException.class)
	public void testStringToKeyNonNumericCharacters() throws Exception {
		KeyFactory.stringToKey("syn/123");
	}
}
