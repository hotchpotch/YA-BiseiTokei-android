package in.hotchpotch.android.yabiseitokei;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestUtils {

	@Test
	public void testDetectAct() {
        // masumin
		assertEquals(Utils.detectAct("0056"), 11);
		assertEquals(Utils.detectAct("0100"), 11);
        
        // ai nonaka
		assertEquals(Utils.detectAct("0000"), 0);
		assertEquals(Utils.detectAct("0001"), 0);
		assertEquals(Utils.detectAct("1155"), 0);
		assertEquals(Utils.detectAct("1200"), 0);
		assertEquals(Utils.detectAct("1204"), 0);
		assertEquals(Utils.detectAct("1936"), 0);
		assertEquals(Utils.detectAct("2359"), 0);

        // ryoko shiraishi
		assertEquals(Utils.detectAct("0654"), 4);
		assertEquals(Utils.detectAct("1325"), 4);
		assertEquals(Utils.detectAct("1430"), 4);
		assertEquals(Utils.detectAct("1644"), 4);

        // ito kanae
		assertEquals(Utils.detectAct("0455"), 7);
		assertEquals(Utils.detectAct("0500"), 7);
		assertEquals(Utils.detectAct("0504"), 7);
		assertEquals(Utils.detectAct("1655"), 7);
		assertEquals(Utils.detectAct("1700"), 7);
		assertEquals(Utils.detectAct("1704"), 7);
		assertEquals(Utils.detectAct("2334"), 7);
        
	}

}
