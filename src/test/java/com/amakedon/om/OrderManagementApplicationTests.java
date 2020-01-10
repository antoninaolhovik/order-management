package com.amakedon.om;

import com.amakedon.om.data.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class OrderManagementApplicationTests {

	@Test
	void testAddProductSuccess(@Autowired TestRestTemplate restTemplate) throws URISyntaxException {

		final String baseUrl = restTemplate.getRootUri() +"/api/products/";
		URI uri = new URI(baseUrl);

		Product product = new Product();
		product.setName("testProduct");
		product.setPrice(1d);
		product.setSku("testProduct");

		HttpEntity<Product> request = new HttpEntity<>(product);

		ResponseEntity<String> result = restTemplate.postForEntity(uri, request, String.class);

		//Verify request succeed
		Assertions.assertEquals(201, result.getStatusCodeValue());
	}

}
