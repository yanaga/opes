# opes
[![Build Status](https://travis-ci.org/yanaga/opes.svg?branch=master)](https://travis-ci.org/yanaga/opes)

Brazilian Value Objects (VO)s for common use cases.

We are constantly solving the same problems again and again in every project that uses some common domain model. In Brazil we have some abstractions that are used everywhere. Every developer probably has (or at least should have) an implementation for CPF, CNPJ, CEP etc.

**opes** is an attempt to aid developers in Brazil to focus on more important problems, rather than implementing this kind of code one more time. **opes** is tested and used in production code in many projects, and hopefully will be in your project from now on.

We currently have implementations for CPF, CNPJ, CPF/CNPJ, CEP and CNS.

**opes** is available on [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22opes%22).

You can add this dependency to your Maven project:

```xml
<dependency>
  <groupId>me.yanaga</groupId>
  <artifactId>opes</artifactId>
  <version>1.0.1</version>
</dependency>
```

Or if you are using Gradle:

```groovy
compile "me.yanaga:opes:1.0.1"
```

#opes (versão em português)

Value Objects (VO)s para casos de uso comuns no Brasil.

Nós estamos constantemente resolvendo os mesmos problemas em cada projeto que utiliza um modelo de negócios comum. No Brasil nós temos algumas abstrações que simplesmente são utilizadas em todo lugar. Todo desenvolvedor acaba tendo (ou ao menos deveria ter) uma implementação própria de CPF, CNPJ, CEP etc.

O **opes** é uma tentativa de ajudar desenvolvedores no Brasil a focar em problemas mais importantes, ao invés de perder tempo implementando esse tipo de código mais uma vez. O **opes** é testado e utilizado em produção em vários projetos, e temos a esperança de que ele poderá ajudar e será utilizado em seu próximo projeto também.

Atualmente temos implementações para CPF, CNPJ, CPF/CNPJ, CEP e CNS. Mas a lista continua crescendo.

O **opes** está disponível no [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22opes%22).

Você pode utilizar esta dependência do Maven em seu projeto:

```xml
<dependency>
  <groupId>me.yanaga</groupId>
  <artifactId>opes</artifactId>
  <version>1.0.1</version>
</dependency>
```

Ou se você utiliza Gradle:

```groovy
compile "me.yanaga:opes:1.0.1"
```
