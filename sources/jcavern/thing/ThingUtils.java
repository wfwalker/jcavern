
package jcavern.thing;

import java.io.*;
import java.util.*;
import jcavern.*;

/**
 * ThingUtils provides a set of utility classes for serializing Things.
 */
public class ThingUtils
{
	/**
	 * Converts this Thing into a byte array.
	 *
	 * @param		inThing						a non-null Thing to convert into a byte array
	 * @return									a non-null array of bytes
	 * @exception	JCavernInternalError		could not serialize this Thing
	 */
	public static byte[] thingToByteArray(Thing inThing) throws JCavernInternalError
	{
		ByteArrayOutputStream someBytes = new ByteArrayOutputStream();

		try
		{
			ObjectOutputStream aStream = new ObjectOutputStream(someBytes);
			aStream.writeObject(inThing);
			aStream.flush();
			
		}
		catch(IOException ioe)
		{
			throw new JCavernInternalError("can't serialize " + ioe);
		}
	
		return someBytes.toByteArray();	
	}

	/**
	 * Converts a byte array into a printable String.
	 *
	 * @param	inBytes		a non-null array of bytes
	 * @return				a non-null String encoding the bytes in hexidecimal
	 */
	public static String byteArrayToDataString(byte[] inBytes)
	{
		StringBuffer playerDataString = new StringBuffer();
		
		for (int byteIndex = 0; byteIndex < inBytes.length; byteIndex++)
		{
			playerDataString.append(" ");

			if (inBytes[byteIndex] >= 0)
			{
				playerDataString.append(Integer.toHexString(inBytes[byteIndex]).toUpperCase());
			}
			else
			{
				playerDataString.append(Integer.toHexString(inBytes[byteIndex]).toUpperCase().substring(6));
			}
		}
		
		return playerDataString.toString();
	}

	/**
	 * Converts a byte array into a printable String.
	 *
	 * @param	inBytes		a non-null array of bytes
	 * @return				a non-null String encoding the bytes in hexidecimal
	 */
	public static byte[] dataStringToByteArray(String inDataString) throws JCavernInternalError
	{
		StringTokenizer aTokenizer = new StringTokenizer(inDataString);
		int				index = 0;
		byte[]			theBytes = new byte[aTokenizer.countTokens()];

		while (aTokenizer.hasMoreTokens())
		{
			String	aToken = aTokenizer.nextToken();
			theBytes[index] = (byte) Integer.parseInt(aToken, 16);
			index++;
		}
		
		return theBytes;
	}

	/**
	 * Converts a byte array back into a Thing.
	 *
	 * @param		theBytes				a non-null array of bytes
	 * @return								a non-null Thing
	 * @exception	JCavernInternalError	byte array did not encode a Thing
	 */
	public static Thing byteArrayToThing(byte[] theBytes) throws JCavernInternalError
	{
		ByteArrayInputStream	aByteStream = null;
		ObjectInputStream		anObjectStream = null;
		Object					aThing = null;

		try
		{
			aByteStream = new ByteArrayInputStream(theBytes);
			anObjectStream = new ObjectInputStream(aByteStream);
			aThing = anObjectStream.readObject();

		}
		catch(StreamCorruptedException sce)
		{
			throw new JCavernInternalError("Thing.fromDataString " + sce);
		}
		catch(IOException ioe)
		{
			throw new JCavernInternalError("Thing.fromDataString " + ioe);
		}
		catch(ClassNotFoundException cnfe)
		{
			throw new JCavernInternalError("Thing.fromDataString " + cnfe);
		}
		
		return (Thing) aThing;
	}
}

