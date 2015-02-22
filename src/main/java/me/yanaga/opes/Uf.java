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

import java.util.Map;
import java.util.stream.Stream;

import static me.yanaga.guava.stream.MoreCollectors.toImmutableMap;

public enum Uf {

	AC(12, "Acre"),

	AL(27, "Alagoas"),

	AM(13, "Amazonas"),

	AP(16, "Amap\u00e1"),

	BA(29, "Bahia"),

	CE(23, "Cear\u00e1"),

	DF(53, "Distrito Federal"),

	ES(32, "Esp\u00edrito Santo"),

	GO(52, "Goi\u00e1s"),

	MA(21, "Maranh\u00e3o"),

	MG(31, "Minas Gerais"),

	MS(50, "Mato Grosso do Sul"),

	MT(51, "Mato Grosso"),

	PA(15, "Par\u00e1"),

	PB(25, "Para\u00edba"),

	PE(26, "Pernambuco"),

	PI(22, "Piau\u00ed"),

	PR(41, "Paran\u00e1"),

	RJ(33, "Rio de Janeiro"),

	RN(24, "Rio Grande do Norte"),

	RO(11, "Rond\u00f4nia"),

	RR(14, "Roraima"),

	RS(43, "Rio Grande do Sul"),

	SC(42, "Santa Catarina"),

	SE(28, "Sergipe"),

	SP(35, "S\u00e3o Paulo"),

	TO(17, "Tocantins"),

	EX(0, "Exterior");

	private static final Map<Integer, Uf> valueMap = Stream.of(values()).collect(toImmutableMap(i -> i.codigoIbge, i -> i));

	public static Uf of(Integer codigo) {
		return valueMap.get(codigo);
	}

	private final String nome;

	private final int codigoIbge;

	private Uf(int codigoIbge, String nome) {
		this.codigoIbge = codigoIbge;
		this.nome = nome;
	}

	public int getCodigoIbge() {
		return codigoIbge;
	}

	public String getNome() {
		return nome;
	}

}
