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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class EmailTest {

	@Test
	public void testOf() {
		assertNotNull(Email.of("edson@yanaga.yanaga"));
		assertNotNull(Email.of("teste.domain@yanaga.me"));
		assertNotNull(Email.of("user123_321@yanaga.me"));
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testOfWithoutUser() {
		Email.of("@yanaga.me");
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testOfWithoutDomain() {
		Email.of("user@");
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testOfWithoutAt() {
		Email.of("user.teste");
	}

	@Test
	public void testToString() {
		assertEquals("edson@yanaga.yanaga", Email.of("edson@yanaga.yanaga").toString());
	}

}
