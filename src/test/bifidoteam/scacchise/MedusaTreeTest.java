package test.bifidoteam.scacchise;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import com.bifidoteam.util.MedusaTree;

public class MedusaTreeTest
{
	private MedusaTree mt;
	
	@Before
	public void fixiture()
	{
		mt = new MedusaTree();
	}
	
	@Test
	public void hasNextBranch()
	{
		mt.AddBranch(0);
		mt.AddBranch(1);
		
		Iterator<Integer> mtIterator = mt.iterator();
		
		assertTrue(mtIterator.hasNext());
	}
	
	// convention: use "should" and explanatory names
//		@Test
//		public void nextOfNewYearsEveShouldBeNewYearsDay() {
//			nye.next();
//
//			assertEquals
//				("next of new year's eve must be new year's day",
//				nye, nyd);
//		}
}
