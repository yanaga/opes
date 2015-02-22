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
import java.util.Formattable;
import java.util.Formatter;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.FormattableFlags.ALTERNATE;
import static java.util.FormattableFlags.LEFT_JUSTIFY;

public class Cnpj implements Serializable, Formattable, Comparable<Cnpj> {

	private static final long serialVersionUID = 1L;

	private final String value;

	private Cnpj(String value) {
		this.value = value;
	}

	public static Cnpj of(String value) {
		checkNotNull(value);
		String digits = value.replaceAll("\\D", "");
		checkArgument(digits.matches("\\d{14}"));
		checkArgument(!digits.matches("(\\d)\\1+"));
		checkArgument(isValid(digits.substring(0, 13)));
		checkArgument(isValid(digits));
		return new Cnpj(digits);
	}

	static boolean isValid(String digits) {
		if (Long.parseLong(digits) % 10 == 0) {
			return somaPonderada(digits) % 11 < 2;
		}
		else {
			return somaPonderada(digits) % 11 == 0;
		}
	}

	static int somaPonderada(String digits) {
		char[] cs = digits.toCharArray();
		int soma = 0;
		for (int i = 0; i < cs.length; i++) {
			int index = cs.length - i - 1;
			soma += Character.digit(cs[i], 10) * ((index % 9 + 1) + (index / 9));
		}
		return soma;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Cnpj) {
			Cnpj other = (Cnpj) obj;
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
		return this.value;
	}

	@Override
	public void formatTo(Formatter formatter, int flags, int width, int precision) {
		StringBuilder sb = new StringBuilder();
		boolean alternate = (flags & ALTERNATE) == ALTERNATE;
		if (alternate) {
			sb.append(value);
		}
		else {
			sb.append(String.format("%s.%s.%s/%s-%s", value.substring(0, 2), value.substring(2, 5),
					value.substring(5, 8), value.substring(8, 12), value.substring(12)));
		}
		int length = sb.length();
		if (length < width) {
			for (int i = 0; i < width - length; i++) {
				if (alternate) {
					sb.insert(0, '0');
				}
				else {
					boolean leftJustified = (flags & LEFT_JUSTIFY) == LEFT_JUSTIFY;
					if (leftJustified) {
						sb.append(' ');
					}
					else {
						sb.insert(0, ' ');
					}
				}
			}
		}
		formatter.format(sb.toString());
	}

	@Override
	public int compareTo(Cnpj o) {
		return ComparisonChain.start().compare(this.value, o.value).result();
	}

}
