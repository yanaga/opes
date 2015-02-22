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

import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class CpfCnpjTest {

	@Test(dataProviderClass = CpfTest.class, dataProvider = "cpfsValidos")
	public void testFromValidCpfs(String s) {
		assertNotNull(CpfCnpj.of(s));
	}

	@Test(dataProviderClass = CnpjTest.class, dataProvider = "cnpjsValidos")
	public void testFromValidCnpjs(String s) {
		assertNotNull(CpfCnpj.of(s));
	}

	@Test(dataProviderClass = CpfTest.class, dataProvider = "cpfsInvalidos", expectedExceptions = IllegalArgumentException.class)
	public void testFromInvalidCpfs(String s) {
		CpfCnpj.of(s);
	}

	@Test(dataProviderClass = CnpjTest.class, dataProvider = "cnpjsInvalidos", expectedExceptions = IllegalArgumentException.class)
	public void testFromInvalidCnpjs(String s) {
		CpfCnpj.of(s);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testOfWith10Digits() {
		CpfCnpj.of("0123456789");
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testOfWith12Digits() {
		CpfCnpj.of("012345678901");
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testOfWith15Digits() {
		CpfCnpj.of("012345678901234");
	}

	@Test
	public void testEquals() {
		assertEquals(CpfCnpj.of("18530249100"), CpfCnpj.of("18530249100"));
	}

	@Test
	public void testCompareTo() {
		assertTrue(CpfCnpj.of("29727693172").compareTo(CpfCnpj.of("18530249100")) > 0);
		assertTrue(CpfCnpj.of("18530249100").compareTo(CpfCnpj.of("29727693172")) < 0);
		assertTrue(CpfCnpj.of("18530249100").compareTo(CpfCnpj.of("18530249100")) == 0);
		assertTrue(CpfCnpj.of("19861350000170").compareTo(CpfCnpj.of("00881753000153")) > 0);
	}

	@Test
	public void testIsCpf() {
		assertThat(CpfCnpj.of("29727693172").isCpf()).isTrue();
		assertThat(CpfCnpj.of("19861350000170").isCpf()).isFalse();
	}

	@Test
	public void testIsCnpj() {
		assertThat(CpfCnpj.of("19861350000170").isCnpj()).isTrue();
		assertThat(CpfCnpj.of("29727693172").isCnpj()).isFalse();
	}

	@Test
	public void testGetCpf() {
		assertThat(CpfCnpj.of("29727693172").getCpf().isPresent()).isTrue();
		assertThat(CpfCnpj.of("29727693172").getCpf().get()).isEqualTo(Cpf.of("29727693172"));
		assertThat(CpfCnpj.of("19861350000170").getCpf().isPresent()).isFalse();
	}

	@Test
	public void testGetCnpj() {
		assertThat(CpfCnpj.of("19861350000170").getCnpj().isPresent()).isTrue();
		assertThat(CpfCnpj.of("19861350000170").getCnpj().get()).isEqualTo(Cnpj.of("19861350000170"));
		assertThat(CpfCnpj.of("29727693172").getCnpj().isPresent()).isFalse();
	}

	@Test
	public void testSerialization() throws IOException, ClassNotFoundException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		CpfCnpj cpfCnpj1 = CpfCnpj.of("00881753000153");
		oos.writeObject(cpfCnpj1);
		oos.flush();
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
		CpfCnpj cpfCnpj2 = (CpfCnpj) ois.readObject();
		assertThat(cpfCnpj1).isEqualTo(cpfCnpj2);
	}

}
