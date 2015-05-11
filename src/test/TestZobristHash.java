package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import wuziqi.GameState;
import wuziqi.ZobristHash;

public class TestZobristHash {
	public ZobristHash zh;
	@Before
	public void init() {
		zh = new ZobristHash(144, 3);
	}

	@Test
	public void testHash(){
		
		GameState g = new GameState(12,12,0);
		GameState g1 = new GameState(12,12,0);
		//System.out.println(zh.hash(g));
		//System.out.println(zh.hash(g1));
		assertEquals(zh.hash(g),zh.hash(g1));
		g.addPiece(5, 5);
		g1.addPiece(5, 5);
		g1.setCurrSide(1-g.getCurrSide());
		//System.out.println(zh.hash(g));
		//System.out.println(zh.hash(g1));
		assertEquals(zh.hash(g),zh.hash(g1));
		
	}
}
