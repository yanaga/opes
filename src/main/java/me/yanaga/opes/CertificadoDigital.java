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

import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DLSequence;
import org.bouncycastle.x509.extension.X509ExtensionUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.crypto.AlgorithmMethod;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.KeySelectorException;
import javax.xml.crypto.KeySelectorResult;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class CertificadoDigital implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String C14N_TRANSFORM_METHOD = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";

	private static final char[] DEFAULT_CERTIFICATE_PASSWORD = { 'o', 'p', 'e', 's' };

	private static final ASN1ObjectIdentifier OID_CNPJ = new ASN1ObjectIdentifier("2.16.76.1.3.3");

	private final transient byte[] bytes;

	private final transient CpfCnpj cnpj;

	private final Instant expiry;

	private final transient PrivateKey privateKey;

	private final transient X509Certificate[] certificateChain;

	private CertificadoDigital(byte[] bytes, CpfCnpj cpfCnpj, Instant expiry, PrivateKey privateKey, X509Certificate[] certificateChain) {
		this.bytes = bytes;
		this.cnpj = cpfCnpj;
		this.expiry = expiry;
		this.privateKey = privateKey;
		this.certificateChain = certificateChain;

	}

	public static CertificadoDigital of(InputStream in) {
		return of(in, DEFAULT_CERTIFICATE_PASSWORD);
	}

	public static CertificadoDigital of(InputStream in, char[] password) {
		try {
			KeyStore keyStore = KeyStore.getInstance("pkcs12");
			keyStore.load(in, password);

			for (Enumeration<String> aliases = keyStore.aliases(); aliases.hasMoreElements(); ) {
				String alias = aliases.nextElement();
				if (!keyStore.isKeyEntry(alias)) {
					continue;
				}
				Key key = keyStore.getKey(alias, password);
				if (!(key instanceof PrivateKey)) {
					continue;
				}
				Certificate[] certs = keyStore.getCertificateChain(alias);
				if ((certs == null) || (certs.length == 0) || !(certs[0] instanceof X509Certificate)) {
					continue;
				}
				if (!(certs instanceof X509Certificate[])) {
					Certificate[] tmp = new X509Certificate[certs.length];
					System.arraycopy(certs, 0, tmp, 0, certs.length);
					certs = tmp;
				}
				Instant expiry = ((X509Certificate) certs[0]).getNotAfter().toInstant();

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				keyStore.store(baos, DEFAULT_CERTIFICATE_PASSWORD);

				return new CertificadoDigital(baos.toByteArray(), extractCnpj(certs), expiry, (PrivateKey) key, (X509Certificate[]) certs);
			}
			throw new IllegalArgumentException("Unable to load PrivateKey and CertificateChain from KeyStore.");
		}
		catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	public byte[] toBytes() {
		return bytes;
	}

	public Optional<CpfCnpj> getCnpj() {
		return Optional.ofNullable(cnpj);
	}

	public Instant getExpiry() {
		return expiry;
	}

	public <T extends Node> T sign(T node) {
		checkNotNull(node);
		checkArgument(node instanceof Document || node instanceof Element);
		try {
			Element element = node instanceof Document ? ((Document) node).getDocumentElement() : (Element) node;
			DOMSignContext dsc = new DOMSignContext(privateKey, element);
			XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");

			List<Transform> transformList = new LinkedList<>();
			transformList.add(signatureFactory.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null));
			transformList.add(signatureFactory.newTransform(C14N_TRANSFORM_METHOD, (TransformParameterSpec) null));

			Node child = findFirstElementChild(element);
			((Element) child).setIdAttribute("Id", true);

			String id = child.getAttributes().getNamedItem("Id").getNodeValue();
			String uri = String.format("#%s", id);
			Reference reference = signatureFactory.newReference(uri,
					signatureFactory.newDigestMethod(DigestMethod.SHA1, null), transformList, null, null);

			SignedInfo signedInfo = signatureFactory.newSignedInfo(signatureFactory.newCanonicalizationMethod(
					CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null), signatureFactory
					.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(reference));

			KeyInfoFactory kif = signatureFactory.getKeyInfoFactory();
			X509Data x509Data = kif.newX509Data(Collections.singletonList(certificateChain[0]));
			KeyInfo keyInfo = kif.newKeyInfo(Collections.singletonList(x509Data));

			XMLSignature xmlSignature = signatureFactory.newXMLSignature(signedInfo, keyInfo);

			xmlSignature.sign(dsc);

			return node;
		}
		catch (Exception ex) {
			throw new IllegalArgumentException("Erro ao assinar XML.", ex);
		}
	}

	public boolean validate(Document document) {
		checkNotNull(document);
		try {
			NodeList nl = document.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
			DOMValidateContext valContext = new DOMValidateContext(new X509KeySelector(), nl.item(0));

			XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");
			XMLSignature signature = signatureFactory.unmarshalXMLSignature(valContext);

			return signature.validate(valContext);
		}
		catch (Exception ex) {
			throw new IllegalArgumentException("Erro ao validar o XML.", ex);
		}
	}

	private Node findFirstElementChild(Node node) {
		NodeList childNodes = node.getChildNodes();
		Node child = null;
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node childNode = childNodes.item(i);
			if (childNode.getNodeType() == Node.ELEMENT_NODE) {
				child = childNode;
				break;
			}
		}
		return child;
	}

	private static CpfCnpj extractCnpj(Certificate[] certs) {
		X509Certificate[] certificates = (X509Certificate[]) certs;
		for (X509Certificate certificate : certificates) {
			try {
				for (Object obj : X509ExtensionUtil.getSubjectAlternativeNames(certificate)) {
					if (obj instanceof List) {
						List values = (List) obj;
						if (values.get(1) instanceof DLSequence) {
							DLSequence seq = (DLSequence) values.get(1);

							ASN1ObjectIdentifier oid = (ASN1ObjectIdentifier) seq.getObjectAt(0);
							if (OID_CNPJ.equals(oid)) {
								ASN1TaggedObject tagged = (ASN1TaggedObject) seq.getObjectAt(1);
								ASN1Object derObj = tagged.getObject();
								try {
									String s = new String(derObj.getEncoded());
									return CpfCnpj.of(s);
								}
								catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}

				}
			}
			catch (CertificateParsingException e) {
				throw new IllegalArgumentException("Erro ao extrair CNPJ do CertificadoDigital", e);
			}
		}
		return null;
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		throw new InvalidObjectException("Proxy required");
	}

	private Object writeReplace() {
		return new SerializationProxy(this);
	}

	private static class SerializationProxy implements Serializable {

		private static final long serialVersionUID = 1L;

		private final byte[] bytes;

		SerializationProxy(CertificadoDigital certificadoDigital) {
			this.bytes = certificadoDigital.bytes;
		}

		private Object readResolve() {
			return CertificadoDigital.of(new ByteArrayInputStream(bytes));
		}

	}

	private static class X509KeySelector extends KeySelector {

		@Override
		public KeySelectorResult select(KeyInfo keyInfo, Purpose purpose, AlgorithmMethod method,
				XMLCryptoContext context) throws KeySelectorException {

			@SuppressWarnings("unchecked")
			Iterator<XMLStructure> ki = keyInfo.getContent().iterator();

			while (ki.hasNext()) {
				XMLStructure info = ki.next();
				if (info instanceof X509Data) {
					X509Data x509Data = (X509Data) info;
					@SuppressWarnings("unchecked")
					Iterator<Object> xi = x509Data.getContent().iterator();
					while (xi.hasNext()) {
						Object o = xi.next();
						if (!(o instanceof X509Certificate))
							continue;
						final PublicKey key = ((X509Certificate) o).getPublicKey();
						if (algEquals(method.getAlgorithm(), key.getAlgorithm())) {
							return new KeySelectorResult() {
								@Override
								public Key getKey() {
									return key;
								}
							};
						}
					}
				}
			}
			throw new KeySelectorException("No KeyValue element found!");
		}

		static boolean algEquals(String algURI, String algName) {
			if (algName.equalsIgnoreCase("DSA") && algURI.equalsIgnoreCase(SignatureMethod.DSA_SHA1)) {
				return true;
			}
			else if (algName.equalsIgnoreCase("RSA") && algURI.equalsIgnoreCase(SignatureMethod.RSA_SHA1)) {
				return true;
			}
			else {
				return false;
			}
		}
	}

}
