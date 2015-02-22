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

public class CpfTest {

	@DataProvider(name = "cpfsValidos")
	public static Object[][] cpfsValidos() {
		return new Object[][] { { "185.302.491-00" }, { "18530249100" }, { "297.276.931-72" }, { "29727693172" }, { "046.428.359-03" },
				{ "04642835903" }, { "023.750.169-47" }, { "02375016947" }, { "855.826.525-90" }, { "669.712.265-00" }, { "01646227212" },
				{ "96183390259" }, { "63118670282" }, { "57212970263" }, { "85272175204" }, { "84250739287" }, { "74390651234" },
				{ "93582803287" }, { "84569190200" }, { "51914794249" }, { "67681530215" }, { "51918102287" }, { "59925272220" },
				{ "72178507204" }, { "85542520200" }, { "98089242200" }, { "66100313200" }, { "51405300230" }, { "13187110703" } };
	}

	@DataProvider(name = "cpfsInvalidos")
	public static Object[][] cpfsInvalidos() {
		return new Object[][] { { "005.333.839-18" }, { "00533383910" }, { "030.405.039-35" }, { "03040503934" }, { "046.428.359-02" },
				{ "04642835913" }, { "023.750.169-57" }, { "02375016937" }, { "855.826.525-91" }, { "669.712.265-10" }, { "01646227211" },
				{ "96183390269" }, { "63118670283" }, { "57212970273" }, { "85272175206" }, { "84250739284" }, { "74390651233" },
				{ "93582803297" }, { "84569190201" }, { "51914794259" }, { "67681530214" }, { "51918102282" }, { "59925272221" },
				{ "72178507294" }, { "85542520210" }, { "98089242210" }, { "66100313201" }, { "51405300238" }, { "13187110704" } };
	}

	@DataProvider(name = "numerosRepetidos")
	public static Object[][] numerosRepetidos() {
		return new Object[][] { { "00000000000" }, { "11111111111" }, { "22222222222" }, { "33333333333" }, { "44444444444" },
				{ "55555555555" }, { "66666666666" }, { "77777777777" }, { "88888888888" }, { "99999999999" } };
	}

	@Test(dataProvider = "cpfsValidos")
	public void testOfValid(String s) {
		assertThat(Cpf.of(s)).isNotNull();
	}

	@Test(dataProvider = "cpfsInvalidos", expectedExceptions = IllegalArgumentException.class)
	public void testOfInvalid(String s) {
		Cpf.of(s);
	}

	@Test(dataProvider = "numerosRepetidos", expectedExceptions = IllegalArgumentException.class)
	public void testRepetidos(String s) {
		Cpf.of(s);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testOfWith10Digits() {
		Cpf.of("0123456789");
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testOfWith12Digits() {
		Cpf.of("012345678901");
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testOfWith15Digits() {
		Cpf.of("012345678901234");
	}

	@Test
	public void testToString() {
		assertThat(Cpf.of("18530249100").toString()).isEqualTo("18530249100");
		assertThat(Cpf.of("29727693172").toString()).isEqualTo("29727693172");
	}

	@Test
	public void testFormatTo() {
		assertThat(String.format("%s", Cpf.of("18530249100"))).isEqualTo("185.302.491-00");
		assertThat(String.format("%s", Cpf.of("29727693172"))).isEqualTo("297.276.931-72");
	}

	@Test
	public void testFormatToAlternate() {
		assertThat(String.format("%#15s", Cpf.of("18530249100"))).isEqualTo("000018530249100");
	}

	@Test
	public void testEquals() {
		assertThat(Cpf.of("18530249100")).isEqualTo(Cpf.of("18530249100"));
	}

	@Test
	public void testCompareTo() {
		assertThat(Cpf.of("29727693172").compareTo(Cpf.of("18530249100")) > 0).isTrue();
		assertThat(Cpf.of("29727693172").compareTo(Cpf.of("29727693172")) == 0).isTrue();
	}

}
