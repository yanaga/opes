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

import static org.assertj.core.api.Assertions.assertThat;

public class CnpjTest {

	@DataProvider(name = "cnpjsValidos")
	public static Object[][] cnpjsValidos() {
		return new Object[][] { { "19.861.350/0001-70" }, { "19861350000170" }, { "23.144.170/0001-45" }, { "23144170000145" },
				{ "46.868.328/0001-25" }, { "42.767.194/0001-03" }, { "58.647.246/0001-30" }, { "37.961.612/0001-50" },
				{ "43.256.675/0001-09" }, { "24.152.237/0001-56" }, { "60.871.888/0001-60" }, { "66.845.982/0001-20" },
				{ "06.074.614/0001-02" }, { "06074614000102" }, { "86.222.998/0001-94" }, { "74.345.224/0001-71" },
				{ "13.544.868/0001-02" }, { "23.325.412/0001-05" }, { "21.319.627/0001-80" }, { "36.747.518/0001-30" } };
	}

	@DataProvider(name = "cnpjsInvalidos")
	public static Object[][] cnpjsInvalidos() {
		return new Object[][] { { "06.305.901/0001-77" }, { "06305901000179" }, { "23.144.170/0001-35" }, { "23144170000155" },
				{ "46.868.328/0001-26" }, { "42.767.194/0001-13" }, { "58.647.246/0001-31" }, { "37.961.612/0001-51" },
				{ "43.256.675/0001-08" }, { "24.152.237/0001-57" }, { "60.871.888/0001-61" }, { "66.845.982/0001-21" },
				{ "06.074.614/0001-12" }, { "06074614000103" }, { "86.222.998/0001-93" }, { "74.345.224/0001-72" },
				{ "13.544.868/0001-03" }, { "23.325.412/0001-02" }, { "21.319.627/0001-82" }, { "36.747.518/0001-41" } };
	}

	@DataProvider(name = "numerosRepetidos")
	public static Object[][] numerosRepetidos() {
		return new Object[][] { { "00000000000000" }, { "11111111111111" }, { "22222222222222" }, { "33333333333333" },
				{ "44444444444444" }, { "55555555555555" }, { "66666666666666" }, { "77777777777777" },
				{ "88888888888888" }, { "99999999999999" } };
	}

	@Test(dataProvider = "cnpjsValidos")
	public void testOfValid(String s) {
		assertThat(Cnpj.of(s)).isNotNull();
	}

	@Test(dataProvider = "cnpjsInvalidos", expectedExceptions = IllegalArgumentException.class)
	public void testOfInvalid(String s) {
		Cnpj.of(s);
	}

	@Test(dataProvider = "numerosRepetidos", expectedExceptions = IllegalArgumentException.class)
	public void testRepetidos(String s) {
		Cnpj.of(s);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testOfWith10Digits() {
		Cnpj.of("0123456789");
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testOfWith12Digits() {
		Cnpj.of("012345678901");
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testOfWith15Digits() {
		Cnpj.of("012345678901234");
	}

	@Test
	public void testToString() {
		assertThat(Cnpj.of("19861350000170").toString()).isEqualTo("19861350000170");
	}

	@Test
	public void testFormatTo() {
		assertThat(String.format("%s", Cnpj.of("19861350000170"))).isEqualTo("19.861.350/0001-70");
	}

	@Test
	public void testFormatToAlternate() {
		assertThat(String.format("%#15s", Cnpj.of("19861350000170"))).isEqualTo("019861350000170");
	}

	@Test
	public void testEquals() {
		assertThat(Cnpj.of("19.861.350/0001-70")).isEqualTo(Cnpj.of("19861350000170"));
	}

	@Test
	public void testCompareTo() {
		assertThat(Cnpj.of("23144170000145").compareTo(Cnpj.of("19861350000170")) > 0).isTrue();
		assertThat(Cnpj.of("19861350000170").compareTo(Cnpj.of("19861350000170")) == 0).isTrue();
	}

}
