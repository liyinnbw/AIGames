import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


public class TestRunner {

	@Test
	public void testGetHoleCount() {
		PlayerSkeleton ps = new PlayerSkeleton();
		int[][] field = {
				{0, 0, 1, 1, 0, 0, 1, 0, 0, 1 },
				{1, 1, 1, 0, 0, 0, 1, 1, 0, 1 },
				{1, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
				{1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }
		};
		int result = ps.getHoleCount(field);
		System.out.println(result);
		assertTrue(result==2);
	}
	

}
