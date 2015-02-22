package me.yanaga.opes;

/*
 * #%L
 * opes
 * %%
 * Copyright (C) 2015 Edson Yanaga
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class CnsTest {

	@DataProvider(name = "cnssValidos")
	public static Object[][] cnssValidos() {
		return new Object[][] {
				{ "898000000043208" }, { "190129759240018" }, { "898001038954985" }, { "100113736920003" }, { "898003701430618" },
				{ "898003701418022" }, { "898000752885083" }, { "898003708105771" }, { "201597135300005" }, { "801434364369875" },
				{ "207015780540004" }, { "209845719350003" }, { "209868015390000" }, { "801434363411304" }, { "706805267673923" },
				{ "700805904091181" }, { "210226354000008" }, { "203478579780008" }, { "206139627900002" }, { "170121420980005" },
				{ "209845737170003" }, { "102274884040008" }, { "102528846360018" }, { "125214400290018" }, { "123203468660018" },
				{ "170151402820018" }, { "108377694740018" }, { "122857340360018" } };
	}

	@DataProvider(name = "numerosRepetidos")
	public static Object[][] numerosRepetidos() {
		return new Object[][] { { "000000000000000" }, { "111111111111111" }, { "222222222222222" }, { "333333333333333" },
				{ "777777777777777" }, { "888888888888888" }, { "999999999999999" } };
	}

	@Test(dataProvider = "cnssValidos")
	public void testOf(String s) {
		Cns.of(s);
	}

	@Test(dataProvider = "numerosRepetidos", expectedExceptions = IllegalArgumentException.class)
	public void testIsRepetido(String s) {
		Cns.of(s);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testOfDefinitivoInvalido() {
		assertNotNull(Cns.of("190129759240017"));
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testOfProvisorioInvalido() {
		assertNotNull(Cns.of("190129759240015"));
	}

	@Test
	public void testFormatTo() {
		assertEquals("190 1297 5924 0018", String.format("%s", Cns.of("190129759240018")));
		assertEquals("898 0010 3895 4985", String.format("%s", Cns.of("898001038954985")));
	}

	@Test
	public void testFormatToAlternate() {
		assertEquals("00000190129759240018", String.format("%#20s", Cns.of("190129759240018")));
	}

}
