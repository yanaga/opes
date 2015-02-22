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

import com.google.common.collect.ComparisonChain;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Email implements Serializable, Comparable<Email> {

	private static final long serialVersionUID = 1L;

	private static final String ATOM = "[a-z0-9!#$%&'*+/=?^_`{|}~-]";

	private static final String DOMAIN = "(" + ATOM + "+(\\." + ATOM + "+)*";

	private static final String IP_DOMAIN = "\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\]";

	private static final Pattern pattern = Pattern.compile("^" + ATOM + "+(\\." + ATOM + "+)*@" + DOMAIN + "|"
			+ IP_DOMAIN + ")$", Pattern.CASE_INSENSITIVE);

	private final String value;

	private Email(String value) {
		this.value = value;
	}

	public static Email of(String value) {
		checkNotNull(value);
		Matcher m = pattern.matcher(value);
		checkArgument(m.matches());
		return new Email(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Email) {
			Email other = (Email) obj;
			return Objects.equals(this.value, other.value);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.value);
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public int compareTo(Email o) {
		return ComparisonChain.start().compare(this.value, o.value).result();
	}

}
