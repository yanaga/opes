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
import com.google.common.collect.Ordering;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Formattable;
import java.util.Formatter;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;


public class CpfCnpj implements Serializable, Formattable, Comparable<CpfCnpj> {

	private static final long serialVersionUID = 1L;

	private final Optional<Cpf> cpf;

	private final Optional<Cnpj> cnpj;

	private CpfCnpj(Cpf cpf) {
		this.cpf = Optional.of(cpf);
		this.cnpj = Optional.empty();
	}

	private CpfCnpj(Cnpj cnpj) {
		this.cpf = Optional.empty();
		this.cnpj = Optional.of(cnpj);
	}

	public static CpfCnpj of(String value) {
		checkNotNull(value);
		String digits = value.replaceAll("\\D", "");
		if (digits.matches("\\d{11}")) {
			return new CpfCnpj(Cpf.of(digits));
		}
		else if (digits.matches("\\d{14}")) {
			return new CpfCnpj(Cnpj.of(digits));
		}
		throw new IllegalArgumentException(String.format("CPF/CNPJ inv\u00e1lido: %s.", value));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CpfCnpj) {
			CpfCnpj other = (CpfCnpj) obj;
			return Objects.equal(this.cpf, other.cpf) && Objects.equal(this.cnpj, other.cnpj);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.cpf, this.cnpj);
	}

	@Override
	public String toString() {
		return cpf.map(Cpf::toString).orElseGet(() -> cnpj.map(Cnpj::toString).get());
	}

	@Override
	public void formatTo(Formatter formatter, int flags, int width, int precision) {
		cpf.ifPresent(cpf -> cpf.formatTo(formatter, flags, width, precision));
		cnpj.ifPresent(cnpj -> cnpj.formatTo(formatter, flags, width, precision));
	}

	@Override
	public int compareTo(CpfCnpj o) {
		return ComparisonChain.start()
				.compare(this.cpf.orElse(null), o.cpf.orElse(null), Ordering.natural().nullsLast())
				.compare(this.cnpj.orElse(null), o.cnpj.orElse(null), Ordering.natural().nullsLast())
				.result();
	}

	public boolean isCpf() {
		return cpf.isPresent();
	}

	public boolean isCnpj() {
		return cnpj.isPresent();
	}

	public Optional<Cpf> getCpf() {
		return cpf;
	}

	public Optional<Cnpj> getCnpj() {
		return cnpj;
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		throw new InvalidObjectException("Proxy required");
	}

	private Object writeReplace() {
		return new SerializationProxy(this.toString());
	}

	private static class SerializationProxy implements Serializable {

		private final String value;

		private SerializationProxy(String value) {
			this.value = value;
		}

		private Object readResolve() {
			return CpfCnpj.of(value);
		}

	}

}
