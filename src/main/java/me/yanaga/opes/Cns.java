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

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

import java.io.Serializable;
import java.util.Formattable;
import java.util.Formatter;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.FormattableFlags.ALTERNATE;
import static java.util.FormattableFlags.LEFT_JUSTIFY;

public class Cns implements Serializable, Formattable, Comparable<Cns> {

	private static final long serialVersionUID = 1L;

	private final String value;

	private Cns(String value) {
		this.value = value;
	}

	public static Cns of(String value) {
		checkNotNull(value);
		String digits = value.replaceAll("\\D", "");
		checkArgument(digits.matches("\\d{15}"));
		checkArgument(!digits.matches("(\\d)\\1+"));
		checkArgument(isValid(digits));
		return new Cns(digits);
	}

	static boolean isValid(String s) {
		if (s.matches("[1-2]\\d{10}00[0-1]\\d") || s.matches("[7-9]\\d{14}")) {
			return somaPonderada(s) % 11 == 0;
		}
		return false;
	}

	static int somaPonderada(String s) {
		checkArgument(s.length() <= 15);
		char[] cs = s.toCharArray();
		int soma = 0;
		for (int i = 0; i < cs.length; i++) {
			soma += Character.digit(cs[i], 10) * (15 - i);
		}
		return soma;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Cpf) {
			Cns other = (Cns) obj;
			return Objects.equal(this.value, other.value);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.value);
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public void formatTo(Formatter formatter, int flags, int width, int precision) {
		StringBuilder sb = new StringBuilder();
		boolean alternate = (flags & ALTERNATE) == ALTERNATE;
		if (alternate) {
			sb.append(value);
		}
		else {
			sb.append(String.format("%s %s %s %s", value.substring(0, 3), value.substring(3, 7),
					value.substring(7, 11), value.substring(11)));
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
	public int compareTo(Cns o) {
		return ComparisonChain.start().compare(this.value, o.value).result();
	}

}
