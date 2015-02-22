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

import com.google.common.collect.Range;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class CepTest {

	@Test
	public void testOf() {
		Cep cep = Cep.of("87030-020");
		assertNotNull(cep);
		assertEquals("87030020", cep.toString());
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testFromInvalidString() {
		Cep.of("8703002");
	}

	@Test
	public void testFormatToAlternate() {
		Cep cep = Cep.of("87030-020");
		assertEquals("87030020", String.format("%#s", cep));
	}

	@Test
	public void testFormatTo() {
		Cep cep = Cep.of("87030-020");
		assertEquals("87030-020", String.format("%s", cep));
	}

	@Test
	public void testCompareTo() {
		Cep inicio = Cep.of("80000000");
		Cep termino = Cep.of("87999999");
		assertTrue(inicio.compareTo(termino) < 0);
		assertTrue(termino.compareTo(inicio) > 0);
		assertTrue(inicio.compareTo(inicio) == 0);

		assertTrue(Range.closed(inicio, termino).contains(Cep.of("87030020")));
		assertFalse(Range.closed(inicio, termino).contains(Cep.of("88000000")));
		assertFalse(Range.closed(inicio, termino).contains(Cep.of("79999999")));
	}

}
